package com.gameplan.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDTO(
    val tecnico: TecnicoDTO,
    val timeId: Int?
)
