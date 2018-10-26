package cafe.adriel.greenhell.repository

import android.content.Context
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.buffer
import cafe.adriel.greenhell.model.Location
import cafe.adriel.greenhell.model.LocationCategory
import cafe.adriel.greenhell.normalize
import cafe.adriel.greenhell.raw
import com.crashlytics.android.Crashlytics
import io.paperdb.Paper
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.withContext

class LocationRepository(private val appContext: Context) {

    companion object {
        private const val DB_USER_LOCATIONS = "locations"
        private const val JSON_LOCATIONS = R.raw.locations
    }

    private val dbUserLocations by lazy { Paper.book(DB_USER_LOCATIONS) }
    private val locationListAdapter by lazy { JsonAdapterFactory.getListAdapter<Location>() }

    suspend fun getLocations() = withContext(IO){
        val defaultLocationsJson = appContext.raw(JSON_LOCATIONS).buffer()
        val defaultLocations = locationListAdapter.fromJson(defaultLocationsJson) ?: emptyList()

        val userLocations = dbUserLocations.allKeys.map {
            dbUserLocations.read<Location>(it).apply {
                category = LocationCategory.MY_LOCATIONS
            }
        }

        (defaultLocations + userLocations).sortedWith(getLocationComparator())
    }

    suspend fun saveLocation(location: Location) {
        withContext(IO){
            try {
                dbUserLocations.write(location.id, location)
            } catch (e: IllegalArgumentException){
                val errorMessage = "Location ID: ${location.id}, Error: ${e.message}"
                Crashlytics.logException(IllegalArgumentException(errorMessage))
                e.printStackTrace()
            }
        }
    }

    suspend fun deleteLocation(location: Location) {
        withContext(IO){
            dbUserLocations.delete(location.id)
        }
    }

    private fun getLocationComparator() = Comparator<Location> { l1, l2 ->
        val indexComparison = l1.index.compareTo(l2.index)
        if(indexComparison == 0){
            l1.name.normalize().compareTo(l2.name.normalize())
        } else {
            indexComparison
        }
    }

}