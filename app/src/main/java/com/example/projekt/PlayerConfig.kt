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
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity


class PlayerConfig : AppCompatActivity() {

    private lateinit var toggleButton: ToggleButton
    private lateinit var textName1: EditText
    private lateinit var textName2: EditText
    private lateinit var gameStartButton: Button
    private lateinit var gespeicherteSpiele: ImageButton
    private lateinit var seekBar : SeekBar
    private lateinit var textViewQuestionCount : TextView
    private var chosenPopout : MutableList <String> = mutableListOf() // erste Stelle Titel zweite Stelle Erklärung


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_config)
        init()
    }

    private fun init() {
        toggleButton = findViewById(R.id.toggleButton)
        textName1 = findViewById(R.id.textName1)
        textName2 = findViewById(R.id.textName2)
        gameStartButton = findViewById(R.id.gameStartButton)
        gespeicherteSpiele = findViewById(R.id.gespeicherteSpiele)
        seekBar = findViewById(R.id.seekBar)
        textViewQuestionCount = findViewById(R.id.textViewQuestionCount)
        var questionCount = seekBar.progress +1
        textViewQuestionCount.text = getString(R.string.fragen_ausgewahlt , questionCount.toString())
        chosenPopout = mutableListOf(getString(R.string.no_name), getString(R.string.no_name_explained))
        toggleButton.setOnClickListener {
            onlineOfflinechanger()
        }
        gameStartButton.setOnClickListener {
            goToVeranstaltungswahl()
        }
        gespeicherteSpiele.setOnClickListener {
            DataStore.reconnect = true
            val intent = Intent(this , OnlineConnection::class.java)
            startActivity(intent)
        }
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onProgressChanged(seekBar: SeekBar , progress: Int , fromUser: Boolean) {
                questionCount = seekBar.progress +1
                textViewQuestionCount.text =
                    getString(R.string.fragen_ausgewahlt , questionCount.toString())
            }
        })
    }

    private fun goToVeranstaltungswahl() {
        DataStore.playerName1 = textName1.text.toString()
        DataStore.playerName2 = textName2.text.toString()
        DataStore.questionCount = seekBar.progress +1
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
            popout()
        }
    }

    private fun popout(){
        val popout = Dialog(this)
        val popoutNoInput = layoutInflater.inflate(R.layout.popout_template, null)
        val popoutText = popoutNoInput.findViewById<TextView>(R.id.popoutText)
        val popoutTitle = popoutNoInput.findViewById<TextView>(R.id.popoutTitle)
        popoutText.text = chosenPopout[1]
        popoutTitle.text = chosenPopout[0]
        popout.setContentView(popoutNoInput)
        popout.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popout.window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        popout.window?.attributes?.height = WindowManager.LayoutParams.MATCH_PARENT
        popout.show()
        val popoutButton = popoutNoInput.findViewById<Button>(R.id.popoutButton)
        popoutButton.setOnClickListener {
            popout.dismiss()
        }
    }


    private fun onlineOfflinechanger() {
        if (toggleButton.text.toString() == "Offline") {
            DataStore.gameMode = false
            textName2.visibility = View.VISIBLE
        } else if (toggleButton.text.toString() == "Online") {
            textName2.visibility = View.INVISIBLE
            DataStore.gameMode = true
        }
    }
}