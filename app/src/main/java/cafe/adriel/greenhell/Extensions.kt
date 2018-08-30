package cafe.adriel.greenhell

import android.content.res.Resources

inline fun <reified T : Any> getClassTag(): String = T::class.java.simpleName

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()