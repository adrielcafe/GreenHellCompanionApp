package cafe.adriel.greenhell.model

import androidx.annotation.StringRes
import cafe.adriel.greenhell.R

enum class LocationCategory(@StringRes val nameResId: Int) {

    MY_LOCATIONS(R.string.my_locations),
    BLUEPRINT(R.string.blueprints),
    UNIQUE_ITEM(R.string.unique_items),
//    CAVE(R.string.cave),
//    WATER(R.string.water),
//    OTHER(R.string.other)

}