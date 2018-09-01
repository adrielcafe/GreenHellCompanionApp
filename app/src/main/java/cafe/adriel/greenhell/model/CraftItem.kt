package cafe.adriel.greenhell.model

import io.mironov.smuggler.AutoParcelable

data class CraftItem(
    val name: String,
    val description: String,
    val category: CraftCategory) : AutoParcelable