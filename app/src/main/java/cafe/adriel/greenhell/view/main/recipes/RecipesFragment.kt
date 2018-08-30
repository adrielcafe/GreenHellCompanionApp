package cafe.adriel.greenhell.view.main.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cafe.adriel.greenhell.R

class RecipesFragment : Fragment() {

    companion object {
        fun newInstance() = RecipesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

}