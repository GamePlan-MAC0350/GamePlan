package com.gameplan.dto

data class CampeonatoDTO(
    val id: Int? = null,
    val numeroTimes: Int,
    val premio: String,
    val pontos: Int,
    val dataComeco: String,
    val dataFinal: String,
    val dataInscricao: String,
    val campeaoId: Int? = null,
    val artilheiroId: Int? = null,
    val maiorAssistenteId: Int? = null
)
