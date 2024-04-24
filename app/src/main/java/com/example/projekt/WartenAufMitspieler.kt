package com.example.projekt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class WartenAufMitspieler : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    lateinit var listenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.warten_auf_mitspieler)

        init()
    }

    private fun init() {
        listenerRegistration = db.collection("Games").document(DataStore.gameID)
            .addSnapshotListener{snapshot, _ ->
                DataStore.topic = snapshot?.get("topic").toString()
                println("Im still on")
                if (DataStore.topic == "Theater" || DataStore.topic == "Oper" || DataStore.topic == "Lesung" || DataStore.topic == "Performance" || DataStore.topic == "Ausstellung" || DataStore.topic == "Konzert") {
                    listenerRegistration.remove()
                    getInput()
                }
            }
    }

    private fun getInput() {
        val intent = Intent(this, ThemaErgebnis::class.java)
        startActivity(intent)
    }

}