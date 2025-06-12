package com.gameplan.dto

import kotlinx.serialization.Serializable

@Serializable
data class TaticaDTO(
    val id: Int? = null,
    val planoJogo: String,
    val conduta: String,
    val instrucaoAtaque: String,
    val instrucaoDefesa: String,
    val instrucaoMeio: String,
    val pressao: Int,
    val estilo: Int,
    val tempo: Int
)
