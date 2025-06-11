package com.gameplan.dto

import kotlinx.serialization.Serializable

@Serializable
data class TecnicoDTO(
    val id: Int? = null,
    val nome: String,
    val nacionalidade: String,
    val dataNascimento: String,
    val email: String,
    val senha: String,
    val timeId: Int? = null,
    val telefone: String? = null
)
