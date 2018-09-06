package cafe.adriel.greenhell.view.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.inflate
import kotlinx.android.synthetic.main.view_location_picker.view.*

class LocationPickerView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs){

    companion object {
        private const val DIRECTION_WEST = 0
        private const val DIRECTION_SOUTH = 1
    }

    init {
        val styleAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.LocationPickerView, 0, 0)
        val direction = styleAttrs.getInt(R.styleable.LocationPickerView_lpvDirection, -1)
        styleAttrs.recycle()

        with(inflate(R.layout.view_location_picker)){
            vLocationPosition.typeface = ResourcesCompat.getFont(context, R.font.digital_7)
            when(direction){
                DIRECTION_WEST -> vLocationDirection.text = "'W"
                DIRECTION_SOUTH -> vLocationDirection.text = "'S"
                else -> vLocationDirection.text = ""
            }
        }
    }

    fun getSelectedPosition() = vLocationPosition.value

    fun setSelectedPosition(position: Int) {
        vLocationPosition.value = position
    }

}