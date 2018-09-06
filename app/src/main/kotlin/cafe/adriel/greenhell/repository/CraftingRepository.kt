package cafe.adriel.greenhell.repository

import android.content.Context
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.buffer
import cafe.adriel.greenhell.model.CraftItem
import cafe.adriel.greenhell.normalize
import cafe.adriel.greenhell.raw
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.withContext

class CraftingRepository(private val appContext: Context) {

    companion object {
        private const val JSON_CRAFT_ITEMS = R.raw.craft_items
    }

    private val craftItemListAdapter by lazy { JsonAdapterFactory.getListAdapter<CraftItem>() }

    suspend fun getCraftItems() = withContext(IO){
        val itemsJson = appContext.raw(JSON_CRAFT_ITEMS).buffer()
        val items = craftItemListAdapter.fromJson(itemsJson) ?: emptyList()
        items.sortedBy { it.name.normalize() }
    }

}