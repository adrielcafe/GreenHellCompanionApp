package cafe.adriel.greenhell

import android.app.Application
import cafe.adriel.greenhell.repository.LocationRepository
import cafe.adriel.greenhell.view.main.locations.LocationsViewModel
import com.esotericsoftware.minlog.Log
import com.github.ajalt.timberkt.Timber
import io.paperdb.Paper
import org.koin.android.ext.android.startKoin
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

class App : Application() {

    private val viewModelsModule = module {
        viewModel { LocationsViewModel(get()) }
    }
    private val repositoriesModule = module {
        single { LocationRepository() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(repositoriesModule, viewModelsModule))
        Paper.init(this)
        initLogging()

        // TODO testing
//        with(Paper.book("locations")){
//            destroy()
//
//            val l1 = Location("Location 1", 21, 45)
//            val l2 = Location("Location 2", 74, 12)
//            val l3 = Location("Location 3", 67, 54)
//            write(l1.id, l1)
//            write(l2.id, l2)
//            write(l3.id, l3)
//        }
    }

    private fun initLogging(){
        if (BuildConfig.RELEASE) {
            Paper.setLogLevel(Log.LEVEL_NONE)
        } else {
            Paper.setLogLevel(Log.LEVEL_DEBUG)
            Timber.plant(Timber.DebugTree())
        }
    }

}