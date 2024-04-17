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
    private var playerToCheck = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.veranstaltungswahloffline)

        init()
    }

    fun init() {
        val textViewSpieler1 = findViewById<TextView>(R.id.textViewSpieler1online)
        continueVeranstaltung = findViewById(R.id.continueVeranstaltung)
        checkTheater = findViewById(R.id.checkTheater)
        checkLesung = findViewById(R.id.checkLesung)
        checkKonzert = findViewById(R.id.checkKonzert)
        checkPerformance = findViewById(R.id.checkPerformance)
        checkOper = findViewById(R.id.checkOper)
        checkAusstellung = findViewById(R.id.checkAusstellung)
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
        if (playerToCheck == 0){
            ausstellung1 = checkAusstellung.isChecked
            lesung1 = checkLesung.isChecked
            konzert1 = checkKonzert.isChecked
            oper1 = checkOper.isChecked
            performance1 = checkPerformance.isChecked
            theater1 = checkTheater.isChecked
        }else{

            ausstellung2 = checkAusstellung.isChecked
            lesung2 = checkLesung.isChecked
            konzert2 = checkKonzert.isChecked
            oper2 = checkOper.isChecked
            performance2 = checkPerformance.isChecked
            theater2 = checkTheater.isChecked
        }
    }

    fun popoutWhenNoInput() {
        if (playerToCheck == 0 && ausstellung1 == false && lesung1 == false && konzert1 == false && oper1 == false && performance1 == false && theater1 == false) {
            popout()
        }else if (playerToCheck == 0){
            checkAusstellung.isChecked = false
            checkLesung.isChecked= false
            checkKonzert.isChecked= false
            checkOper.isChecked= false
            checkPerformance.isChecked= false
            checkTheater.isChecked= false
            playerToCheck = 1
        }else if(ausstellung2 == false && lesung2 == false && konzert2 == false && oper2 == false && performance2 == false && theater2 == false){
            popout()
        }else {
            checkIfThemaMatch()
            val intent2 = Intent(this, ThemaErgebnis::class.java)
            startActivity(intent2)
        }
    }

    private fun popout() {
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
            popout
        }
    }

    private fun giveTopic() {
        if (themaSet.size == 0) {
            if (theater1 == true || theater2 == true) {
                themaSet.add("Theater")
            }
            if (oper1 == true || oper2 == true) {
                themaSet.add("Oper")
            }
            if (ausstellung1 == true || ausstellung2 == true) {
                themaSet.add("Ausstellung")
            }
            if (lesung1 == true || lesung2 == true) {
                themaSet.add("Lesung")
            }
            if (konzert1 == true || konzert2 == true) {
                themaSet.add("Konzert")
            }
            if (performance1 == true || performance2 == true) {
                themaSet.add("Performance")
            }
        }
        val random: Int = (0 until (themaSet.size)).random()
        DataStore.topic = themaSet[random]
    }

    private fun checkIfThemaMatch() {
        if (theater1 == true && theater2 == true) {
            themaSet.add("Theater")
        }
        if (oper1 == true && oper2 == true) {
            themaSet.add("Oper")
        }
        if (ausstellung1 == true && ausstellung2 == true) {
            themaSet.add("Ausstellung")
        }
        if (lesung1 == true && lesung2 == true) {
            themaSet.add("Lesung")
        }
        if (konzert1 == true && konzert2 == true) {
            themaSet.add("Konzert")
        }
        if (performance1 == true && performance2 == true) {
            themaSet.add("Performance")
        }
        giveTopic()
    }
}