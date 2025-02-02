package br.com.phere.projectherescanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.phere.projectherescanner.viewmodel.EventosViewModel
import br.com.phere.projectherescanner.ui.components.EventoItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventosListScreen(viewModel: EventosViewModel, navController: NavController) {
    LaunchedEffect(Unit) {
        viewModel.resetMessages()
        viewModel.fetchEventosByStatus(isActive = true)
    }

    val eventos = viewModel.eventos.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val errorMessage = viewModel.errorMessage.collectAsState().value

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = { viewModel.fetchEventosByStatus(isActive = true) }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Eventos Ativos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                isLoading && eventos.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                eventos.isEmpty() -> {
                    Text(
                        text = "Nenhum evento ativo encontrado.",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                else -> {
                    LazyColumn {
                        items(eventos) { evento ->
                            EventoItem(
                                evento = evento,
                                onClick = { navController.navigate("eventoDetalhes/${evento.id}") }
                            )
                        }
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
