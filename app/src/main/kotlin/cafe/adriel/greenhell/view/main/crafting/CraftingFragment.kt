package cafe.adriel.greenhell.view.main.crafting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.greenhell.Analytics
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.model.CraftItem
import cafe.adriel.greenhell.share
import com.kennyc.view.MultiStateView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.listeners.ItemFilterListener
import kotlinx.android.synthetic.main.fragment_crafting.*
import kotlinx.android.synthetic.main.fragment_crafting.view.*
import kotlinx.android.synthetic.main.item_crafting.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CraftingFragment : Fragment() {

    companion object {
        fun newInstance() = CraftingFragment()
    }

    private val viewModel by viewModel<CraftingViewModel>()
    private lateinit var adapter: FastItemAdapter<CraftItemAdapterItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_crafting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!::adapter.isInitialized) {
            adapter = FastItemAdapter()
            adapter.apply {
                setHasStableIds(true)
                withUseIdDistributor(true)
                withEventHook(object : ClickEventHook<CraftItemAdapterItem>() {
                    override fun onBindMany(viewHolder: RecyclerView.ViewHolder) =
                        viewHolder.itemView.run { listOf(vShare) }

                    override fun onClick(view: View?, position: Int, fastAdapter: FastAdapter<CraftItemAdapterItem>?, item: CraftItemAdapterItem?) {
                        if (view != null && item != null) {
                            onListItemClicked(view, item)
                        }
                    }
                })
                itemFilter.withFilterPredicate { item, constraint ->
                    constraint == getString(item.craftItem.category.nameResId)
                }
                itemFilter.withItemFilterListener(object : ItemFilterListener<CraftItemAdapterItem> {
                    override fun itemsFiltered(constraint: CharSequence?, results: MutableList<CraftItemAdapterItem>?) {
                        updateState()
                    }
                    override fun onReset() {
                        updateState()
                    }
                })
            }
        }

        with(view){
            vCraftItems.layoutManager = LinearLayoutManager(context)
            vCraftItems.adapter = adapter

            vCraftCategories.setListener { onCategorySelected(it) }
        }

        viewModel.getCraftItems().observe(this, Observer { showCraftItems(it) })
    }

    private fun onListItemClicked(view: View, item: CraftItemAdapterItem){
        when(view.id){
            R.id.vShare -> shareCraftItem(item.craftItem)
        }
    }

    private fun onCategorySelected(categoryName: String){
        adapter.filter(categoryName)
    }

    private fun showCraftItems(craftItems: List<CraftItem>){
        val adapterItems = craftItems.map { CraftItemAdapterItem(it) }
        adapter.itemFilter.clear()
        adapter.itemFilter.add(adapterItems)
        vCraftCategories?.selectDefaultCategory()
        updateState()
    }

    private fun shareCraftItem(craftItem: CraftItem){
        activity?.run {
            "${craftItem.name} recipe:\n${craftItem.description}".share(this)
            Analytics.logShareCraftItem(craftItem)
        }
    }

    private fun updateState(){
        vState?.viewState = if(adapter.adapterItems.isEmpty())
            MultiStateView.VIEW_STATE_EMPTY
        else
            MultiStateView.VIEW_STATE_CONTENT
    }

}