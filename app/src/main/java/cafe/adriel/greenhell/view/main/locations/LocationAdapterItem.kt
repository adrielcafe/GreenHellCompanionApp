package cafe.adriel.greenhell.view.main.locations

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.model.Location
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter_extensions.drag.IDraggable
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.item_location.view.*

class LocationAdapterItem(val location: Location) :
    AbstractItem<LocationAdapterItem, LocationAdapterItem.ViewHolder>(),
    IDraggable<LocationAdapterItem, LocationAdapterItem> {

    private var draggable = true

    override fun getIdentifier() = location.id.hashCode().toLong()

    override fun getLayoutRes() = R.layout.item_location

    override fun getType() = layoutRes

    override fun getViewHolder(v: View) = ViewHolder(v)

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        val westPosition = NumberPicker.getTwoDigitFormatter().format(location.westPosition)
        val southPosition = NumberPicker.getTwoDigitFormatter().format(location.southPosition)
        with(holder.itemView){
            vName.text = location.name
            vWestPosition.text = "$westPosition'W"
            vSouthPosition.text = "$southPosition'S"
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        with(holder.itemView){
            vName.text = ""
            vWestPosition.text = ""
            vSouthPosition.text = ""
            vSwipeMenu.smoothCloseMenu()
        }
    }

    override fun isDraggable() = draggable

    override fun withIsDraggable(draggable: Boolean): LocationAdapterItem {
        this.draggable = draggable
        return this
    }

    fun closeSwipeMenu(view: View){
        with(view){
            vSwipeMenu.smoothCloseMenu()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}