package com.example.projekt

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {


    lateinit var startButton: Button
    lateinit var test: String
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }


    private fun init() {
        startButton = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
            println("&&$test &&2")
        }
        logQuestionAnswers()
        test()

    }

    private fun addAnswerToDb(answer: MutableMap<String, Any>) {
        db.collection("Answer")
            .add(answer)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    fun logQuestionAnswers() {
        val docRef = db.collection("Question")
        docRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    DataStore.questions.add(
                        Question(
                            text = document.data["text"].toString(),
                            ID = document.data["ID"].toString()
                        )
                    )
                    Log.d(TAG, "${document.id}=>${document.data["text"]}")
                    println("${DataStore.questions} ")
                }
            }
        val docAns = db.collection("Answer")
        docAns.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    DataStore.answers.add(
                        Answer(
                            text = document.data["Text"].toString(),
                            ID = document.data["ID"].toString(),
                            _QuestionID = document.data["_QuestionId"].toString()
                        )
                    )
                    println(DataStore.answers)
                }
            }
    }

    fun createAnswers() {

        val texts = listOf("30 Jahre", "60 Jahre", "200 Jahre", "400 Jahre")

        var counter = 0

        repeat(texts.size) {

            val answer: MutableMap<String, Any> = hashMapOf(
                "ID" to "$counter",
                "Text" to texts[it],
                "_QuestionId" to "Gx80PliwZ5SgxAg71N85"
            )
            addAnswerToDb(answer)
            counter++
        }

        val texts2 = listOf("300", "200", "900", "400")

        repeat(texts2.size) {

            val answer: MutableMap<String, Any> = hashMapOf(
                "ID" to "$counter",
                "Text" to texts2[it],
                "_QuestionId" to "HVNAmGic19IL9XOvr38Q"
            )
            addAnswerToDb(answer)
            counter++
        }

        val texts3 = listOf("Irgendeins", "Garkeins", "Jedes", "Jedes Zweite")

        repeat(texts3.size) {

            val answer: MutableMap<String, Any> = hashMapOf(
                "ID" to "$counter",
                "Text" to texts3[it],
                "_QuestionId" to "Z6gG8U231q8q57QgiV0X"
            )
            addAnswerToDb(answer)
            counter++
        }

        val texts4 = listOf("3.1415926535", "3", "4", "2")

        repeat(texts3.size) {

            val answer: MutableMap<String, Any> = hashMapOf(
                "ID" to "$counter",
                "Text" to texts4[it],
                "_QuestionId" to "WXH0mfjh7SOYi4MNuit0"
            )
            addAnswerToDb(answer)
            counter++
        }
    }

    fun test() {
        test = "1"
        val docRef = db.collection("Answer").document("08m79j4cYbfKpgiNhYyE")
        docRef.get()
            .addOnSuccessListener { document ->
                test = document.getString("Text").toString()

            }
    }

}
