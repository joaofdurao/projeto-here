package br.com.phere.projectherescanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.phere.projectherescanner.utils.formatDataHora
import br.com.phere.projectherescanner.viewmodel.EventosViewModel


@Composable
fun EventoDetalhesScreen(eventoId: String, viewModel: EventosViewModel) {
    LaunchedEffect(eventoId) {
        viewModel.resetMessages()
        viewModel.loadEventoById(eventoId)
    }

    val evento = viewModel.evento.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        evento?.let {
            Text(
                text = it.titulo,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(text = it.descricao, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
            Text(
                text = "Data: ${formatDataHora(it.dataHora)}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(text = "Local: ${it.local}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
            Text(text = "Palestrante: ${it.palestrante}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
            Text(text = "Profissão: ${it.profissaoPalestrante}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { /* Implementar funcionalidade de editar evento */ }) {
                Text("Editar Evento")
            }
        } ?: run {
            Text("Erro ao carregar detalhes do evento.", color = MaterialTheme.colorScheme.error)
        }
    }
}

