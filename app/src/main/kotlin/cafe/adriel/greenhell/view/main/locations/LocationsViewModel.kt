package cafe.adriel.greenhell.view.main.locations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cafe.adriel.greenhell.model.Location
import cafe.adriel.greenhell.repository.LocationRepository

class LocationsViewModel(private val locationsRepo: LocationRepository) : ViewModel(){

    private val locations = MutableLiveData<List<Location>>()

    fun getLocations() = locations.apply {
        value = locationsRepo.getLocations()
    }

    fun saveLocation(location: Location) = locationsRepo.saveLocation(location)

    fun deleteLocation(location: Location) = locationsRepo.deleteLocation(location)

}