package br.com.phere.projectherescanner.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.phere.projectherescanner.data.api.RetrofitInstance
import br.com.phere.projectherescanner.data.auth.LoginRequest
import br.com.phere.projectherescanner.data.auth.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = RetrofitInstance.api.login(LoginRequest(email, password))

                if (response.isSuccessful) {
                    val token = response.body()?.string()
                    Log.d("AuthViewModel", "Código de resposta: ${response.code()}, Token recebido: $token")

                    if (!token.isNullOrEmpty()) {
                        RetrofitInstance.setAuthToken(token)
                        saveToken(token)
                        _loginState.value = LoginState.Success
                    } else {
                        _loginState.value = LoginState.Error("Token não encontrado na resposta")
                    }
                } else {
                    Log.e("AuthViewModel", "Falha no login: Código ${response.code()}, mensagem: ${response.message()}")
                    _loginState.value = LoginState.Error("Login falhou: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Erro de exceção: ${e.message}", e)
                _loginState.value = LoginState.Error("Erro: ${e.message}")
            }
        }
    }

    private fun saveToken(token: String) {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("jwt_token", token).apply()

        val savedToken = sharedPreferences.getString("jwt_token", null)
        if (savedToken == null) {
            _loginState.value = LoginState.Error("Erro ao salvar o token no SharedPreferences")
        }
    }
}