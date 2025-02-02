package br.com.phere.projectherescanner.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatDataHora(dataHora: String): String {
    val originalFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val originalFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val targetFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")

    return try {
        LocalDateTime.parse(dataHora, originalFormatter1).format(targetFormatter)
    } catch (e: Exception) {
        LocalDateTime.parse(dataHora, originalFormatter2).format(targetFormatter)
    }
}

fun parseDataHora(dataHora: String): LocalDateTime? {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return try {
        LocalDateTime.parse(dataHora, formatter)
    } catch (e: Exception) {
        null
    }
}

