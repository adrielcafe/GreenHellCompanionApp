package cafe.adriel.greenhell.view.main

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cafe.adriel.androidcoroutinescopes.viewmodel.CoroutineScopedAndroidViewModel
import cafe.adriel.greenhell.BuildConfig
import cafe.adriel.greenhell.RemoteConfig
import com.crashlytics.android.Crashlytics
import com.github.stephenvinouze.core.managers.KinAppManager
import com.github.stephenvinouze.core.models.KinAppProductType
import com.github.stephenvinouze.core.models.KinAppPurchase
import com.github.stephenvinouze.core.models.KinAppPurchaseResult
import kotlinx.coroutines.experimental.launch

class MainViewModel(app: Application) : CoroutineScopedAndroidViewModel(app), KinAppManager.KinAppListener {

    private val appUpdateAvailable = MutableLiveData<Boolean>()
    private val purchaseCompleted = MutableLiveData<Boolean>()
    private val billingSupported = MutableLiveData<Boolean>()
    private val billingManager by lazy { KinAppManager(getApplication(), BuildConfig.APPLICATION_ID) }

    init {
        billingManager.bind(this)
        launch {
            RemoteConfig.load()
            appUpdateAvailable.value = BuildConfig.VERSION_CODE < RemoteConfig.getMinVersion()
        }
    }

    override fun onCleared() {
        super.onCleared()
        billingManager.unbind()
    }

    override fun onBillingReady() {
        launch {
            try {
                billingSupported.value = billingManager.isBillingSupported(KinAppProductType.INAPP)
                billingManager.restorePurchases(KinAppProductType.INAPP)?.forEach {
                    billingManager.consumePurchase(it)
                }
            } catch (e: Exception){
                Crashlytics.logException(e)
                e.printStackTrace()
                billingSupported.value = false
            }
        }
    }

    override fun onPurchaseFinished(purchaseResult: KinAppPurchaseResult, purchase: KinAppPurchase?) {
        if(purchaseResult == KinAppPurchaseResult.SUCCESS && purchase != null){
            launch {
                billingManager.consumePurchase(purchase)
                purchaseCompleted.value = true
            }
        } else {
            purchaseCompleted.value = false
        }
    }

    fun getAppUpdateAvailable(): LiveData<Boolean> = appUpdateAvailable

    fun getPurchaseCompleted(): LiveData<Boolean> = purchaseCompleted

    fun getBillingSupported(): LiveData<Boolean> = billingSupported

    fun verifyDonation(requestCode: Int, resultCode: Int, data: Intent?) =
        billingManager.verifyPurchase(requestCode, resultCode, data)

    fun donate(activity: Activity, sku: String) {
        if(BuildConfig.RELEASE) {
            if(sku.isNotEmpty())
                billingManager.purchase(activity, sku, KinAppProductType.INAPP)
        } else {
            billingManager.purchase(activity, KinAppManager.TEST_PURCHASE_SUCCESS, KinAppProductType.INAPP)
        }
    }

}