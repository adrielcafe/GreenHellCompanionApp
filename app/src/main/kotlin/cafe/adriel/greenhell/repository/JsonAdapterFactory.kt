package cafe.adriel.greenhell.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object JsonAdapterFactory {

    val moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    inline fun <reified T> getAdapter() =
        moshi.adapter<T>(T::class.java)

    inline fun <reified T> getListAdapter() =
        moshi.adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))

}