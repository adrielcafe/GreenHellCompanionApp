package cafe.adriel.greenhell.view.fullscreenimage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.greenhell.R
import kotlinx.android.synthetic.main.activity_fullscreen_image.*

class FullScreenImageActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) = context.startActivity(
            Intent(context, FullScreenImageActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_image)
        vClose.setOnClickListener { finish() }
    }

}