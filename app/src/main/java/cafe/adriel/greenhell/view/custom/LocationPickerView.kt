package cafe.adriel.greenhell.view.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import cafe.adriel.greenhell.R
import kotlinx.android.synthetic.main.view_location_picker.view.*

class LocationPickerView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs){

    companion object {
        private const val LOCATION_WEST = 0
        private const val LOCATION_SOUTH = 1
    }

    private var direction: Int = -1

    init {
        val styleAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.LocationPickerView, 0, 0)
        try {
            direction = styleAttrs.getInt(R.styleable.LocationPickerView_lpvDirection, Color.BLACK)
        } finally {
            styleAttrs.recycle()
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_location_picker, this, true)
        with(view){
            vLocationPosition.typeface = ResourcesCompat.getFont(context, R.font.digital_7)
            when(direction){
                LOCATION_WEST -> vLocationDirection.text = "'W"
                LOCATION_SOUTH -> vLocationDirection.text = "'S"
                else -> vLocationDirection.text = ""
            }
        }
    }

    fun getSelectedPosition() = vLocationPosition.value

    fun setSelectedPosition(position: Int) {
        vLocationPosition.value = position
    }

}