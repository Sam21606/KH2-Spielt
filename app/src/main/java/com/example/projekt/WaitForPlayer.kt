package com.example.projekt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class WaitForPlayer : AppCompatActivity() {
    private var db = FirebaseFirestore.getInstance()
    private lateinit var listenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wait_for_player)

        init()
    }

    private fun init() {
        listenerRegistration = db.collection("Games").document(DataStore.gameID)
            .addSnapshotListener{snapshot, _ ->
                DataStore.topic = snapshot?.get("topic").toString()
                DataStore.questionsPicked = (snapshot?.get("questionsPicked") as? MutableList<Int>)!!
                if (DataStore.topic == "Theater" || DataStore.topic == "Oper" || DataStore.topic == "Lesung" || DataStore.topic == "Performance" || DataStore.topic == "Ausstellung" || DataStore.topic == "Konzert") {
                    listenerRegistration.remove()
                    getInput()
                }
            }
    }

    private fun getInput() {
        val intent = Intent(this, TopicResult::class.java)
        startActivity(intent)
    }

}