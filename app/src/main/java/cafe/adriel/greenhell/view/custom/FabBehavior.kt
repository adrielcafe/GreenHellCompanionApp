package cafe.adriel.greenhell.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import cafe.adriel.greenhell.px
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class FabBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<FloatingActionButton>(context, attrs) {

    private val margin = 20.px

    override fun layoutDependsOn(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
        if(dependency is BottomNavigationView){
            (child.layoutParams as CoordinatorLayout.LayoutParams)
                .setMargins(0, 0, margin, dependency.measuredHeight + margin)
        }
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
        val translationY = Math.min(0F, dependency.translationY - dependency.height)
        child.translationY = translationY
        return true
    }

}