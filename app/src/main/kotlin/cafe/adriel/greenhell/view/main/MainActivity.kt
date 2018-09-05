package cafe.adriel.greenhell.view.main

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import cafe.adriel.greenhell.AddLocationEvent
import cafe.adriel.greenhell.App
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.open
import cafe.adriel.greenhell.view.main.crafting.CraftingFragment
import cafe.adriel.greenhell.view.main.locations.LocationsFragment
import cafe.adriel.greenhell.view.main.map.MapFragment
import com.kobakei.ratethisapp.RateThisApp
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(vToolbar)

        RateThisApp.onCreate(this)
        RateThisApp.showRateDialogIfNeeded(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val adapter = SectionsPagerAdapter(supportFragmentManager)
        vContent.adapter = adapter
        vContent.offscreenPageLimit = adapter.count
        vBottomNav.setOnNavigationItemSelectedListener { onNavItemSelected(it.itemId) }
        vAdd.setOnClickListener { onAddClicked() }

        viewModel.appUpdateAvailable.observe(this, Observer { newVersion ->
            if(newVersion) showUpdateAppDialog()
        })
    }

    override fun onResume() {
        super.onResume()
        onNavItemSelected(vBottomNav.selectedItemId)
    }

    private fun onNavItemSelected(itemId: Int): Boolean {
        vAdd.hide()
        vContent.currentItem = when(itemId) {
            R.id.nav_locations -> {
                vAdd.show()
                0
            }
            R.id.nav_crafting -> 1
            R.id.nav_map -> 2
            else -> -1
        }
        return true
    }

    private fun onAddClicked(){
        when(vContent.currentItem){
            0 -> EventBus.getDefault().post(AddLocationEvent())
        }
    }

    private fun showUpdateAppDialog(){
        AlertDialog.Builder(this)
            .setTitle(R.string.update_available)
            .setMessage(R.string.new_version_available_update_now)
            .setNegativeButton(R.string.no, null)
            .setPositiveButton(R.string.yes) { _, _ ->
                try {
                    App.MARKET_URL.open(this)
                } catch (e: Exception){
                    App.PLAY_STORE_URL.open(this)
                }
            }
            .show()
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val sections by lazy { listOf(
            LocationsFragment.newInstance(),
            CraftingFragment.newInstance(),
            MapFragment.newInstance())
        }

        override fun getItem(position: Int) = sections[position]

        override fun getCount() = sections.size

    }
}