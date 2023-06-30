package com.example.projekt

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    lateinit var toggleButton: Button
    lateinit var textName1: EditText
    lateinit var textName2: EditText
    lateinit var settingButton: ImageButton
    lateinit var gameStartButton: Button
    lateinit var spielerName1: String
    lateinit var spielerName2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        init()
    }

    private fun init() {
        toggleButton = findViewById<ToggleButton>(R.id.toggleButton)
        textName1 = findViewById<EditText>(R.id.textName1)
        textName2 = findViewById<EditText>(R.id.textName2)
        settingButton = findViewById<ImageButton>(R.id.SettingsButton)
        gameStartButton = findViewById<Button>(R.id.gameStartButton)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        toggleButton.setOnClickListener() {
            onlineOfflinechanger()
        }
        gameStartButton.setOnClickListener {
            goToVeranstaltungswahl()
        }
    }

    private fun goToVeranstaltungswahl() {
        spielerName1 = textName1.text.toString()
        spielerName2 = textName2.text.toString()
        DataStore.playerName1 = spielerName1
        DataStore.playerName2 = spielerName2
        onlineOfflinechanger()
        if (DataStore.gameMode && DataStore.playerName1 != "") {
            val intent = Intent(this, OnlineVerbindung::class.java)
            startActivity(intent)
        } else if (!DataStore.gameMode && (DataStore.playerName1 != "") && (DataStore.playerName2 != "")) {
            val intent2 = Intent(this, VeranstaltungswahlOffline1::class.java)
            startActivity(intent2)
        } else {
            popoutNoName()
        }
    }

    private fun popoutNoName() {
        val popoutNoName =
            layoutInflater.inflate(R.layout.popout_kein_name_eingetippt, null)
        val popout = Dialog(this)
        popout.setContentView(popoutNoName)
        popout.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popout.window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        popout.window?.attributes?.height = WindowManager.LayoutParams.MATCH_PARENT
        popout.show()
        val popoutButton = popoutNoName.findViewById<Button>(R.id.popoutButton)
        popoutButton.setOnClickListener {
            popout.dismiss()
        }
    }

    fun onlineOfflinechanger() {
        if (toggleButton.text.toString() == "Offline") {
            DataStore.gameMode = false
            textName2.visibility = View.VISIBLE
            println("Visible")
        } else if (toggleButton.text.toString() == "Online") {
            textName2.visibility = View.INVISIBLE
            DataStore.gameMode = true
        }
    }
}