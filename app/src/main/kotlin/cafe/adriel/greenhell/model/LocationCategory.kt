package cafe.adriel.greenhell.model

import android.os.Parcelable
import androidx.annotation.StringRes
import cafe.adriel.greenhell.R
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class LocationCategory(@StringRes val nameResId: Int) : Parcelable {

    MY_LOCATIONS(R.string.my_locations),
    BLUEPRINT(R.string.blueprints),
    UNIQUE_ITEM(R.string.unique_items),

    // TODO add later
//    CAVE(R.string.cave),
//    WATER(R.string.water),
//    OTHER(R.string.other)

}