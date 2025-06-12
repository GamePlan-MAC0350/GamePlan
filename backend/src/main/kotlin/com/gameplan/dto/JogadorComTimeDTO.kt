package com.gameplan.dto

import kotlinx.serialization.Serializable

@Serializable
data class JogadorComTimeDTO(
    val id: Int?,
    val nome: String,
    val altura: Int,
    val nacionalidade: String,
    val dataNascimento: String,
    val numeroCamisa: Int,
    val posicao: String,
    val peDominante: String,
    val golsTotais: Int,
    val assistenciasTotais: Int,
    val cartoesAmarelos: Int,
    val cartoesVermelhos: Int,
    val clubeId: Int?,
    val nomeTime: String?
)
