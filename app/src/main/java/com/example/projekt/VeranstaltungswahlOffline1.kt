package com.example.projekt

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VeranstaltungswahlOffline1 : AppCompatActivity() {
    var theater1: Boolean? = false
    var oper1: Boolean? = false
    var ausstellung1: Boolean? = false
    var lesung1: Boolean? = false
    var performance1: Boolean? = false
    var konzert1: Boolean? = false
    lateinit var continueVeranstaltung: Button
    lateinit var checkTheater: CheckBox
    lateinit var checkLesung: CheckBox
    lateinit var checkKonzert: CheckBox
    lateinit var checkPerformance: CheckBox
    lateinit var checkOper: CheckBox
    lateinit var checkAusstellung: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.veranstaltungswahloffline1)

        init()
    }

    fun init() {
        val textViewSpieler1 = findViewById<TextView>(R.id.textViewSpieler1online)
        continueVeranstaltung = findViewById<Button>(R.id.continueVeranstaltung)
        checkTheater = findViewById<CheckBox>(R.id.checkTheater)
        checkLesung = findViewById<CheckBox>(R.id.checkLesung)
        checkKonzert = findViewById<CheckBox>(R.id.checkKonzert)
        checkPerformance = findViewById<CheckBox>(R.id.checkPerformance)
        checkOper = findViewById<CheckBox>(R.id.checkOper)
        checkAusstellung = findViewById<CheckBox>(R.id.checkAusstellung)
        textViewSpieler1.text = DataStore.playerName1
        checkAusstellung.setOnClickListener {
            checkboxCheck()
        }
        checkLesung.setOnClickListener {
            checkboxCheck()
        }
        checkKonzert.setOnClickListener {
            checkboxCheck()
        }
        checkOper.setOnClickListener {
            checkboxCheck()
        }
        checkPerformance.setOnClickListener {
            checkboxCheck()
        }
        checkTheater.setOnClickListener {
            checkboxCheck()
        }
        continueVeranstaltung.setOnClickListener() {
            popoutWhenNoInput()
        }
    }

    fun checkboxCheck() {
        ausstellung1 = checkAusstellung.isChecked
        lesung1 = checkLesung.isChecked
        konzert1 = checkKonzert.isChecked
        oper1 = checkOper.isChecked
        performance1 = checkPerformance.isChecked
        theater1 = checkTheater.isChecked
    }

    fun popoutWhenNoInput() {
        if (ausstellung1 == false && lesung1 == false && konzert1 == false && oper1 == false && performance1 == false && theater1 == false) {
            val popoutNoTopic =
                layoutInflater.inflate(R.layout.popout_kein_thema_gewaehlt, null)
            val popout = Dialog(this)
            popout.setContentView(popoutNoTopic)
            popout.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popout.window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
            popout.window?.attributes?.height = WindowManager.LayoutParams.MATCH_PARENT
            popout.show()
            val popoutButton = popoutNoTopic.findViewById<Button>(R.id.popoutButton)
            popoutButton.setOnClickListener() {
                popout.dismiss()
            }
        } else {
            val intent = Intent(this, VeranstaltungswahlOffline2::class.java).also {
                it.putExtra("Theater", theater1)
                it.putExtra("Oper", oper1)
                it.putExtra("Performance", performance1)
                it.putExtra("Lesung", lesung1)
                it.putExtra("Ausstellung", ausstellung1)
                it.putExtra("Konzert", konzert1)
                startActivity(it)
            }
            startActivity(intent)
        }
    }
}