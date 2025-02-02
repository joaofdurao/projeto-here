package br.com.phere.projectherescanner.data.auth

sealed class LoginState {
    object Idle : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
    object Loading : LoginState()
}