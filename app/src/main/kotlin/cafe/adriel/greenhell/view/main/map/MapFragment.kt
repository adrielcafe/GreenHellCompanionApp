package cafe.adriel.greenhell.view.main.map

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cafe.adriel.greenhell.Analytics
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.view.fullscreenimage.FullScreenImageActivity
import kotlinx.android.synthetic.main.fragment_map.view.*

class MapFragment : Fragment() {

    companion object {
        fun newInstance() = MapFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view){
            vCredits.movementMethod = LinkMovementMethod.getInstance()
            vFullscreen.setOnClickListener {
                Analytics.logExpandMap()
                FullScreenImageActivity.start(context)
            }
        }
    }

}