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
import cafe.adriel.greenhell.model.LocationCategory
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.view_category_picker.view.*

class CategoryPickerView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    companion object {
        private const val TYPE_LOCATION = 0
        private const val TYPE_CRAFTING = 1
    }

    private lateinit var listener: (String) -> Unit
    private lateinit var adapter: FastItemAdapter<CategoryAdapterItem>

    init {
        val styleAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.CategoryPickerView, 0, 0)
        val type = styleAttrs.getInt(R.styleable.CategoryPickerView_cpvType, -1)
        styleAttrs.recycle()

        if(!::adapter.isInitialized) {
            adapter = FastItemAdapter()
            adapter.setHasStableIds(true)
            adapter.withSelectable(true)
            adapter.withOnClickListener { v, _, item, position ->
                v?.let { onListItemClicked(it, item, position) }
                true
            }
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_category_picker, this, true)
        with(view){
            vCategories.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            vCategories.adapter = adapter
        }

        loadCategories(type)
        selectDefaultCategory()
    }

    private fun onListItemClicked(view: View, item: CategoryAdapterItem, position: Int) {
        selectCategory(view, item)
        listener(item.name)
    }

    private fun loadCategories(type: Int){
        val categories = when (type){
            TYPE_LOCATION -> LocationCategory.values().map { context.getString(it.nameResId) }
            TYPE_CRAFTING -> CraftCategory.values().map { context.getString(it.nameResId) }.sorted()
            else -> emptyList()
        }
        val items = categories.map { CategoryAdapterItem(it) }
        adapter.clear()
        adapter.add(items)
    }

    private fun selectCategory(view: View, item: CategoryAdapterItem){
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

    fun setListener(listener: (String) -> Unit){
        this.listener = listener
    }

    inner class CategoryAdapterItem(val name: String) :
        AbstractItem<CategoryAdapterItem, CategoryAdapterItem.ViewHolder>() {

        override fun getIdentifier() = name.hashCode().toLong()

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