package com.example

import GamePlan.model.Tecnico
import com.gameplan.dto.CampeonatoDTO
import com.gameplan.dto.JogadorDTO
import com.gameplan.dto.TimeDTO
import com.gameplan.dto.TecnicoDTO
import com.gameplan.dto.TaticaDTO
import com.gameplan.dto.PartidaDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection
import java.sql.DriverManager

fun Application.configureDatabases() {
    val dbConnection: Connection = connectToPostgres(embedded = false)

    routing {
        // CRUD para Campeonato
        post("/campeonatos") {
            val dto = call.receive<CampeonatoDTO>()
            val statement = dbConnection.prepareStatement("INSERT INTO Campeonato (numero_times, premio, pontos, data_comeco, data_final, data_inscricao, campeao, artilheiro, maior_assistencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")
            statement.setInt(1, dto.numeroTimes)
            statement.setString(2, dto.premio)
            statement.setInt(3, dto.pontos)
            statement.setString(4, dto.dataComeco)
            statement.setString(5, dto.dataFinal)
            statement.setString(6, dto.dataInscricao)
            statement.setObject(7, dto.campeaoId)
            statement.setObject(8, dto.artilheiroId)
            statement.setObject(9, dto.maiorAssistenteId)
            statement.executeUpdate()
            call.respond(HttpStatusCode.Created)
        }

        get("/campeonatos/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val statement = dbConnection.prepareStatement("SELECT * FROM Campeonato WHERE id = ?")
            statement.setInt(1, id)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val dto = CampeonatoDTO(
                    id = resultSet.getInt("id"),
                    numeroTimes = resultSet.getInt("numero_times"),
                    premio = resultSet.getString("premio"),
                    pontos = resultSet.getInt("pontos"),
                    dataComeco = resultSet.getString("data_comeco"),
                    dataFinal = resultSet.getString("data_final"),
                    dataInscricao = resultSet.getString("data_inscricao"),
                    campeaoId = resultSet.getObject("campeao") as? Int,
                    artilheiroId = resultSet.getObject("artilheiro") as? Int,
                    maiorAssistenteId = resultSet.getObject("maior_assistencia") as? Int
                )
                call.respond(HttpStatusCode.OK, dto)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // CRUD para Jogador
        post("/jogadores") {
            val dto = call.receive<JogadorDTO>()
            val statement = dbConnection.prepareStatement("INSERT INTO Jogador (nome, altura, nacionalidade, data_nascimento, numero_camisa, posicao, pe_dominante, gols_totais, assistencias_totais, cartoes_amarelos, cartoes_vermelhos, clube) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
            statement.setString(1, dto.nome)
            statement.setInt(2, dto.altura)
            statement.setString(3, dto.nacionalidade)
            statement.setString(4, dto.dataNascimento)
            statement.setInt(5, dto.numeroCamisa)
            statement.setString(6, dto.posicao)
            statement.setString(7, dto.peDominante)
            statement.setInt(8, dto.golsTotais)
            statement.setInt(9, dto.assistenciasTotais)
            statement.setInt(10, dto.cartoesAmarelos)
            statement.setInt(11, dto.cartoesVermelhos)
            statement.setObject(12, dto.clubeId)
            statement.executeUpdate()
            call.respond(HttpStatusCode.Created)
        }
        get("/jogadores/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val statement = dbConnection.prepareStatement("SELECT * FROM Jogador WHERE id = ?")
            statement.setInt(1, id)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val dto = JogadorDTO(
                    id = resultSet.getInt("id"),
                    nome = resultSet.getString("nome"),
                    altura = resultSet.getInt("altura"),
                    nacionalidade = resultSet.getString("nacionalidade"),
                    dataNascimento = resultSet.getString("data_nascimento"),
                    numeroCamisa = resultSet.getInt("numero_camisa"),
                    posicao = resultSet.getString("posicao"),
                    peDominante = resultSet.getString("pe_dominante"),
                    golsTotais = resultSet.getInt("gols_totais"),
                    assistenciasTotais = resultSet.getInt("assistencias_totais"),
                    cartoesAmarelos = resultSet.getInt("cartoes_amarelos"),
                    cartoesVermelhos = resultSet.getInt("cartoes_vermelhos"),
                    clubeId = resultSet.getObject("clube") as? Int
                )
                call.respond(HttpStatusCode.OK, dto)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // CRUD para Time
        post("/times") {
            try {
                val rawBody = call.receiveText()
                println("[DEBUG] Corpo recebido em /times: $rawBody")
                val dto = kotlinx.serialization.json.Json.decodeFromString<com.gameplan.dto.TimeDTO>(rawBody)
                val statement = dbConnection.prepareStatement(
                    "INSERT INTO Team (nome, nacionalidade, data_fundacao, tecnico) VALUES (?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
                )
                statement.setString(1, dto.nome)
                statement.setString(2, dto.nacionalidade)
                statement.setString(3, dto.dataFundacao)
                statement.setObject(4, dto.tecnicoId)
                statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                var id: Int? = null
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1)
                }
                val responseDto = com.gameplan.dto.TimeDTO(
                    id = id,
                    nome = dto.nome,
                    nacionalidade = dto.nacionalidade,
                    dataFundacao = dto.dataFundacao,
                    tecnicoId = dto.tecnicoId
                )
                call.respond(HttpStatusCode.Created, responseDto)
            } catch (e: Exception) {
                println("[ERROR] Erro ao processar /times: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Erro ao processar time: ${e.message}")
            }
        }

        // CRUD para Tecnico
        post("/tecnicos") {
            try {
                val rawBody = call.receiveText()
                println("[DEBUG] Corpo recebido em /tecnicos: $rawBody")
                val dto = kotlinx.serialization.json.Json.decodeFromString<com.gameplan.dto.TecnicoDTO>(rawBody)
                val tecnico = Tecnico(
                    0,
                    dto.nome,
                    dto.nacionalidade,
                    dto.dataNascimento,
                    dto.email,
                    dto.senha
                )
                val statement = dbConnection.prepareStatement(
                    "INSERT INTO Tecnico (nome, nacionalidade, data_nascimento, email, senha) VALUES (?, ?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
                )
                statement.setString(1, tecnico.getNome())
                statement.setString(2, tecnico.getNacionalidade())
                statement.setString(3, tecnico.getDataNascimento())
                statement.setString(4, tecnico.getEmail())
                statement.setString(5, tecnico.getSenha())
                statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                var id: Int? = null
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1)
                }
                val responseDto = com.gameplan.dto.TecnicoDTO(
                    id = id,
                    nome = dto.nome,
                    nacionalidade = dto.nacionalidade,
                    dataNascimento = dto.dataNascimento,
                    email = dto.email,
                    senha = dto.senha
                )
                call.respond(HttpStatusCode.Created, responseDto)
            } catch (e: Exception) {
                println("[ERROR] Erro ao processar /tecnicos: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Erro ao processar técnico: ${e.message}")
            }
        }

        // CRUD para Tatica
        post("/taticas") {
            val dto = call.receive<TaticaDTO>()
            val statement = dbConnection.prepareStatement("INSERT INTO Tatica (plano_jogo, conduta, instrucao_ataque, instrucao_defesa, instrucao_meio, pressao, estilo, tempo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
            statement.setString(1, dto.planoJogo)
            statement.setString(2, dto.conduta)
            statement.setString(3, dto.instrucaoAtaque)
            statement.setString(4, dto.instrucaoDefesa)
            statement.setString(5, dto.instrucaoMeio)
            statement.setInt(6, dto.pressao)
            statement.setInt(7, dto.estilo)
            statement.setInt(8, dto.tempo)
            statement.executeUpdate()
            call.respond(HttpStatusCode.Created)
        }

        // CRUD para Partida
        post("/partidas") {
            val dto = call.receive<PartidaDTO>()
            val statement = dbConnection.prepareStatement("INSERT INTO Partida (time_1, time_2, gols_time_1, gols_time_2, lugar, data_partida, hora_partida, empate, gols_time_1_penaltis, gols_time_2_penaltis, vencedor, campeonato) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
            statement.setInt(1, dto.time1Id)
            statement.setInt(2, dto.time2Id)
            statement.setInt(3, dto.golsTime1)
            statement.setInt(4, dto.golsTime2)
            statement.setString(5, dto.lugar)
            statement.setString(6, dto.dataPartida)
            statement.setString(7, dto.horaPartida)
            statement.setBoolean(8, dto.empate)
            statement.setInt(9, dto.golsTime1Penaltis)
            statement.setInt(10, dto.golsTime2Penaltis)
            statement.setObject(11, dto.vencedorId)
            statement.setObject(12, dto.campeonatoId)
            statement.executeUpdate()
            call.respond(HttpStatusCode.Created)
        }
    }
}

fun Application.connectToPostgres(embedded: Boolean): Connection {
    Class.forName("org.postgresql.Driver")
    if (embedded) {
        log.info("Using embedded H2 database for testing; replace this flag to use postgres")
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
    } else {
        val url = environment.config.property("postgres.url").getString()
        log.info("Connecting to postgres database at $url")
        val user = environment.config.property("postgres.user").getString()
        val password = environment.config.property("postgres.password").getString()

        return DriverManager.getConnection(url, user, password)
    }
}
