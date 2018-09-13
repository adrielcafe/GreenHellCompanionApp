package cafe.adriel.greenhell.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import cafe.adriel.greenhell.*
import cafe.adriel.greenhell.view.main.about.AboutDialog
import cafe.adriel.greenhell.view.main.crafting.CraftingFragment
import cafe.adriel.greenhell.view.main.donate.DonateDialog
import cafe.adriel.greenhell.view.main.locations.LocationsFragment
import cafe.adriel.greenhell.view.main.map.MapFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.kobakei.ratethisapp.RateThisApp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(vToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if(BuildConfig.RELEASE) {
            RateThisApp.onCreate(this)
            RateThisApp.showRateDialogIfNeeded(this)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val adapter = SectionsPagerAdapter(supportFragmentManager)
        vContent.adapter = adapter
        vContent.offscreenPageLimit = adapter.count

        val drawerToggle = ActionBarDrawerToggle(this, vDrawer, vToolbar, R.string.open_menu, R.string.close_menu)
        vDrawer.addDrawerListener(drawerToggle)
        vDrawerNav.setNavigationItemSelectedListener(this)
        drawerToggle.syncState()

        vBottomNav.setOnNavigationItemSelectedListener { onNavItemSelected(it.itemId) }
        vAdd.setOnClickListener { onAddClicked() }

        with(viewModel) {
            getAppUpdateAvailable().observe(this@MainActivity, Observer { newVersion ->
                if (newVersion)
                    showUpdateAppDialog()
            })
            getPurchaseCompleted().observe(this@MainActivity, Observer { success ->
                if (success)
                    Snackbar.make(vRoot, R.string.thanks_for_support, Snackbar.LENGTH_LONG).show()
            })
            getBillingSupported().observe(this@MainActivity, Observer { supported ->
                vDrawerNav.menu
                    .findItem(R.id.nav_donate)
                    .isVisible = supported
            })
        }
    }

    override fun onResume() {
        super.onResume()
        onNavItemSelected(vBottomNav.selectedItemId)
    }

    override fun onBackPressed() {
        if (vDrawer.isDrawerOpen(GravityCompat.START)) {
            vDrawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!viewModel.verifyDonation(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // Drawer Nav listener
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_about -> launch(UI) {
                delay(300)
                AboutDialog.show(this@MainActivity)
            }
            R.id.nav_donate -> launch(UI) {
                delay(300)
                DonateDialog.show(this@MainActivity)
            }
            R.id.nav_share -> shareApp()
            R.id.nav_rate -> rateApp()
        }
        vDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    // Bottom Nav listener
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
            0 -> postEvent(AddLocationEvent())
        }
    }

    private fun showUpdateAppDialog(){
        AlertDialog.Builder(this)
            .setTitle(R.string.update_available)
            .setMessage(R.string.new_version_available_update_now)
            .setNegativeButton(R.string.no, null)
            .setPositiveButton(R.string.yes) { _, _ ->
                showAppInPlayStore()
            }
            .show()
    }

    private fun shareApp(){
        "${getString(R.string.you_should_try)}\n${App.PLAY_STORE_URL}".share(this)
        Analytics.logShareApp()
    }

    private fun rateApp(){
        showAppInPlayStore()
        Analytics.logRateApp()
    }

    private fun showAppInPlayStore(){
        try {
            Uri.parse(App.MARKET_URL).open(this)
            Analytics.logOpenUrl(App.MARKET_URL)
        } catch (e: Exception){
            Uri.parse(App.PLAY_STORE_URL).open(this)
            Analytics.logOpenUrl(App.PLAY_STORE_URL)
        }
    }

    @Subscribe
    fun onEvent(event: DonateEvent){
        viewModel.donate(this, event.sku)
        Analytics.logDonate(event.sku)
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