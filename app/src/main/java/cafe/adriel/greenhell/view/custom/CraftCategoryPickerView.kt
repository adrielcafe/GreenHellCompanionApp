package cafe.adriel.greenhell.view.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.model.CraftCategory
import com.github.ajalt.timberkt.e
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.view_craft_category_picker.view.*

class CraftCategoryPickerView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private lateinit var listener: (CraftCategory) -> Unit
    private lateinit var adapter: FastItemAdapter<CraftCategoryAdapterItem>

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_craft_category_picker, this, true)

        if(!::adapter.isInitialized) {
            adapter = FastItemAdapter()
            adapter.setHasStableIds(true)
            adapter.withSelectable(true)
            adapter.withOnClickListener { v, adapter, item, position ->
                v?.let { onListItemClicked(it, item, position) }
                true
            }
        }

        with(view){
            vCategories.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            vCategories.adapter = adapter
        }

        val items = CraftCategory.values().map { CraftCategoryAdapterItem(it) }
        adapter.clear()
        adapter.add(items)
        selectDefaultCategory()
    }

    private fun onListItemClicked(view: View, item: CraftCategoryAdapterItem, position: Int) {
        selectCategory(view, item, position)
        listener(item.craftCategory)
    }

    private fun selectCategory(view: View, item: CraftCategoryAdapterItem, position: Int){
        unSelectAllCategories()
        with(view){
            item.setCategorySelected(this, true)
        }
    }

    private fun selectDefaultCategory(){
        unSelectAllCategories()

        vCategories.post {
            val item = adapter.adapterItems.first()
            val viewHolder = vCategories.findViewHolderForAdapterPosition(0)
            e { "DEFAULT ${item.craftCategory} $viewHolder" }
            viewHolder?.itemView?.run {
                item.setCategorySelected(this, true)
            }
        }
    }

    private fun unSelectAllCategories(){
        adapter.adapterItems.forEachIndexed { index, item ->
            val viewHolder = vCategories.findViewHolderForAdapterPosition(index)
            viewHolder?.itemView?.run {
                item.setCategorySelected(this, false)
            }
        }
    }

    fun setListener(listener: (CraftCategory) -> Unit){
        this.listener = listener
    }

    inner class CraftCategoryAdapterItem(val craftCategory: CraftCategory) :
        AbstractItem<CraftCategoryAdapterItem, CraftCategoryAdapterItem.ViewHolder>() {

        override fun getIdentifier() = craftCategory.name.hashCode().toLong()

        override fun getLayoutRes() = R.layout.item_craft_category

        override fun getType() = layoutRes

        override fun getViewHolder(v: View) = ViewHolder(v)

        override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
            super.bindView(holder, payloads)
            with(holder.itemView as AppCompatButton){
                text = craftCategory.name
            }
        }

        override fun unbindView(holder: ViewHolder) {
            super.unbindView(holder)
            with(holder.itemView as AppCompatButton){
                text = ""
                setCategorySelected(this, false)
            }
        }

        fun setCategorySelected(view: View, selected: Boolean){
            with(view as AppCompatButton) {
                if (selected) {
                    setTextColor(Color.WHITE)
                    setBackgroundResource(R.drawable.bg_fill_accent_ripple)
                } else {
                    setTextColor(Color.BLACK)
                    setBackgroundResource(R.drawable.bg_fill_white_ripple)
                }
            }
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }

}