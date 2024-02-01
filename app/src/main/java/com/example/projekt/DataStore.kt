package com.example.projekt

import android.content.ContentValues
import android.util.Log
import com.example.projekt.DataStore.db
import com.google.firebase.firestore.FirebaseFirestore

object DataStore {
    lateinit var answer: MutableMap<String, Any>
    var gameMode: Boolean = true
    var playerName1: String = "asd"
    var playerName2: String = "123"
    var topic: String = "oper"
    var stage: Int = 2
    var rating1 = 0
    var rating2 = 0
    var currentPoints1 = 1
    var currentPoints2 = 1
    var questionsPicked = mutableListOf<Any>("2","123")
    val questions = mutableListOf<Question>()
    val answers = mutableListOf<Answer>()

    var gameID = "Y1of7QZJof5CkV7Bi5Uj"
    var player1OR2 = true // true = 1 = Spieler der das Spiel erstellt
    var answersInApp = 0


    var db = FirebaseFirestore.getInstance()
    fun addGameDataToFirestore() {
        if (player1OR2) {
            answer = hashMapOf(
                "playerName1" to playerName1,
                "topic" to topic,
                "currentPoints1" to "$currentPoints1",
                "questionsPicked" to "$questionsPicked",
            )
        } else {

            answer = hashMapOf(
                "playerName2" to "$playerName2",
                "topic" to "$topic",
                "currentPoints2" to "$currentPoints2",
                "questionsPicked" to "$questionsPicked",
            )
        }
        updateAnswerInDB()
    }
    fun logQuestionAnswers() {
        var db = FirebaseFirestore.getInstance()
        println("Ich wurde ausgeführt")
        val docRef = db.collection("Question")
        docRef.get()
            .addOnFailureListener {
                println("wir haben einen fail")
            }
            .addOnSuccessListener {
                    result ->
                for (document in result) {
                    questions.add(
                        Question(
                            text = document.data["text"].toString(),
                            ID = document.data["ID"].toString()
                        )
                    )
                    Log.d(ContentValues.TAG, "${document.id}=>${document.data["text"]}")
                    println("das sind die questions das wurde ausgeführt$questions ")
                    println("nächster Test")
                }
                println("Wir haben succeses")
            }
        println("irgendentwas ist weird")
        val docAns = db.collection("Answer")
        docAns.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    answers.add(
                        Answer(
                            ID = document.data["ID"].toString(),
                            text = document.data["Text"].toString(),
                            _QuestionID = document.data["_QuestionId"].toString(),
                            correct = document.data["correct"].toString()
                        )
                    )
                    println(answers)
                    println("nächster Test $answers")
                }
                answersInApp += 1
            }

    }
}
    fun updateAnswerInDB(){
        db.collection("Games").document("${DataStore.gameID}")
            .update(DataStore.answer)
    }


