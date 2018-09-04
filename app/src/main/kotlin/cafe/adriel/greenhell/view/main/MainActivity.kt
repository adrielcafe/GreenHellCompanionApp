package cafe.adriel.greenhell.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import cafe.adriel.greenhell.AddLocationEvent
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.view.main.crafting.CraftingFragment
import cafe.adriel.greenhell.view.main.locations.LocationsFragment
import cafe.adriel.greenhell.view.main.map.MapFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(vToolbar)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val adapter = SectionsPagerAdapter(supportFragmentManager)
        vContent.adapter = adapter
        vContent.offscreenPageLimit = adapter.count
        vBottomNav.setOnNavigationItemSelectedListener { onNavItemSelected(it.itemId) }
        vAdd.setOnClickListener { onAddClicked() }
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