package com.example.projekt

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object DataStore {
    var questionCount = 2
    lateinit var answer: MutableMap<String, Any>
    var gameMode: Boolean = true // True = online
    var playerName1: String = ""
    var playerName2: String = ""
    var topic: String = ""
    var stage = 1
    var currentPoints1 = 0
    var currentPoints2 = 0
    var questionsPicked = mutableListOf<Int>()
    val questions = mutableListOf<Question>()
    val answers = mutableListOf<Answer>()

    var gameID = "Q1q5oJnxhwoIBmqxp6y2"
    var player1OR2 = true // true = 1 = Spieler der das Spiel erstellt
    var gameData: MutableMap<String, Any> = hashMapOf()
    var reconnect = false
    var choosenAvatar1 = 1
    var choosenAvatar2 = 1
    val images = listOf(R.drawable.person_blue_no_background, R.drawable.person_green_no_background, R.drawable.person_red_no_background, R.drawable.person_turkis_no_background, R.drawable.person_yellow_no_background)



    private var db = FirebaseFirestore.getInstance()

    fun logQuestionAnswers() {
        val docRef = db.collection("Question") // bekommt path zur richtigen collection
        docRef.get() // lädt diese collection herunter
            .addOnFailureListener {
                println("wir haben einen fail beim Herunterladen")
            }
            //Prüft auf success/Fail
            .addOnSuccessListener {
                    result ->//führt für alle heruntergeladenen Werte folgendes aus
                for (document in result) {
                    questions.add(// fügt die Werte zur Variable questions hinzu
                        Question(
                            text = document.data["text"].toString(),
                            ID = document.data["ID"].toString()
                        )
                    )
                }
            }
        val docAns = db.collection("Answer")// bekommt path zur richtigen collection
        docAns.get()// lädt diese collection herunter
            .addOnSuccessListener { result ->
                for (document in result) {
                    answers.add(// fügt die Werte zur Variable answers hinzu
                        Answer(
                            ID = document.data["ID"].toString(),
                            text = document.data["Text"].toString(),
                            _QuestionID = document.data["_QuestionId"].toString(),
                            correct = document.data["correct"].toString()
                        )
                    )
                }
            }
    }

    fun createGame() {
        db.collection("Games")
            .add(gameData)
            .addOnSuccessListener { documentReference ->
                gameID = documentReference.id
            }
    }

    fun updateAnswerInDB(){
        db.collection("Games").document(gameID)
            .update(answer)
            .addOnFailureListener {
                Log.e("Database", "Failed to update the database.")
            }
    }
}



