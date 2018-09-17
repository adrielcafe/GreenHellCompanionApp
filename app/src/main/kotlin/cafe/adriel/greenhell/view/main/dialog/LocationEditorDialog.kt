package cafe.adriel.greenhell.view.main.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.SaveLocationEvent
import cafe.adriel.greenhell.classTag
import cafe.adriel.greenhell.model.Location
import cafe.adriel.greenhell.model.LocationCategory
import cafe.adriel.greenhell.postEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tinsuke.icekick.extension.freezeInstanceState
import com.tinsuke.icekick.extension.parcelState
import com.tinsuke.icekick.extension.unfreezeInstanceState
import kotlinx.android.synthetic.main.dialog_location_editor.*
import kotlinx.android.synthetic.main.dialog_location_editor.view.*

class LocationEditorDialog : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_LOCATION = "location"

        fun show(fragmentManager: FragmentManager, location: Location? = null) {
            LocationEditorDialog().run {
                arguments = Bundle().apply {
                    putParcelable(ARG_LOCATION, location)
                }
                show(fragmentManager, classTag<LocationEditorDialog>())
            }
        }
    }

    private var location: Location? by parcelState()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        arguments?.run {
            location = getParcelable(ARG_LOCATION)
        }
        return inflater.inflate(R.layout.dialog_location_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view){
            location?.let {
                vName.setText(it.name)
                vWestPosition.setSelectedPosition(it.westPosition)
                vSouthPosition.setSelectedPosition(it.southPosition)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        freezeInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        unfreezeInstanceState(savedInstanceState)
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
                this.category = LocationCategory.MY_LOCATIONS
            } ?: Location(name, westPosition, southPosition, LocationCategory.MY_LOCATIONS)

        postEvent(SaveLocationEvent(newLocation))
        dismiss()
    }

    private fun isValid() = when {
        vName.text.isNullOrBlank() -> {
            vNameLayout.error = getString(R.string.give_name_your_location)
            false
        }
        vName.text?.length ?: 0 > vNameLayout.counterMaxLength -> {
            vNameLayout.error = getString(R.string.name_too_long)
            false
        }
        else -> {
            vNameLayout.error = ""
            true
        }
    }

}