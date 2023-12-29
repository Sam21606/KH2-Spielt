package com.example.projekt

import com.google.firebase.firestore.FirebaseFirestore

object DataStore {
    lateinit var answer: MutableMap<String, Any>
    var gameMode: Boolean = false
    var playerName1: String = ""
    var playerName2: String = ""
    var topic: String = ""
    var stage: Int = 1
    var rating1 = 0
    var rating2 = 0
    var currentPoints1 = 0
    var currentPoints2 = 0
    val questions: MutableList<Question> = mutableListOf()
    val questionsPicked = mutableListOf<Any>()
    val answers: MutableList<Answer> = mutableListOf()
    var gameID = ""
    var player1OR2 = true // true = 1 = Spieler der das Spiel erstellt


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
}
    fun updateAnswerInDB(){
        DataStore.db.collection("Games").document("${DataStore.gameID}")
            .update(DataStore.answer)
    }
