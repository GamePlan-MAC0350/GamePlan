package com.gameplan.dto

data class PartidaDTO(
    val id: Int? = null,
    val time1Id: Int,
    val time2Id: Int,
    val golsTime1: Int,
    val golsTime2: Int,
    val lugar: String,
    val dataPartida: String,
    val horaPartida: String,
    val empate: Boolean,
    val golsTime1Penaltis: Int,
    val golsTime2Penaltis: Int,
    val vencedorId: Int? = null,
    val campeonatoId: Int? = null
)
