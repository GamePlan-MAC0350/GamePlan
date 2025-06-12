package com.gameplan.dto

import kotlinx.serialization.Serializable

@Serializable
data class TimeComTaticaEJogadoresDTO(
    val id: Int?,
    val nome: String,
    val nacionalidade: String,
    val dataFundacao: String,
    val artilheiro: String?,
    val maiorAssistente: String?,
    val partidasJogadas: Int,
    val golsMarcados: Int,
    val golsSofridos: Int,
    val pontos: Int,
    val vitorias: Int,
    val derrotas: Int,
    val tatica: TaticaDTO?,
    val jogadores: List<String>,
    val tecnicoNome: String? = null
)
