package cafe.adriel.greenhell

import android.content.Context
import android.os.Bundle
import cafe.adriel.greenhell.model.Location
import com.google.firebase.analytics.FirebaseAnalytics

object Analytics {
    private const val EVENT_SAVE_LOCATION = "save_location"
    private const val PARAM_NAME = "name"
    private const val PARAM_WEST = "west"
    private const val PARAM_SOUTH = "south"

    private var analytics: FirebaseAnalytics? = null

    fun init(context: Context){
        if(analytics == null){
            analytics = FirebaseAnalytics.getInstance(context.applicationContext)
        }
    }

    fun logSaveLocation(location: Location){
        val params = Bundle().apply {
            putString(PARAM_NAME, location.name)
            putInt(PARAM_WEST, location.westPosition)
            putInt(PARAM_SOUTH, location.southPosition)
        }
        analytics?.logEvent(EVENT_SAVE_LOCATION, params)
    }

}