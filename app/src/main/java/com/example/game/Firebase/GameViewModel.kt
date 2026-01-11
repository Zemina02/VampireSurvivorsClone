package com.example.game.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.game.highscore.HighscoreViewModel
import com.example.game.models.Highscore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class GameViewModel : ViewModel() {
    
    private val highScoreViewModel = HighscoreViewModel()
    
    // Game state
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score
    
    private val _health = MutableStateFlow(100)
    val health: StateFlow<Int> = _health
    
    private val _timeElapsed = MutableStateFlow(0L)
    val timeElapsed: StateFlow<Long> = _timeElapsed
    
    private val _enemyCount = MutableStateFlow(0)
    val enemyCount: StateFlow<Int> = _enemyCount
    
    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver
    
    private val _finalScore = MutableStateFlow(0)
    val finalScore: StateFlow<Int> = _finalScore
    
    private val _isNewRecord = MutableStateFlow(false)
    val isNewRecord: StateFlow<Boolean> = _isNewRecord
    
    fun addScore(points: Int) {
        _score.value += points
    }
    
    fun damagePlayer(amount: Int) {
        _health.value = (_health.value - amount).coerceAtLeast(0)
        if (_health.value <= 0) {
            endGame()
        }
    }
    
    fun updateTime(deltaTime: Long) {
        _timeElapsed.value += deltaTime
    }
    
    fun updateEnemyCount(count: Int) {
        _enemyCount.value = count
    }
    
    fun endGame() {
        _isGameOver.value = true
        _finalScore.value = _score.value
        
        viewModelScope.launch {
            saveHighScore()
        }
    }
    
    private fun saveHighScore() {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        val username = Firebase.auth.currentUser?.email ?: "User"
        
        if (userId.isNotEmpty()) {
            highScoreViewModel.saveHighscore(userId, username, _finalScore.value)
            _isNewRecord.value = true
        }
    }
    
    fun resetGame() {
        _score.value = 0
        _health.value = 100
        _timeElapsed.value = 0L
        _enemyCount.value = 0
        _isGameOver.value = false
        _finalScore.value = 0
        _isNewRecord.value = false
    }
}
