package cafe.adriel.greenhell.model

import androidx.annotation.StringRes
import cafe.adriel.greenhell.R

enum class CraftCategory(@StringRes val nameResId: Int) {

    CAMPSITE(R.string.campsite),
    FOOD(R.string.food),
    MEDICINE(R.string.medicine),
    SHELTER(R.string.shelter),
    TOOL(R.string.tool),
    TRAP(R.string.trap),
    WEAPON(R.string.weapon)

}