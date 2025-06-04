package com.example

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection
import java.sql.DriverManager
import org.slf4j.event.*
import kotlinx.serialization.Serializable

@Serializable
data class TeamResponse(val teamName: String, val players: List<String>)

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/team/{teamName}/players") {
            val teamName = call.parameters["teamName"]
            println("Recebido teamName: $teamName")
            if (teamName.isNullOrBlank()) {
                println("Erro: teamName está vazio ou nulo")
                call.respond(HttpStatusCode.BadRequest, "Team name is required")
                return@get
            }

            println("Iniciando processamento do endpoint /team/{teamName}/players")
            println("Parâmetro recebido: teamName = $teamName")
            println("Verificando se teamName está vazio ou nulo...")
            
            // Simulação de resposta com jogadores
            val players = listOf(
                "Cristiano Ronaldo",
                "Karim Benzema",
                "Luka Modric",
                "Sergio Ramos"
            )

            println("Preparando resposta com jogadores...")
            println("Resposta preparada: TeamResponse(teamName = $teamName, players = $players)")
            call.respond(HttpStatusCode.OK, TeamResponse(teamName, players))
        }
    }
}
