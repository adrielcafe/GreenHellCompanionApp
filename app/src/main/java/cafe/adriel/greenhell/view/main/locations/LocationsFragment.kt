package cafe.adriel.greenhell.view.main.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.greenhell.*
import cafe.adriel.greenhell.model.Location
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.kennyc.view.MultiStateView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback
import kotlinx.android.synthetic.main.fragment_locations.*
import kotlinx.android.synthetic.main.fragment_locations.view.*
import kotlinx.android.synthetic.main.item_location.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class LocationsFragment : Fragment(), ItemTouchCallback {

    companion object {
        fun newInstance() = LocationsFragment()
    }

    private val viewModel by viewModel<LocationsViewModel>()
    private lateinit var adapter: FastItemAdapter<LocationAdapterItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_locations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!::adapter.isInitialized) {
            adapter = FastItemAdapter()
            adapter.setHasStableIds(true)
            adapter.withEventHook(object : ClickEventHook<LocationAdapterItem>() {
                override fun onBindMany(viewHolder: RecyclerView.ViewHolder) =
                    viewHolder.itemView.run { listOf(vShare, vEdit, vDelete) }

                override fun onClick(view: View?, position: Int, fastAdapter: FastAdapter<LocationAdapterItem>?, item: LocationAdapterItem?) {
                    if (view != null && item != null) {
                        onListItemClicked(view, item, position)
                    }
                }
            })
        }

        with(view){
            vLocations.setHasFixedSize(true)
            vLocations.layoutManager = LinearLayoutManager(context)
            vLocations.adapter = adapter

            ItemTouchHelper(SimpleDragCallback(this@LocationsFragment))
                .attachToRecyclerView(vLocations)
        }

        viewModel.getLocations().observe(this, Observer { showLocations(it) })
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int): Boolean {
        Collections.swap(adapter.adapterItems, oldPosition, newPosition)
        adapter.notifyAdapterItemMoved(oldPosition, newPosition)
        return true
    }

    override fun itemTouchDropped(oldPosition: Int, newPosition: Int) {
        // Save locations with current index
        adapter.adapterItems.forEachIndexed { index, item ->
            item.location.run {
                this.index = index
                viewModel.saveLocation(this)
            }
        }
    }

    private fun onListItemClicked(view: View, item: LocationAdapterItem, position: Int){
        closeSwipeMenu(position)
        when(view.id){
            R.id.vShare -> shareLocation(item.location)
            R.id.vEdit -> showLocationEditorDialog(item.location)
            R.id.vDelete -> deleteLocation(item.location)
        }
    }

    private fun showLocations(locations: List<Location>){
        val adapterItems = locations.map { LocationAdapterItem(it) }
        adapter.clear()
        adapter.add(adapterItems)
        updateState()
    }

    private fun showLocationEditorDialog(location: Location? = null){
        fragmentManager?.let { LocationEditorDialog.show(it, location) }
    }

    private fun saveLocation(location: Location){
        val position = getItemPositionByLocation(location)
        if(position >= 0) {
            adapter.notifyAdapterItemChanged(position)
        } else {
            adapter.add(LocationAdapterItem(location))
        }
        viewModel.saveLocation(location)
        updateState()
    }

    private fun deleteLocation(location: Location){
        val position = getItemPositionByLocation(location)
        var shouldDelete = true
        adapter.remove(position)
        updateState()
        activity?.run {
            Snackbar.make(findViewById(R.id.vRoot), R.string.location_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo) {
                    shouldDelete = false
                    adapter.add(position, LocationAdapterItem(location))
                    updateState()
                }
                .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>(){
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        if(shouldDelete) {
                            viewModel.deleteLocation(location)
                        }
                        updateState()
                    }
                })
                .show()
        }
    }

    private fun shareLocation(location: Location){
        activity?.run {
            "${location.westPosition}'W ${location.southPosition}'S: ${location.name}".share(this)
            Analytics.logShareLocation(location)
        }
    }

    private fun updateState(){
        vState.viewState =
                if(adapter.adapterItems.isEmpty()) MultiStateView.VIEW_STATE_EMPTY
                else MultiStateView.VIEW_STATE_CONTENT
    }

    private fun closeSwipeMenu(position: Int){
        val view = vLocations.getChildAt(position)
        view?.let { adapter.getAdapterItem(position).closeSwipeMenu(it) }
    }

    private fun getItemPositionByLocation(location: Location) =
        adapter.adapterItems.indexOfFirst { location.id == it.location.id }

    @Subscribe
    fun onEvent(event: AddLocationEvent){
        showLocationEditorDialog()
    }

    @Subscribe
    fun onEvent(event: SaveLocationEvent){
        saveLocation(event.location)
        Analytics.logSaveLocation(event.location)
    }
}