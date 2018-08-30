package cafe.adriel.greenhell.repository

import cafe.adriel.greenhell.model.Location
import io.paperdb.Paper

class LocationRepository {

    companion object {
        private const val DB_LOCATION = "locations"
    }

    fun getLocations() =
        Paper.book(DB_LOCATION).allKeys
            .map { Paper.book(DB_LOCATION).read(it) as Location }
            .sortedBy { it.index }

    fun saveLocation(location: Location) {
        Paper.book(DB_LOCATION).write(location.id, location)
    }

    fun deleteLocation(location: Location){
        Paper.book(DB_LOCATION).delete(location.id)
    }

}