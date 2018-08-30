package cafe.adriel.greenhell.view.main.locations

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.SaveLocationEvent
import cafe.adriel.greenhell.getClassTag
import cafe.adriel.greenhell.model.Location
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_location_editor.*
import kotlinx.android.synthetic.main.dialog_location_editor.view.*
import org.greenrobot.eventbus.EventBus

class LocationEditorDialog : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_LOCATION = "location"

        fun show(fragmentManager: FragmentManager, location: Location? = null) {
            LocationEditorDialog().run {
                arguments = Bundle().apply {
                    putParcelable(ARG_LOCATION, location)
                }
                show(fragmentManager, getClassTag<LocationEditorDialog>())
            }
        }
    }

    private var location: Location? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.run {
            location = getParcelable(ARG_LOCATION)
        }
        isCancelable = false
        return inflater.inflate(R.layout.dialog_location_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view){
            location?.let {
                vName.setText(it.name)
                vWestPosition.setSelectedLocation(it.westPosition)
                vSouthPosition.setSelectedLocation(it.southPosition)
            }
            vName.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    isValid()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            })
            vClose.setOnClickListener { dismiss() }
            vSave.setOnClickListener { saveLocation() }
        }
    }

    private fun saveLocation(){
        if(!isValid()) return

        val name = vName.text.toString()
        val westPosition = vWestPosition.getSelectedPosition()
        val southPosition = vSouthPosition.getSelectedPosition()
        val newLocation = location?.apply {
            this.name = name
            this.westPosition = westPosition
            this.southPosition = southPosition
        } ?: Location(name, westPosition, southPosition)

        EventBus.getDefault().post(SaveLocationEvent(newLocation))
        dismiss()
    }

    private fun isValid() = when {
        vName.text.isNullOrBlank() -> {
            vNameLayout.error = getString(R.string.lets_give_name_location)
            false
        }
        vName.text!!.length > vNameLayout.counterMaxLength -> {
            vNameLayout.error = getString(R.string.name_too_long)
            false
        }
        else -> {
            vNameLayout.error = ""
            true
        }
    }

}