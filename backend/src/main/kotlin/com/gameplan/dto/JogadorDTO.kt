package com.gameplan.dto

data class JogadorDTO(
    val id: Int? = null,
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
    val clubeId: Int? = null
)
