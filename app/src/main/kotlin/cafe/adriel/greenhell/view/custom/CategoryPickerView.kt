package cafe.adriel.greenhell.view.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.inflate
import cafe.adriel.greenhell.model.CraftCategory
import cafe.adriel.greenhell.model.LocationCategory
import cafe.adriel.greenhell.normalize
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.view_category_picker.view.*
import java.util.*

class CategoryPickerView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    companion object {
        private const val TYPE_LOCATION = 0
        private const val TYPE_CRAFTING = 1
    }

    private lateinit var adapter: FastItemAdapter<CategoryAdapterItem>
    private var listener: ((String) -> Unit)? = null

    init {
        val styleAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.CategoryPickerView, 0, 0)
        val type = styleAttrs.getInt(R.styleable.CategoryPickerView_cpvType, -1)
        styleAttrs.recycle()

        if(!::adapter.isInitialized) {
            adapter = FastItemAdapter()
            adapter.apply {
                setHasStableIds(true)
                withUseIdDistributor(true)
                withSelectable(true)
                withOnClickListener { view, _, item, _ ->
                    view?.let { onListItemClicked(it, item) }
                    true
                }
            }
        }

        with(inflate(R.layout.view_category_picker)){
            vCategories.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            vCategories.adapter = adapter
        }

        loadCategories(type)
    }

    private fun onListItemClicked(view: View, item: CategoryAdapterItem) {
        selectCategory(view, item)
    }

    private fun loadCategories(type: Int){
        val items = when (type){
                TYPE_LOCATION -> LocationCategory.values().map { context.getString(it.nameResId) }
                TYPE_CRAFTING -> CraftCategory.values().map { context.getString(it.nameResId) }
                else -> emptyList()
            }
            .sortedWith(getCategoryComparator(type))
            .map { CategoryAdapterItem(it) }
        adapter.clear()
        adapter.add(items)
    }

    fun selectDefaultCategory(){
        vCategories.post {
            vCategories.findViewHolderForAdapterPosition(0)?.itemView?.run {
                val firstItem = adapter.adapterItems.first()
                selectCategory(this, firstItem)
            }
        }
    }

    private fun selectCategory(view: View, item: CategoryAdapterItem){
        unSelectAllCategories()
        with(view) {
            item.setCategorySelected(this, true)
        }
        listener?.invoke(item.name)
    }

    private fun unSelectAllCategories(){
        adapter.adapterItems.forEachIndexed { index, item ->
            vCategories.findViewHolderForAdapterPosition(index)?.itemView?.run {
                item.setCategorySelected(this, false)
            }
        }
    }

    private fun getCategoryComparator(type: Int): Comparator<String> =
        when(type){
            // "My Locations" category should be first and "Other" category the last,
            // No matter what the current language is
            TYPE_LOCATION -> Comparator { c1, c2 ->
                if(c1 == context.getString(R.string.my_locations) || c2 == context.getString(R.string.my_locations)
                    || c1 == context.getString(R.string.other) || c2 == context.getString(R.string.other))
                    0
                else
                    c1.normalize().compareTo(c2.normalize())
            }
            else -> Comparator { c1, c2 ->
                c1.normalize().compareTo(c2.normalize())
            }
        }

    fun setListener(listener: (String) -> Unit){
        this.listener = listener
    }

    private inner class CategoryAdapterItem(val name: String) :
        AbstractItem<CategoryAdapterItem, CategoryAdapterItem.ViewHolder>() {

        override fun getLayoutRes() = R.layout.item_category

        override fun getType() = layoutRes

        override fun getViewHolder(v: View) = ViewHolder(v)

        override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
            super.bindView(holder, payloads)
            with(holder.itemView as AppCompatButton){
                text = name
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