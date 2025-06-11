package com.gameplan.dto

data class TimeDTO(
    val id: Int? = null,
    val nome: String,
    val nacionalidade: String,
    val dataFundacao: String,
    val artilheiroId: Int? = null,
    val maiorAssistenteId: Int? = null,
    val partidasJogadas: Int,
    val golsMarcados: Int,
    val golsSofridos: Int,
    val pontos: Int,
    val vitorias: Int,
    val derrotas: Int,
    val taticaId: Int? = null,
    val tecnicoId: Int? = null,
    val campeonatoId: Int? = null
)
