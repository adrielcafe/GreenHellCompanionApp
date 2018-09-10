package cafe.adriel.greenhell

import android.app.Activity
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.RawRes
import androidx.core.app.ShareCompat
import androidx.core.content.getSystemService
import androidx.core.content.res.ResourcesCompat
import okio.BufferedSource
import okio.Okio
import java.io.InputStream
import java.text.Normalizer

inline fun <reified T : Any> classTag(): String = T::class.java.simpleName

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Context.colorFrom(@ColorRes colorRes: Int) = ResourcesCompat.getColor(resources, colorRes, theme)

fun String.share(activity: Activity) =
    ShareCompat.IntentBuilder
        .from(activity)
        .setText(this)
        .setType(ClipDescription.MIMETYPE_TEXT_PLAIN)
        .startChooser()

fun String.normalize() =
    Regex("\\p{InCombiningDiacriticalMarks}+")
        .replace(Normalizer.normalize(this, Normalizer.Form.NFD), "")
        .toLowerCase()

fun Uri.open(context: Context) =
    context.startActivity(Intent(Intent.ACTION_VIEW).apply { data = this@open })

fun View.inflater() = context.getSystemService<LayoutInflater>()!!

fun ViewGroup.inflate(@LayoutRes resId: Int): View = inflater().inflate(resId, this, true)

fun Context.raw(@RawRes resId: Int): InputStream = resources.openRawResource(resId)

fun InputStream.buffer(): BufferedSource = Okio.buffer(Okio.source(this))