package br.com.phere.projectherescanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.phere.projectherescanner.data.api.RetrofitInstance
import br.com.phere.projectherescanner.data.models.Evento
import br.com.phere.projectherescanner.utils.parseDataHora
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventosViewModel : ViewModel() {

    private val _evento = MutableStateFlow<Evento?>(null)
    val evento: StateFlow<Evento?> = _evento

    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _confirmationMessage = MutableStateFlow<String?>(null)
    val confirmationMessage: StateFlow<String?> = _confirmationMessage

    init {
        fetchEventosByStatus(true)
    }

    fun fetchEventosByStatus(isActive: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = RetrofitInstance.api.getEventosByStatus(isActive)
                _eventos.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao carregar eventos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadEventoById(eventoId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                _evento.value = RetrofitInstance.api.getEventoById(eventoId)
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao carregar detalhes do evento: ${e.message}"
                _evento.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun confirmPresence(participacaoId: String) {
        viewModelScope.launch {
            _errorMessage.value = null
            _confirmationMessage.value = null

            try {
                val response = RetrofitInstance.api.confirmPresenca(participacaoId)
                if (response.isSuccessful) {
                    val confirmationMessage = response.body()?.string() ?: "Presença confirmada!"
                    _confirmationMessage.value = confirmationMessage

                    launch {
                        kotlinx.coroutines.delay(1000)
                        _confirmationMessage.value = null
                    }
                } else {
                    _errorMessage.value = "Erro ao confirmar presença: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao confirmar presença: ${e.message}"
            }
        }
    }

    fun createEvent(
        titulo: String,
        descricao: String,
        dataHora: String,
        horaAula: Int,
        curso: String,
        local: String,
        palestrante: String,
        profissaoPalestrante: String
    ) {
        viewModelScope.launch {
            try {
                val dataHoraLocalDateTime = parseDataHora(dataHora)
                if (dataHoraLocalDateTime == null) {
                    _errorMessage.value = "Formato de data e hora inválido. Use dd/MM/yyyy HH:mm."
                    return@launch
                }

                val newEvent = Evento(
                    id = null,
                    titulo = titulo,
                    descricao = descricao,
                    dataHora = dataHoraLocalDateTime.toString(),
                    horaAula = horaAula,
                    curso = curso,
                    local = local,
                    palestrante = palestrante,
                    profissaoPalestrante = profissaoPalestrante,
                    status = true
                )

                val response = RetrofitInstance.api.createEvento(newEvent)

                if (response.isSuccessful && response.code() == 201) {
                    _confirmationMessage.value = "Evento criado com sucesso!"
                    fetchEventosByStatus(true)
                } else {
                    _errorMessage.value = "Erro ao criar evento: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao criar evento: ${e.message}"
            }
        }
    }

    fun resetMessages() {
        _errorMessage.value = null
        _confirmationMessage.value = null
    }
}
