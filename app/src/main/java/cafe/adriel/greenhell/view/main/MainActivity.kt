package cafe.adriel.greenhell.view.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import cafe.adriel.greenhell.AddLocationEvent
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.view.main.locations.LocationsFragment
import cafe.adriel.greenhell.view.main.map.MapFragment
import cafe.adriel.greenhell.view.main.recipes.RecipesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private lateinit var adapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(vToolbar)

        adapter = SectionsPagerAdapter(supportFragmentManager)
        vContent.adapter = adapter
        vContent.addOnPageChangeListener(this)
        vBottomNav.setOnNavigationItemSelectedListener(this)
        vAdd.setOnClickListener {
            when(vContent.currentItem){
                0 -> EventBus.getDefault().post(AddLocationEvent())
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        vAdd.hide()
        vContent.currentItem = when(item.itemId) {
            R.id.nav_locations -> {
                vAdd.show()
                0
            }
            R.id.nav_recipes -> 1
            R.id.nav_map -> 2
            else -> -1
        }
        return true
    }

    override fun onPageSelected(position: Int) {
        vAdd.hide()
        vBottomNav.selectedItemId = when(position){
            0 -> {
                vAdd.show()
                R.id.nav_locations
            }
            1 -> R.id.nav_recipes
            2 -> R.id.nav_map
            else -> -1
        }
    }

    override fun onPageScrollStateChanged(state: Int) { }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val sections by lazy {
            listOf(LocationsFragment.newInstance(), RecipesFragment.newInstance(), MapFragment.newInstance())
        }

        override fun getItem(position: Int) = sections[position]

        override fun getCount() = sections.size

    }
}