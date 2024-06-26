package com.example.projekt

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {


    private lateinit var startButton: Button
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome)

        init()
    }


    fun init() {
        startButton = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            val intent = Intent(this, PlayerConfig::class.java)
            startActivity(intent)
        }
        //test()

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



    fun createAnswers() {

        val texts = listOf("30 Jahre", "60 Jahre", "200 Jahre", "400 Jahre")

        var counter = 0

        repeat(texts.size) {

            val answer: MutableMap<String, Any> = hashMapOf(
                "ID" to "$counter",
                "Text" to texts[it],
                "_QuestionId" to "Gx80PliwZ5SgxAg71N85",
                "correct" to "false"
            )
            addAnswerToDb(answer)
            counter++
        }

        val texts2 = listOf("300", "200", "900", "400")

        repeat(texts2.size) {

            val answer: MutableMap<String, Any> = hashMapOf(
                "ID" to "$counter",
                "Text" to texts2[it],
                "_QuestionId" to "HVNAmGic19IL9XOvr38Q",
                "correct" to "false"
            )
            addAnswerToDb(answer)
            counter++
        }

        val texts3 = listOf("Irgendeins", "Garkeins", "Jedes", "Jedes Zweite")

        repeat(texts3.size) {

            val answer: MutableMap<String, Any> = hashMapOf(
                "ID" to "$counter",
                "Text" to texts3[it],
                "_QuestionId" to "Z6gG8U231q8q57QgiV0X",
                "correct" to "false"
            )
            addAnswerToDb(answer)
            counter++
        }

        val texts4 = listOf("3.1415926535", "3", "4", "2")

        repeat(texts3.size) {

            val answer: MutableMap<String, Any> = hashMapOf(
                "ID" to "$counter",
                "Text" to texts4[it],
                "_QuestionId" to "WXH0mfjh7SOYi4MNuit0",
                "correct" to "false"
            )
            addAnswerToDb(answer)
            counter++
        }
    }
}
