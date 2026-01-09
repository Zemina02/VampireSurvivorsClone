package com.example.game.highscore

import androidx.lifecycle.ViewModel
import com.example.game.models.Highscore
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HighscoreViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _highscore = MutableStateFlow(Highscore())
    val highscore: StateFlow<Highscore> = _highscore

    fun loadHighscore(userId: String) {
        db.collection("highscores").document(userId).get()
            .addOnSuccessListener { document ->
                val score = document.getLong("score")?.toInt() ?: 0
                val username = document.getString("username") ?: ""
                _highscore.value = Highscore(userId = userId, username = username, score = score)
            }
    }

    fun saveHighscore(userId: String, username: String, currentScore: Int) {
        val docRef = db.collection("highscores").document(userId)

        docRef.get().addOnSuccessListener { document ->
            val savedScore = document.getLong("score")?.toInt() ?: 0
            if (currentScore > savedScore) {
                val newScore = mapOf(
                    "userId" to userId,
                    "username" to username,
                    "score" to currentScore
                )
                docRef.set(newScore)
                _highscore.value = Highscore(userId = userId, username = username, score = currentScore)
            }
        }
    }
}
