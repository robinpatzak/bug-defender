package de.mow2.towerdefense.controller

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import de.mow2.towerdefense.R
import kotlinx.android.synthetic.main.popup_view.*
import kotlinx.android.synthetic.main.popup_view.view.*


class DialogFragment: DialogFragment() {
    private lateinit var popUpView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        popUpView = inflater.inflate(R.layout.popup_view, container, false)
        return popUpView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Test", tag.toString())

        //decide which fragment will be inflated
        when(tag.toString()) {
            "aboutDialog" -> {
                popupText.text = "Ich bin ein about Dialog"
            }
            "infoDialog" -> {
                popupText.text = "Ich bin ein Infodialog"
            }
            "settingsDialog" -> {
                popupText.visibility = View.GONE
                childFragmentManager
                    .beginTransaction()
                    .replace(R.id.popupFragmentContainer, SettingsFragment())
                    .commit()
            }
        }

/*        popUpView.musicOnOff.setOnClickListener {
            if(!musicOnOff.isChecked){
                SoundManager.pauseMusic()
            } else {
                SoundManager.resumeMusic()
            }
        }*/
        popUpView.buttonPopup.setOnClickListener{
            dismiss()
        }
    }
}