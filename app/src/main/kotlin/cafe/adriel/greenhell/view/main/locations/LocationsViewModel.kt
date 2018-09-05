package cafe.adriel.greenhell.view.main.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cafe.adriel.greenhell.model.Location
import cafe.adriel.greenhell.repository.LocationRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class LocationsViewModel(private val locationsRepo: LocationRepository) : ViewModel(){

    private val locations = MutableLiveData<List<Location>>()

    fun getLocations(): LiveData<List<Location>> {
        launch(UI) {
            locations.value = locationsRepo.getLocations()
        }
        return locations
    }

    fun saveLocation(location: Location) = launch(UI) {
        locationsRepo.saveLocation(location)
    }

    fun deleteLocation(location: Location) = launch(UI) {
        locationsRepo.deleteLocation(location)
    }

}