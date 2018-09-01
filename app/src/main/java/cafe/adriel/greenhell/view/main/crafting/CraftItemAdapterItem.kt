package cafe.adriel.greenhell.view.main.crafting

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.model.CraftItem
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_craft_item.view.*

class CraftItemAdapterItem(val craftItem: CraftItem) :
    AbstractItem<CraftItemAdapterItem, CraftItemAdapterItem.ViewHolder>() {

    override fun getIdentifier() = craftItem.name.hashCode().toLong()

    override fun getLayoutRes() = R.layout.item_craft_item

    override fun getType() = layoutRes

    override fun getViewHolder(v: View) = ViewHolder(v)

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        with(holder.itemView){
            vName.text = craftItem.name
            vDescription.text = craftItem.description
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        with(holder.itemView){
            vName.text = ""
            vDescription.text = ""
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}