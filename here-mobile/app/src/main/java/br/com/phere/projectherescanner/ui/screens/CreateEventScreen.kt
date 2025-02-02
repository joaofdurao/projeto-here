package br.com.phere.projectherescanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.phere.projectherescanner.viewmodel.EventosViewModel

@Composable
fun CreateEventScreen(navController: NavController, viewModel: EventosViewModel) {
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var dataHora by remember { mutableStateOf("") }
    var horaAula by remember { mutableStateOf("") }
    var curso by remember { mutableStateOf("") }
    var local by remember { mutableStateOf("") }
    var palestrante by remember { mutableStateOf("") }
    var profissaoPalestrante by remember { mutableStateOf("") }

    var localErrorMessage by remember { mutableStateOf<String?>(null) }

    val confirmationMessage by viewModel.confirmationMessage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(confirmationMessage) {
        if (confirmationMessage != null) {
            titulo = ""
            descricao = ""
            dataHora = ""
            horaAula = ""
            curso = ""
            local = ""
            palestrante = ""
            profissaoPalestrante = ""
            localErrorMessage = null

            // Retorna para a tela anterior
            navController.popBackStack()

            // Reseta as mensagens no ViewModel
            viewModel.resetMessages()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Criar Novo Evento",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = dataHora,
                onValueChange = { dataHora = it },
                label = { Text("Data e Hora (ex: 30/10/2024 12:00)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = horaAula,
                onValueChange = { horaAula = it },
                label = { Text("Hora Aula") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }

        item {
            OutlinedTextField(
                value = curso,
                onValueChange = { curso = it },
                label = { Text("Curso") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = local,
                onValueChange = { local = it },
                label = { Text("Local") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = palestrante,
                onValueChange = { palestrante = it },
                label = { Text("Palestrante") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = profissaoPalestrante,
                onValueChange = { profissaoPalestrante = it },
                label = { Text("Profissão do Palestrante") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (errorMessage != null || localErrorMessage != null) {
            item {
                Text(
                    text = errorMessage ?: localErrorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        item {
            Button(
                onClick = {
                    if (titulo.isNotBlank() && descricao.isNotBlank() && dataHora.isNotBlank() && horaAula.isNotBlank()) {
                        val horaAulaInt = horaAula.toIntOrNull()
                        if (horaAulaInt != null) {
                            viewModel.createEvent(
                                titulo = titulo,
                                descricao = descricao,
                                dataHora = dataHora,
                                horaAula = horaAulaInt,
                                curso = curso,
                                local = local,
                                palestrante = palestrante,
                                profissaoPalestrante = profissaoPalestrante
                            )
                        } else {
                            localErrorMessage = "Hora Aula deve ser um número válido"
                        }
                    } else {
                        localErrorMessage = "Preencha todos os campos obrigatórios"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Evento")
            }
        }
    }
}




