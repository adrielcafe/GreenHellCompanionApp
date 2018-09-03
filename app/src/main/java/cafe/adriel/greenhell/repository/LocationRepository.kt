package cafe.adriel.greenhell.repository

import android.content.Context
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.buffer
import cafe.adriel.greenhell.model.Location
import cafe.adriel.greenhell.model.LocationCategory
import cafe.adriel.greenhell.raw
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.paperdb.Paper

class LocationRepository(private val appContext: Context) {

    companion object {
        private const val DB_USER_LOCATIONS = "locations"
        private const val JSON_DEFAULT_LOCATIONS = R.raw.locations
    }

    private val moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    private val locationListAdapter by lazy {
        val type = Types.newParameterizedType(List::class.java, Location::class.java)
        moshi.adapter<List<Location>>(type)
    }

    fun getLocations(): List<Location> {
        val defaultLocationsJson = appContext.raw(JSON_DEFAULT_LOCATIONS).buffer()
        val defaultLocations = locationListAdapter.fromJson(defaultLocationsJson) ?: emptyList()
        val userLocations = Paper.book(DB_USER_LOCATIONS).allKeys
            .map {
                val location = Paper.book(DB_USER_LOCATIONS).read(it) as Location
                if (location.category == null) location.category = LocationCategory.MY_LOCATIONS
                location
            }
        return (defaultLocations + userLocations).sortedWith(Comparator { l1, l2 ->
            val indexComparison = l1.index.compareTo(l2.index)
            if(indexComparison == 0){
               l1.name.compareTo(l2.name)
            } else {
                indexComparison
            }
        })
    }

    fun saveLocation(location: Location) {
        Paper.book(DB_USER_LOCATIONS).write(location.id, location)
    }

    fun deleteLocation(location: Location){
        Paper.book(DB_USER_LOCATIONS).delete(location.id)
    }

}