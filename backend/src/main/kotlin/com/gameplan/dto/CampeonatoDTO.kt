package com.gameplan.dto

import kotlinx.serialization.Serializable

@Serializable
data class CampeonatoDTO(
    val id: Int? = null,
    val nome: String, // NOVO CAMPO
    val numeroTimes: Int,
    val premio: String,
    val pontos: Int,
    val dataComeco: String,
    val dataFinal: String,
    val dataInscricao: String,
    val campeaoId: Int? = null,
    val artilheiroId: Int? = null,
    val maiorAssistenteId: Int? = null,
    val timesInscritos: Int? = null, // Adicionado para refletir o banco
    val idTimeFundador: Int, // NOVO CAMPO
    val sorteio: Boolean = false // NOVO CAMPO
)
