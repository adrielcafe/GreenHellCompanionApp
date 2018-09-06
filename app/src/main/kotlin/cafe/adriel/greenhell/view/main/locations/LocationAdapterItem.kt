package cafe.adriel.greenhell.view.main.locations

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.model.Location
import cafe.adriel.greenhell.model.LocationCategory
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter_extensions.drag.IDraggable
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.item_location.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class LocationAdapterItem(val location: Location) :
    AbstractItem<LocationAdapterItem, LocationAdapterItem.ViewHolder>(),
    IDraggable<LocationAdapterItem, LocationAdapterItem> {

    private val userLocation = location.category == LocationCategory.MY_LOCATIONS

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
            if(userLocation) {
                vDrag.visibility = View.VISIBLE
                vEditLayout.visibility = View.VISIBLE
                vDeleteLayout.visibility = View.VISIBLE
            } else {
                vDrag.visibility = View.GONE
                vEditLayout.visibility = View.GONE
                vDeleteLayout.visibility = View.GONE
            }
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

    override fun isDraggable() = userLocation

    override fun withIsDraggable(draggable: Boolean): LocationAdapterItem {
        return this
    }

    fun closeSwipeMenu(view: View){
        launch(UI) {
            // Short delay for smooth animation
            delay(200)
            view.vSwipeMenu.smoothCloseMenu()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}