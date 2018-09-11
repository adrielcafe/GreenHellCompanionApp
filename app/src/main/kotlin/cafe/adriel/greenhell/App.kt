package cafe.adriel.greenhell

import android.app.Application
import cafe.adriel.greenhell.repository.CraftingRepository
import cafe.adriel.greenhell.repository.LocationRepository
import cafe.adriel.greenhell.view.main.MainViewModel
import cafe.adriel.greenhell.view.main.crafting.CraftingViewModel
import cafe.adriel.greenhell.view.main.locations.LocationsViewModel
import com.esotericsoftware.minlog.Log
import com.github.ajalt.timberkt.Timber
import io.paperdb.Paper
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.core.Koin
import org.koin.dsl.module.module
import org.koin.log.EmptyLogger

class App : Application() {

    companion object {
        // Contact Links
        const val EMAIL = "me@adriel.cafe"
        const val WEBSITE = "http://adriel.cafe"
        const val GITHUB_PROFILE_URL = "https://github.com/adrielcafe"
        const val LINKEDIN_PROFILE_URL = "https://linkedin.com/in/adrielcafe"
        const val PROJECT_REPO_URL = "https://github.com/adrielcafe/GreenHellCompanionApp"

        // App Links
        const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        const val MARKET_URL = "market://details?id=${BuildConfig.APPLICATION_ID}"

        // In-App Purchase
        const val PRODUCT_SKU_COFFEE_1 = "coffee_1"
        const val PRODUCT_SKU_COFFEE_3 = "coffee_3"
        const val PRODUCT_SKU_COFFEE_5 = "coffee_5"
    }

    // DI
    private val viewModelsModule = module {
        viewModel { MainViewModel(this@App) }
        viewModel { LocationsViewModel(get()) }
        viewModel { CraftingViewModel(get()) }
    }
    private val repositoriesModule = module {
        single { LocationRepository(this@App) }
        single { CraftingRepository(this@App) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(repositoriesModule, viewModelsModule))
        Analytics.init(this)
        Paper.init(this)
        initLogging()
    }

    private fun initLogging(){
        if (BuildConfig.RELEASE) {
            Paper.setLogLevel(Log.LEVEL_NONE)
            Koin.logger = EmptyLogger()
        } else {
            Paper.setLogLevel(Log.LEVEL_DEBUG)
            Timber.plant(Timber.DebugTree())
            Koin.logger = AndroidLogger()
        }
    }

}