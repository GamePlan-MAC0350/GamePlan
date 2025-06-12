package com.example

import GamePlan.model.Tecnico
import com.gameplan.dto.CampeonatoDTO
import com.gameplan.dto.JogadorDTO
import com.gameplan.dto.TimeDTO
import com.gameplan.dto.TecnicoDTO
import com.gameplan.dto.TaticaDTO
import com.gameplan.dto.PartidaDTO
import com.gameplan.dto.JogadorComTimeDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection
import java.sql.DriverManager
import kotlinx.serialization.Serializable

@Serializable
data class JogadorComTimeDTO(
    val id: Int?,
    val nome: String,
    val altura: Int,
    val nacionalidade: String,
    val dataNascimento: String,
    val numeroCamisa: Int,
    val posicao: String,
    val peDominante: String,
    val golsTotais: Int,
    val assistenciasTotais: Int,
    val cartoesAmarelos: Int,
    val cartoesVermelhos: Int,
    val clubeId: Int?,
    val nomeTime: String?
)

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
            try {
                val rawBody = call.receiveText()
                println("[DEBUG] Corpo recebido em /jogadores: $rawBody")
                val dto = kotlinx.serialization.json.Json.decodeFromString<com.gameplan.dto.JogadorDTO>(rawBody)
                println("[DEBUG] DTO recebido: $dto")
                val statement = dbConnection.prepareStatement("INSERT INTO Jogador (nome, altura, nacionalidade, data_nascimento, numero_camisa, posicao, pe_dominante, gols_totais, assistencias_totais, cartoes_amarelos, cartoes_vermelhos, clube) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS)
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
                val rows = statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                var jogadorId: Int? = null
                if (generatedKeys.next()) {
                    jogadorId = generatedKeys.getInt(1)
                }
                println("[DEBUG] Jogador inserido com id: $jogadorId (linhas afetadas: $rows)")
                val responseDto = com.gameplan.dto.JogadorDTO(
                    id = jogadorId,
                    nome = dto.nome,
                    altura = dto.altura,
                    nacionalidade = dto.nacionalidade,
                    dataNascimento = dto.dataNascimento,
                    numeroCamisa = dto.numeroCamisa,
                    posicao = dto.posicao,
                    peDominante = dto.peDominante,
                    golsTotais = dto.golsTotais,
                    assistenciasTotais = dto.assistenciasTotais,
                    cartoesAmarelos = dto.cartoesAmarelos,
                    cartoesVermelhos = dto.cartoesVermelhos,
                    clubeId = dto.clubeId
                )
                call.respond(HttpStatusCode.Created, responseDto)
            } catch (e: Exception) {
                println("[ERROR] Erro ao processar /jogadores: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Erro ao processar jogador: ${e.message}")
            }
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

        // GET /jogadores?nome=... - busca jogador pelo nome e retorna também o nome do time
        get("/jogadores") {
            val nome = call.request.queryParameters["nome"]
            println("[DEBUG] GET /jogadores?nome chamado com nome: $nome")
            if (nome == null) {
                call.respond(HttpStatusCode.BadRequest, "Nome do jogador não informado")
                return@get
            }
            val statement = dbConnection.prepareStatement("SELECT * FROM Jogador WHERE nome = ?")
            statement.setString(1, nome)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val clubeId = resultSet.getObject("clube")
                var nomeTime: String? = null
                println("[DEBUG] Valor bruto de clubeId: $clubeId (tipo: ${clubeId?.javaClass})")
                if (clubeId != null) {
                    try {
                        val clubeIdInt = when (clubeId) {
                            is Int -> clubeId
                            is Long -> clubeId.toInt()
                            is Number -> clubeId.toInt()
                            is String -> clubeId.toIntOrNull()
                            else -> null
                        }
                        println("[DEBUG] clubeId convertido para Int: $clubeIdInt")
                        if (clubeIdInt != null) {
                            val timeStmt = dbConnection.prepareStatement("SELECT nome FROM Team WHERE id = ?")
                            timeStmt.setInt(1, clubeIdInt)
                            val timeResult = timeStmt.executeQuery()
                            if (timeResult.next()) {
                                nomeTime = timeResult.getString("nome")
                                println("[DEBUG] Nome do time encontrado: $nomeTime")
                            } else {
                                println("[DEBUG] Nenhum time encontrado para clubeId: $clubeIdInt")
                            }
                        } else {
                            println("[DEBUG] clubeIdInt é null após conversão")
                        }
                    } catch (e: Exception) {
                        println("[DEBUG] Erro ao buscar nome do time: ${e.message}")
                    }
                } else {
                    println("[DEBUG] clubeId é null")
                }
                val dto = JogadorComTimeDTO(
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
                    clubeId = (clubeId as? Int),
                    nomeTime = nomeTime
                )
                println("[DEBUG] Jogador encontrado: $dto")
                call.respond(HttpStatusCode.OK, dto)
            } else {
                println("[DEBUG] Nenhum jogador encontrado com nome: $nome")
                call.respond(HttpStatusCode.NotFound, "Jogador não encontrado")
            }
        }

        // CRUD para Time
        post("/times") {
            try {
                val rawBody = call.receiveText()
                println("[DEBUG] Corpo recebido em /times: $rawBody")
                val dto = kotlinx.serialization.json.Json.decodeFromString<com.gameplan.dto.TimeDTO>(rawBody)
                println("[DEBUG] DTO recebido: $dto")
                val statement = dbConnection.prepareStatement(
                    "INSERT INTO Team (nome, nacionalidade, data_fundacao, tecnico, tatica) VALUES (?, ?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
                )
                statement.setString(1, dto.nome)
                statement.setString(2, dto.nacionalidade)
                statement.setString(3, dto.dataFundacao)
                statement.setObject(4, dto.tecnicoId)
                statement.setObject(5, dto.taticaId)
                val rows = statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                var id: Int? = null
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1)
                }
                println("[DEBUG] Time inserido com id: $id (linhas afetadas: $rows)")
                val responseDto = com.gameplan.dto.TimeDTO(
                    id = id,
                    nome = dto.nome,
                    nacionalidade = dto.nacionalidade,
                    dataFundacao = dto.dataFundacao,
                    tecnicoId = dto.tecnicoId,
                    taticaId = dto.taticaId
                )
                call.respond(HttpStatusCode.Created, responseDto)
            } catch (e: Exception) {
                println("[ERROR] Erro ao processar /times: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Erro ao processar time: ${e.message}")
            }
        }

        // GET /times/{id} - retorna DTO do time
        get("/times/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            println("[DEBUG] GET /times/{id} chamado com id: $id")
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }
            val statement = dbConnection.prepareStatement("SELECT * FROM Team WHERE id = ?")
            statement.setInt(1, id)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val dto = com.gameplan.dto.TimeDTO(
                    id = resultSet.getInt("id"),
                    nome = resultSet.getString("nome"),
                    nacionalidade = resultSet.getString("nacionalidade"),
                    dataFundacao = resultSet.getString("data_fundacao"),
                    tecnicoId = resultSet.getObject("tecnico") as? Int,
                    taticaId = resultSet.getObject("tatica") as? Int
                )
                println("[DEBUG] Time encontrado: $dto")
                call.respond(HttpStatusCode.OK, dto)
            } else {
                println("[DEBUG] Nenhum time encontrado com id: $id")
                call.respond(HttpStatusCode.NotFound, "Time não encontrado")
            }
        }

        // GET /times?nome=... - busca time pelo nome e retorna info, tática e jogadores
        get("/times") {
            val nome = call.request.queryParameters["nome"]
            println("[DEBUG] GET /times?nome chamado com nome: $nome")
            if (nome == null) {
                call.respond(HttpStatusCode.BadRequest, "Nome do time não informado")
                return@get
            }
            val statement = dbConnection.prepareStatement("SELECT * FROM Team WHERE nome = ?")
            statement.setString(1, nome)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val timeId = resultSet.getInt("id")
                println("[DEBUG] Time encontrado com id: $timeId")
                // Buscar artilheiro e maior assistente (nomes)
                var artilheiroNome: String? = null
                var maiorAssistenteNome: String? = null
                val artilheiroId = resultSet.getObject("artilheiro") as? Int
                val maiorAssistenteId = resultSet.getObject("maior_assistente") as? Int
                if (artilheiroId != null) {
                    val artStmt = dbConnection.prepareStatement("SELECT nome FROM Jogador WHERE id = ?")
                    artStmt.setInt(1, artilheiroId)
                    val artRes = artStmt.executeQuery()
                    if (artRes.next()) artilheiroNome = artRes.getString("nome")
                    println("[DEBUG] Nome do artilheiro: $artilheiroNome")
                }
                if (maiorAssistenteId != null) {
                    val assStmt = dbConnection.prepareStatement("SELECT nome FROM Jogador WHERE id = ?")
                    assStmt.setInt(1, maiorAssistenteId)
                    val assRes = assStmt.executeQuery()
                    if (assRes.next()) maiorAssistenteNome = assRes.getString("nome")
                    println("[DEBUG] Nome do maior assistente: $maiorAssistenteNome")
                }
                // Buscar tática
                val taticaId = resultSet.getObject("tatica") as? Int
                var taticaDto: com.gameplan.dto.TaticaDTO? = null
                if (taticaId != null) {
                    val taticaStmt = dbConnection.prepareStatement("SELECT * FROM Tatica WHERE id = ?")
                    taticaStmt.setInt(1, taticaId)
                    val taticaRes = taticaStmt.executeQuery()
                    if (taticaRes.next()) {
                        taticaDto = com.gameplan.dto.TaticaDTO(
                            id = taticaRes.getInt("id"),
                            planoJogo = taticaRes.getString("plano_jogo"),
                            conduta = taticaRes.getString("conduta"),
                            instrucaoAtaque = taticaRes.getString("instrucao_ataque"),
                            instrucaoDefesa = taticaRes.getString("instrucao_defesa"),
                            instrucaoMeio = taticaRes.getString("instrucao_meio"),
                            pressao = taticaRes.getInt("pressao"),
                            estilo = taticaRes.getInt("estilo"),
                            tempo = taticaRes.getInt("tempo")
                        )
                        println("[DEBUG] Tática encontrada: $taticaDto")
                    } else {
                        println("[DEBUG] Nenhuma tática encontrada para id: $taticaId")
                    }
                } else {
                    println("[DEBUG] taticaId é null")
                }
                // Buscar jogadores do time
                val jogadoresStmt = dbConnection.prepareStatement("SELECT nome FROM Jogador WHERE clube = ?")
                jogadoresStmt.setInt(1, timeId)
                val jogadoresRes = jogadoresStmt.executeQuery()
                val jogadores = mutableListOf<String>()
                while (jogadoresRes.next()) {
                    jogadores.add(jogadoresRes.getString("nome"))
                }
                println("[DEBUG] Jogadores encontrados: $jogadores")
                // Buscar nome do técnico
                val tecnicoId = resultSet.getObject("tecnico") as? Int
                var tecnicoNome: String? = null
                if (tecnicoId != null) {
                    val tecnicoStmt = dbConnection.prepareStatement("SELECT nome FROM Tecnico WHERE id = ?")
                    tecnicoStmt.setInt(1, tecnicoId)
                    val tecnicoRes = tecnicoStmt.executeQuery()
                    if (tecnicoRes.next()) {
                        tecnicoNome = tecnicoRes.getString("nome")
                    }
                }
                // Montar DTO de resposta
                val dto = com.gameplan.dto.TimeComTaticaEJogadoresDTO(
                    id = timeId,
                    nome = resultSet.getString("nome"),
                    nacionalidade = resultSet.getString("nacionalidade"),
                    dataFundacao = resultSet.getString("data_fundacao"),
                    artilheiro = artilheiroNome,
                    maiorAssistente = maiorAssistenteNome,
                    partidasJogadas = resultSet.getInt("partidas_jogadas_totais"),
                    golsMarcados = resultSet.getInt("gols_marcados"),
                    golsSofridos = resultSet.getInt("gols_sofridos"),
                    pontos = resultSet.getInt("pontos"),
                    vitorias = resultSet.getInt("vitorias"),
                    derrotas = resultSet.getInt("derrotas"),
                    tatica = taticaDto,
                    jogadores = jogadores,
                    tecnicoNome = tecnicoNome
                )
                println("[DEBUG] TimeComTaticaEJogadoresDTO montado: $dto")
                call.respond(HttpStatusCode.OK, dto)
            } else {
                println("[DEBUG] Nenhum time encontrado com nome: $nome")
                call.respond(HttpStatusCode.NotFound, "Time não encontrado")
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
            try {
                val rawBody = call.receiveText()
                println("[DEBUG] Corpo recebido em /taticas: $rawBody")
                val dto = kotlinx.serialization.json.Json.decodeFromString<com.gameplan.dto.TaticaDTO>(rawBody)
                println("[DEBUG] DTO recebido: $dto")
                val statement = dbConnection.prepareStatement(
                    "INSERT INTO Tatica (plano_jogo, conduta, instrucao_ataque, instrucao_defesa, instrucao_meio, pressao, estilo, tempo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
                )
                statement.setString(1, dto.planoJogo)
                statement.setString(2, dto.conduta)
                statement.setString(3, dto.instrucaoAtaque)
                statement.setString(4, dto.instrucaoDefesa)
                statement.setString(5, dto.instrucaoMeio)
                statement.setInt(6, dto.pressao)
                statement.setInt(7, dto.estilo)
                statement.setInt(8, dto.tempo)
                val rows = statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                var taticaId: Int? = null
                if (generatedKeys.next()) {
                    taticaId = generatedKeys.getInt(1)
                }
                println("[DEBUG] Tática inserida com id: $taticaId (linhas afetadas: $rows)")
                val responseDto = com.gameplan.dto.TaticaDTO(
                    id = taticaId,
                    planoJogo = dto.planoJogo,
                    conduta = dto.conduta,
                    instrucaoAtaque = dto.instrucaoAtaque,
                    instrucaoDefesa = dto.instrucaoDefesa,
                    instrucaoMeio = dto.instrucaoMeio,
                    pressao = dto.pressao,
                    estilo = dto.estilo,
                    tempo = dto.tempo
                )
                call.respond(HttpStatusCode.Created, responseDto)
            } catch (e: Exception) {
                println("[ERROR] Erro ao processar /taticas: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Erro ao processar tática: ${e.message}")
            }
        }

        // GET /taticas/{id} - retorna DTO da tática
        get("/taticas/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            println("[DEBUG] GET /taticas/{id} chamado com id: $id")
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }
            val statement = dbConnection.prepareStatement("SELECT * FROM Tatica WHERE id = ?")
            statement.setInt(1, id)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val dto = TaticaDTO(
                    id = resultSet.getInt("id"),
                    planoJogo = resultSet.getString("plano_jogo"),
                    conduta = resultSet.getString("conduta"),
                    instrucaoAtaque = resultSet.getString("instrucao_ataque"),
                    instrucaoDefesa = resultSet.getString("instrucao_defesa"),
                    instrucaoMeio = resultSet.getString("instrucao_meio"),
                    pressao = resultSet.getInt("pressao"),
                    estilo = resultSet.getInt("estilo"),
                    tempo = resultSet.getInt("tempo")
                )
                println("[DEBUG] Tática encontrada: $dto")
                call.respond(HttpStatusCode.OK, dto)
            } else {
                println("[DEBUG] Nenhuma tática encontrada com id: $id")
                call.respond(HttpStatusCode.NotFound, "Tática não encontrada")
            }
        }

        // PUT /taticas/{id} - atualiza tática existente
        put("/taticas/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            println("[DEBUG] PUT /taticas/{id} chamado com id: $id")
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@put
            }
            val rawBody = call.receiveText()
            println("[DEBUG] Corpo recebido em PUT /taticas/{id}: $rawBody")
            val dto = kotlinx.serialization.json.Json.decodeFromString<com.gameplan.dto.TaticaDTO>(rawBody)
            val statement = dbConnection.prepareStatement(
                "UPDATE Tatica SET plano_jogo = ?, conduta = ?, instrucao_ataque = ?, instrucao_defesa = ?, instrucao_meio = ?, pressao = ?, estilo = ?, tempo = ? WHERE id = ?"
            )
            statement.setString(1, dto.planoJogo)
            statement.setString(2, dto.conduta)
            statement.setString(3, dto.instrucaoAtaque)
            statement.setString(4, dto.instrucaoDefesa)
            statement.setString(5, dto.instrucaoMeio)
            statement.setInt(6, dto.pressao)
            statement.setInt(7, dto.estilo)
            statement.setInt(8, dto.tempo)
            statement.setInt(9, id)
            val rows = statement.executeUpdate()
            println("[DEBUG] Linhas afetadas na atualização da tática: $rows")
            if (rows > 0) {
                // Retorna o DTO atualizado com o id
                val responseDto = com.gameplan.dto.TaticaDTO(
                    id = id,
                    planoJogo = dto.planoJogo,
                    conduta = dto.conduta,
                    instrucaoAtaque = dto.instrucaoAtaque,
                    instrucaoDefesa = dto.instrucaoDefesa,
                    instrucaoMeio = dto.instrucaoMeio,
                    pressao = dto.pressao,
                    estilo = dto.estilo,
                    tempo = dto.tempo
                )
                call.respond(HttpStatusCode.OK, responseDto)
            } else {
                call.respond(HttpStatusCode.NotFound, "Tática não encontrada para atualização")
            }
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

        // Login para Técnico
        post("/login") {
            try {
                val rawBody = call.receiveText()
                println("[DEBUG] Corpo recebido em /login: $rawBody")
                val json = kotlinx.serialization.json.Json.decodeFromString<Map<String, String>>(rawBody)
                val email = json["email"]
                val senha = json["senha"]
                println("[DEBUG] Email recebido: '$email', Senha recebida: '$senha'")
                if (email == null || senha == null) {
                    call.respond(HttpStatusCode.BadRequest, "Email e senha são obrigatórios.")
                    return@post
                }
                val statement = dbConnection.prepareStatement("SELECT * FROM Tecnico WHERE email = ? AND senha = ?")
                statement.setString(1, email)
                statement.setString(2, senha)
                val resultSet = statement.executeQuery()
                if (resultSet.next()) {
                    val tecnicoId = resultSet.getInt("id")
                    // Buscar o id do time comandado por esse técnico
                    val timeStmt = dbConnection.prepareStatement("SELECT id FROM Team WHERE tecnico = ?")
                    timeStmt.setInt(1, tecnicoId)
                    val timeResult = timeStmt.executeQuery()
                    var timeId: Int? = null
                    if (timeResult.next()) {
                        timeId = timeResult.getInt("id")
                    }
                    val dto = com.gameplan.dto.TecnicoDTO(
                        id = tecnicoId,
                        nome = resultSet.getString("nome"),
                        nacionalidade = resultSet.getString("nacionalidade"),
                        dataNascimento = resultSet.getString("data_nascimento"),
                        email = resultSet.getString("email"),
                        senha = resultSet.getString("senha")
                    )
                    val loginResponse = com.gameplan.dto.LoginResponseDTO(tecnico = dto, timeId = timeId)
                    call.respond(HttpStatusCode.OK, loginResponse)
                } else {
                    println("[DEBUG] Nenhum técnico encontrado para email='$email' e senha='$senha'")
                    call.respond(HttpStatusCode.Unauthorized, "Email ou senha incorretos.")
                }
            } catch (e: Exception) {
                println("[ERROR] Erro ao processar /login: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Erro ao processar login: ${e.message}")
            }
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
