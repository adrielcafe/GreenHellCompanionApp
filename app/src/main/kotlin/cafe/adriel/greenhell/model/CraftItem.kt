package cafe.adriel.greenhell.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CraftItem(
    val name: String,
    val description: String,
    val category: CraftCategory) : Parcelable