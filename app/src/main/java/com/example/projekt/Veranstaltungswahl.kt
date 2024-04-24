package com.example.projekt

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.properties.Delegates

class Veranstaltungswahl : AppCompatActivity() {
    private var random by Delegates.notNull<Int>()
    private var theater1 = false
    private var oper1 = false
    private var ausstellung1 = false
    private var lesung1 = false
    private var performance1 = false
    private var konzert1 = false
    private var theater2 = false
    private var oper2 = false
    private var ausstellung2 = false
    private var lesung2 = false
    private var performance2 = false
    private var konzert2 = false
    private val themaSet: MutableList<String> = mutableListOf()
    private lateinit var continueVeranstaltung: Button
    private lateinit var checkTheater: CheckBox
    private lateinit var checkLesung: CheckBox
    private lateinit var checkKonzert: CheckBox
    private lateinit var checkPerformance: CheckBox
    private lateinit var checkOper: CheckBox
    private lateinit var checkAusstellung: CheckBox
    private lateinit var textViewSpieler1online: TextView
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.veranstaltungswahl)

        init()
    }

    private fun setListener(vararg views: View) {
        views.forEach { it.setOnClickListener { checkboxCheck() } }
    }

    fun init() {
        continueVeranstaltung = findViewById(R.id.continueVeranstaltung)
        checkTheater = findViewById(R.id.checkTheater)
        checkLesung = findViewById(R.id.checkLesung)
        checkKonzert = findViewById(R.id.checkKonzert)
        checkPerformance = findViewById(R.id.checkPerformance)
        checkOper = findViewById(R.id.checkOper)
        checkAusstellung = findViewById(R.id.checkAusstellung)
        textViewSpieler1online = findViewById(R.id.textViewSpieler1online)

        setListener(
            checkAusstellung,
            checkLesung,
            checkKonzert,
            checkOper,
            checkPerformance,
            checkTheater
        )

        continueVeranstaltung.setOnClickListener {
            getTopicFromFirebase()
        }
        textViewSpieler1online.text = DataStore.playerName1
    }

    fun checkIfThemaMatch() {
        checkboxCheck()
        giveTopicToFirebase()
        checkIfThereIsTopicFromFirebase()

    }

    fun checkWhatTopicMatches() {
        if (theater1 && theater2) {
            themaSet.add("Theater")
        }
        if (oper1 && oper2) {
            themaSet.add("Oper")
        }
        if (ausstellung1 && ausstellung2) {
            themaSet.add("Ausstellung")
        }
        if (lesung1 && lesung2) {
            themaSet.add("Lesung")
        }
        if (konzert1 && konzert2) {
            themaSet.add("Konzert")
        }
        if (performance1 && performance2) {
            themaSet.add("Performance")
        }
        if (themaSet.isEmpty()) {
            ifNoMatchChooseRandom()
        } else {
            updateTopicInDS()
        }
    }

    private fun checkIfThereIsTopicFromFirebase() {
        if (DataStore.player1OR2) {
            if (!theater2 && !oper2 && !ausstellung2 && !lesung2 && !konzert2 && !performance2) {
                val intent = Intent(this, WartenAufMitspieler::class.java)
                startActivity(intent)
            } else {
                checkWhatTopicMatches()
            }
        } else {
            if (!theater1 && !oper1 && !ausstellung1 && !lesung1 && !konzert1 && !performance1) {
                val intent = Intent(this, WartenAufMitspieler::class.java)
                startActivity(intent)
            } else {
                checkWhatTopicMatches()
            }

        }
    }

    private fun giveTopicToFirebase() {
        if (DataStore.player1OR2) {
            DataStore.answer = hashMapOf(
                "theater1" to "$theater1",
                "oper1" to "$oper1",
                "ausstellung1" to "$ausstellung1",
                "lesung1" to "$lesung1",
                "konzert1" to "$konzert1",
                "performance1" to "$performance1",
            )

            DataStore.updateAnswerInDB()

        } else {
            DataStore.answer = hashMapOf(
                "theater2" to "$theater2",
                "oper2" to "$oper2",
                "ausstellung2" to "$ausstellung2",
                "lesung2" to "$lesung2",
                "konzert2" to "$konzert2",
                "performance2" to "$performance2",
            )
            DataStore.updateAnswerInDB()
        }
    }

    private fun getTopicFromFirebase() {
        if (DataStore.player1OR2) {
            val docRef = db.collection("Games").document(DataStore.gameID)
            docRef.get()
                .addOnSuccessListener { document ->
                    theater2 = document.getString("theater2").toBoolean()
                    oper2 = document.getString("oper2").toBoolean()
                    ausstellung2 = document.getString("ausstellung2").toBoolean()
                    lesung2 = document.getString("lesung2").toBoolean()
                    konzert2 = document.getString("konzert2").toBoolean()
                    performance2 = document.getString("performance2").toBoolean()
                    popoutWhenNoInputElseNewLayout()
                }
        } else {

            val docRef = db.collection("Games").document(DataStore.gameID)
            docRef.get()
                .addOnSuccessListener { document ->
                    theater1 = document.getString("theater1").toBoolean()
                    oper1 = document.getString("oper1").toBoolean()
                    ausstellung1 = document.getString("ausstellung1").toBoolean()
                    lesung1 = document.getString("lesung1").toBoolean()
                    konzert1 = document.getString("konzert1").toBoolean()
                    performance1 = document.getString("performance1").toBoolean()
                    popoutWhenNoInputElseNewLayout()
                }
        }
    }

    fun ifNoMatchChooseRandom() {
        if (themaSet.size == 0) {
            if (theater1 || theater2) {
                themaSet.add("Theater")
            }
            if (oper1 || oper2) {
                themaSet.add("Oper")
            }
            if (ausstellung1 || ausstellung2) {
                themaSet.add("Ausstellung")
            }
            if (lesung1 || lesung2) {
                themaSet.add("Lesung")
            }
            if (konzert1 || konzert2) {
                themaSet.add("Konzert")
            }
            if (performance1 || performance2) {
                themaSet.add("Performance")
            }
            updateTopicInDS()
        }


    }

    private fun updateTopicInDS(){
        random = (0 until (themaSet.size)).random()
        DataStore.topic = themaSet[random]
        selectQuestions()
        DataStore.answer = hashMapOf(
            "topic" to DataStore.topic,
            "questionsPicked" to DataStore.questionsPicked
        )
        DataStore.updateAnswerInDB()
        val intent = Intent(this, ThemaErgebnis::class.java)
        startActivity(intent)
    }

    private fun selectQuestions() {
        // Logik um Fragen nach Thema auszuwählen
        DataStore.questionsPicked = mutableListOf("test", 2)
    }

    fun checkboxCheck() {
        if (DataStore.player1OR2) {
            ausstellung1 = checkAusstellung.isChecked
            lesung1 = checkLesung.isChecked
            konzert1 = checkKonzert.isChecked
            oper1 = checkOper.isChecked
            performance1 = checkPerformance.isChecked
            theater1 = checkTheater.isChecked
        } else {

            ausstellung2 = checkAusstellung.isChecked
            lesung2 = checkLesung.isChecked
            konzert2 = checkKonzert.isChecked
            oper2 = checkOper.isChecked
            performance2 = checkPerformance.isChecked
            theater2 = checkTheater.isChecked
        }
    }

    private fun popoutWhenNoInputElseNewLayout() {
        if (DataStore.player1OR2) {
            if (!ausstellung1 && !lesung1 && !konzert1 && !oper1 && !performance1 && !theater1) {
                popout()
            } else {
                checkIfThemaMatch()
            }
        } else {
            if (!ausstellung2 && !lesung2 && !konzert2 && !oper2 && !performance2 && !theater2) {
                popout()
            } else {
                checkIfThemaMatch()
            }
        }
    }

    fun popout(){

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
    }
}

