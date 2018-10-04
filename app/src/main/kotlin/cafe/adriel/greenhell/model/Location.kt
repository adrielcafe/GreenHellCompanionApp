package cafe.adriel.greenhell.model

import android.os.Parcelable
import androidx.annotation.IntRange
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Location(
    var name: String,
    @IntRange(from = 0, to = 99) var westPosition: Int,
    @IntRange(from = 0, to = 99) var southPosition: Int,
    var category: LocationCategory = LocationCategory.MY_LOCATIONS,
    var index: Int = Int.MAX_VALUE) : Parcelable {

    @Transient
    val id = UUID.randomUUID().toString()

}