package cafe.adriel.greenhell.repository

import cafe.adriel.greenhell.model.Location
import cafe.adriel.greenhell.model.LocationCategory
import io.paperdb.Paper

class LocationRepository {

    companion object {
        private const val DB_LOCATION = "locations"
    }

    fun getLocations() =
        Paper.book(DB_LOCATION).allKeys
            .map {
                val location = Paper.book(DB_LOCATION).read(it) as Location
                if(location.category == null) location.category = LocationCategory.MY_LOCATIONS
                location
            }
            .sortedBy { it.index }

    fun saveLocation(location: Location) {
        Paper.book(DB_LOCATION).write(location.id, location)
    }

    fun deleteLocation(location: Location){
        Paper.book(DB_LOCATION).delete(location.id)
    }

}