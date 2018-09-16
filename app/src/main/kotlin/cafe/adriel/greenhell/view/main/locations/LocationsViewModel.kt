package cafe.adriel.greenhell.view.main.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cafe.adriel.androidcoroutinescopes.viewmodel.CoroutineScopedViewModel
import cafe.adriel.greenhell.model.Location
import cafe.adriel.greenhell.repository.LocationRepository
import kotlinx.coroutines.experimental.launch

class LocationsViewModel(private val locationsRepo: LocationRepository) : CoroutineScopedViewModel(){

    private val locations = MutableLiveData<List<Location>>()

    fun getLocations(): LiveData<List<Location>> {
        launch {
            locations.value = locationsRepo.getLocations()
        }
        return locations
    }

    fun saveLocation(location: Location) = launch {
        locationsRepo.saveLocation(location)
    }

    fun deleteLocation(location: Location) = launch {
        locationsRepo.deleteLocation(location)
    }

}