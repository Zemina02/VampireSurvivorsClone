package com.example.game.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

data class LoginState(
    var email : String = "",
    var password : String = "",
    var error : String? = null,
    var loading : Boolean = false
)

class LoginViewModel : ViewModel() {

    var uiState = mutableStateOf(LoginState())
        private set

    fun onChangeEmail(email : String) {
        uiState.value = uiState.value.copy(
            email = email,
            error = null
        )
    }

    fun onChangePassword(password : String) {
        uiState.value = uiState.value.copy(
            password = password,
            error = null
        )
    }

    fun login(onLoginSuccess : () -> Unit) {
        uiState.value = uiState.value.copy(
            loading = true
        )

        if (uiState.value.email.isEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Email is required",
                loading = false
            )
            return
        }

        if (uiState.value.password.isEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Password is required",
                loading = false
            )
            return
        }

        val auth = Firebase.auth

        auth.signInWithEmailAndPassword(
            uiState.value.email,
            uiState.value.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LoginViewModel", "signInWithEmail:success")
                    uiState.value = uiState.value.copy(
                        loading = false,
                        error = null
                    )
                    onLoginSuccess()
                } else {
                    Log.w("LoginViewModel", "signInWithEmail:failure", task.exception)
                    uiState.value = uiState.value.copy(
                        error = task.exception?.message,
                        loading = false
                    )
                }
            }
    }
}
