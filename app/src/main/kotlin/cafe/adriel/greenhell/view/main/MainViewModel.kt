package cafe.adriel.greenhell.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cafe.adriel.greenhell.BuildConfig
import cafe.adriel.greenhell.RemoteConfig

class MainViewModel : ViewModel(){

    private val appUpdateAvailable = MutableLiveData<Boolean>()

    init {
        RemoteConfig.load {
            appUpdateAvailable.value = BuildConfig.VERSION_CODE < RemoteConfig.getMinVersion()
        }
    }

    fun getAppUpdateAvailable(): LiveData<Boolean> = appUpdateAvailable

}