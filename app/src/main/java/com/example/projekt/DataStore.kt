package com.example.projekt

import com.example.projekt.DataStore.db
import com.google.firebase.firestore.FirebaseFirestore

object DataStore {
    lateinit var answer: MutableMap<String, Any>
    var gameMode: Boolean = true
    var playerName1: String = ""
    var playerName2: String = ""
    var topic: String = ""
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
    var gameData: MutableMap<String, Any> = hashMapOf()


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

    fun getDatastoreData(){
        val docRef = db.collection("Games").document(gameID)
        docRef.addSnapshotListener{ snapshot, _ ->
            gameID = snapshot?.get("gameID") as? String ?: ""

        }
    }
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
                    //Biete einen Stelle zum Prüfen
                    //Log.d(ContentValues.TAG, "${document.id}=>${document.data["text"]}")
                    println("das sind die questions das wurde ausgeführt$questions ")
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
                answersInApp += 1
            }

    }

    fun createGame() {

        db.collection("Games")
            .add(gameData)
            .addOnSuccessListener { documentReference ->
                gameID = documentReference.id
            }
    }
}
    fun updateAnswerInDB(){
        db.collection("Games").document(DataStore.gameID)
            .update(DataStore.answer)
    }


