package cafe.adriel.greenhell.repository

import android.content.Context
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.buffer
import cafe.adriel.greenhell.model.CraftItem
import cafe.adriel.greenhell.raw
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class CraftingRepository(private val appContext: Context) {

    companion object {
        private const val DB_CRAFT_ITEMS = R.raw.craft_items
    }

    private val moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    private val craftItemListAdapter by lazy {
        val type = Types.newParameterizedType(List::class.java, CraftItem::class.java)
        moshi.adapter<List<CraftItem>>(type)
    }

    fun getCraftItems(): List<CraftItem> {
        val itemsJson = appContext.raw(DB_CRAFT_ITEMS).buffer()
        val items = craftItemListAdapter.fromJson(itemsJson)?.sortedBy { it.name }
        return items ?: emptyList()
    }

}