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

class PlayerConfig : AppCompatActivity() {

    private lateinit var toggleButton: ToggleButton
    private lateinit var textName1: EditText
    private lateinit var textName2: EditText
    private lateinit var settingButton: ImageButton
    private lateinit var gameStartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_config)
        init()
    }

    private fun init() {
        toggleButton = findViewById(R.id.toggleButton)
        textName1 = findViewById(R.id.textName1)
        textName2 = findViewById(R.id.textName2)
        settingButton = findViewById(R.id.SettingsButton)
        gameStartButton = findViewById(R.id.gameStartButton)
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
        DataStore.playerName1 = textName1.text.toString()
        DataStore.playerName2 = textName2.text.toString()
        onlineOfflinechanger()
        if (DataStore.gameMode && DataStore.playerName1 != "") {
            DataStore.logQuestionAnswers()
            val intent = Intent(this, OnlineConnection::class.java)
            startActivity(intent)
        } else if (!DataStore.gameMode && (DataStore.playerName1 != "") && (DataStore.playerName2 != "")) {
            DataStore.logQuestionAnswers()
            val intent2 = Intent(this, Veranstaltungswahl::class.java)
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
        } else if (toggleButton.text.toString() == "Online") {
            textName2.visibility = View.INVISIBLE
            DataStore.gameMode = true
        }
    }
}