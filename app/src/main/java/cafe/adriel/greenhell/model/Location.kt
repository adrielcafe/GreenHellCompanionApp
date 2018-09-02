package cafe.adriel.greenhell.model

import androidx.annotation.IntRange
import io.mironov.smuggler.AutoParcelable
import java.util.*

data class Location(
    var name: String,
    @IntRange(from = 0, to = 99) var westPosition: Int,
    @IntRange(from = 0, to = 99) var southPosition: Int,
    var category: LocationCategory = LocationCategory.MY_LOCATIONS,
    var index: Int = Int.MAX_VALUE) : AutoParcelable {

    val id = UUID.randomUUID().toString()

}