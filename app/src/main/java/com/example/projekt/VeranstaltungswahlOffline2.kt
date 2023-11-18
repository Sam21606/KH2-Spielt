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

class VeranstaltungswahlOffline2 : AppCompatActivity() {
    var theater1: Boolean = false
    var oper1: Boolean = false
    var performance1: Boolean = false
    var lesung1: Boolean = false
    var konzert1: Boolean = false
    var ausstellung1: Boolean = false
    var theater2: Boolean? = false
    var oper2: Boolean? = false
    var ausstellung2: Boolean? = false
    var lesung2: Boolean? = false
    var performance2: Boolean? = false
    var konzert2: Boolean? = false
    private val themaSet: MutableList<String> = mutableListOf()
    lateinit var continueVeranstaltung: Button
    lateinit var checkTheater: CheckBox
    lateinit var checkLesung: CheckBox
    lateinit var checkKonzert: CheckBox
    lateinit var checkPerformance: CheckBox
    lateinit var checkOper: CheckBox
    lateinit var checkAusstellung: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.veranstaltungswahloffline2)

        init()
    }


    fun init() {
        readIntent()
        var textName2 = findViewById<TextView>(R.id.textViewSpieler1online)
        continueVeranstaltung = findViewById(R.id.continueVeranstaltung)
        checkTheater = findViewById(R.id.checkTheater)
        checkLesung = findViewById(R.id.checkLesung)
        checkKonzert = findViewById(R.id.checkKonzert)
        checkPerformance = findViewById(R.id.checkPerformance)
        checkOper = findViewById(R.id.checkOper)
        checkAusstellung = findViewById(R.id.checkAusstellung)
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
            showPopoutIfNotChoosed()
        }
        textName2.text = DataStore.playerName2
    }

    private fun showPopoutIfNotChoosed() {
        if (ausstellung2 == false && lesung2 == false && konzert2 == false && oper2 == false && performance2 == false && theater2 == false) {
            val popoutNoTopic = layoutInflater.inflate(R.layout.popout_kein_thema_gewaehlt, null)
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
            checkIfThemaMatch()
            giveTopic()
            println("2234235623237312724776465656346 234613461346134622${DataStore.topic}")
            val intent2 = Intent(this, ThemaErgebnis::class.java)
            startActivity(intent2)
        }
    }

    private fun giveTopic() {
        if (themaSet.size == 0) {
            if (theater1 || theater2 == true) {
                themaSet.add("Theater")
            }
            if (oper1 || oper2 == true) {
                themaSet.add("Oper")
            }
            if (ausstellung1 || ausstellung2 == true) {
                themaSet.add("Ausstellung")
            }
            if (lesung1 || lesung2 == true) {
                themaSet.add("Lesung")
            }
            if (konzert1 || konzert2 == true) {
                themaSet.add("Konzert")
            }
            if (performance1 || performance2 == true) {
                themaSet.add("Performance")
            }
        }
        val random: Int = (0 until (themaSet.size)).random()
        DataStore.topic = themaSet[random]
    }

    private fun checkIfThemaMatch() {
        if (theater1 && theater2 == true) {
            themaSet.add("Theater")
        }
        if (oper1 && oper2 == true) {
            themaSet.add("Oper")
        }
        if (ausstellung1 && ausstellung2 == true) {
            themaSet.add("Ausstellung")
        }
        if (lesung1 && lesung2 == true) {
            themaSet.add("Lesung")
        }
        if (konzert1 && konzert2 == true) {
            themaSet.add("Konzert")
        }
        if (performance1 && performance2 == true) {
            themaSet.add("Performance")
        }
    }

    private fun checkboxCheck() {
        ausstellung2 = checkAusstellung.isChecked
        lesung2 = checkLesung.isChecked
        konzert2 = checkKonzert.isChecked
        oper2 = checkOper.isChecked
        performance2 = checkPerformance.isChecked
        theater2 = checkTheater.isChecked
    }

    private fun readIntent() {
        theater1 = intent.getBooleanExtra("Theater", false)
        performance1 = intent.getBooleanExtra("Performance", false)
        lesung1 = intent.getBooleanExtra("Lesung", false)
        konzert1 = intent.getBooleanExtra("Konzert", false)
        ausstellung1 = intent.getBooleanExtra("Ausstellung", false)
        lesung1 = intent.getBooleanExtra("Performance", false)
    }
}
