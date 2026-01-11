package com.example.game.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.game.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

data class RegisterState(
    var name : String = "",
    var email : String = "",
    var password : String = "",
    var confirmPassword : String = "",
    var error : String? = null,
    var loading : Boolean = false
)

class RegisterViewModel : ViewModel() {

    var uiState = mutableStateOf(RegisterState())
        private set

    fun onChangeName(name : String) {
        uiState.value = uiState.value.copy(
            name = name,
            error = null
        )
    }

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

    fun onChangeConfirmPassword(confirmPassword : String) {
        uiState.value = uiState.value.copy(
            confirmPassword = confirmPassword,
            error = null
        )
    }

    fun register(onRegisterSuccess : () -> Unit) {
        uiState.value = uiState.value.copy(
            loading = true
        )

        if (uiState.value.name.isEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Name is required",
                loading = false
            )
            return
        }

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

        if (uiState.value.password != uiState.value.confirmPassword) {
            uiState.value = uiState.value.copy(
                error = "Passwords do not match",
                loading = false
            )
            return
        }

        val auth = Firebase.auth
        val db = Firebase.firestore

        auth.createUserWithEmailAndPassword(uiState.value.email, uiState.value.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result.user
                    if (firebaseUser == null) {
                        uiState.value = uiState.value.copy(
                            error = "User authentication failed",
                            loading = false
                        )
                        return@addOnCompleteListener
                    }

                    val userId = firebaseUser.uid

                    val user = User(
                        docId = userId,
                        name = uiState.value.name,
                        email = uiState.value.email,
                        bio = ""
                    )

                    db.collection("users")
                        .document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            uiState.value = uiState.value.copy(
                                loading = false,
                                error = null
                            )
                            onRegisterSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.e("RegisterViewModel", "Error saving user", e)
                            uiState.value = uiState.value.copy(
                                error = "Error saving user profile",
                                loading = false
                            )
                        }
                }
                else {
                    Log.w("RegisterViewModel", "createUserWithEmail:failure", task.exception)
                    uiState.value = uiState.value.copy(
                        error = task.exception?.message,
                        loading = false
                    )
                }
            }
    }
}
