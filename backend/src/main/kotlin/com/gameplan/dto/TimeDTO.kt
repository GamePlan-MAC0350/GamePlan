package com.gameplan.dto

import kotlinx.serialization.Serializable

@Serializable
data class TimeDTO(
    val id: Int? = null,
    val nome: String,
    val nacionalidade: String,
    val dataFundacao: String,
    val artilheiroId: Int? = null,
    val maiorAssistenteId: Int? = null,
    val partidasJogadas: Int = 0,
    val golsMarcados: Int = 0,
    val golsSofridos: Int = 0,
    val pontos: Int = 0,
    val vitorias: Int = 0,
    val derrotas: Int = 0,
    val taticaId: Int? = null,
    val tecnicoId: Int? = null,
    val campeonatoId: Int? = null
)
