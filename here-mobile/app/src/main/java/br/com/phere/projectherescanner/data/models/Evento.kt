package br.com.phere.projectherescanner.data.models

import java.util.UUID

data class Evento(
    val id: UUID?,
    val titulo: String,
    val descricao: String,
    val dataHora: String,
    val horaAula: Int,
    val curso: String,
    val local: String,
    val palestrante: String,
    val profissaoPalestrante: String,
    val status: Boolean
)
