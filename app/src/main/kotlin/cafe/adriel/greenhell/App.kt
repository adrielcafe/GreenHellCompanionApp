package cafe.adriel.greenhell

import android.app.Application
import android.net.Uri
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
        val PLAY_STORE_URL = Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
        val MARKET_URL = Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
    }

    // DI
    private val viewModelsModule = module {
        viewModel { MainViewModel() }
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