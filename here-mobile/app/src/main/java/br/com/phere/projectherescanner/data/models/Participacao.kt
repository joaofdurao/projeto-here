package br.com.phere.projectherescanner.data.models

import java.util.UUID

data class Participacao(
    val id: UUID,
    val presenca: Boolean,
    val status: Boolean,
    val idUsuario: UUID,
    val idEvento: UUID
)
