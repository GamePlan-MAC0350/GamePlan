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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull

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

@kotlinx.serialization.Serializable
data class CampeonatoComTimeFundadorDTO(
    val id: Int, // ADICIONADO
    val nome: String,
    val numeroTimes: Int,
    val premio: String,
    val pontos: Int,
    val dataComeco: String,
    val dataFinal: String,
    val dataInscricao: String,
    val timesInscritos: Int,
    val nomeTimeFundador: String?,
    val sorteio: Boolean // ADICIONADO
)

@kotlinx.serialization.Serializable
data class PartidaSimplesDTO(
    val id: Int,
    val time1Id: Int,
    val time2Id: Int,
    val numeroPartida: Int,
    val time1Nome: String,
    val time2Nome: String,
    val golsTime1: Int?,
    val golsTime2: Int?,
    val vencedorId: Int?,
    val penaltisTime1: Int?,
    val penaltisTime2: Int?
)

fun Application.configureDatabases() {
    val dbConnection: Connection = connectToPostgres(embedded = false)

    routing {
        // CRUD para Campeonato
        post("/campeonatos") {
            try {
                val rawBody = call.receiveText()
                println("[DEBUG] Corpo recebido em /campeonatos: $rawBody")
                val dto = kotlinx.serialization.json.Json.decodeFromString<com.gameplan.dto.CampeonatoDTO>(rawBody)
                println("[DEBUG] DTO recebido: $dto")
                // Verifica se o time já possui campeonato
                val checkStmt = dbConnection.prepareStatement("SELECT campeonato FROM Team WHERE id = ?")
                checkStmt.setInt(1, dto.idTimeFundador)
                val checkResult = checkStmt.executeQuery()
                if (checkResult.next()) {
                    val campeonatoExistente = checkResult.getObject("campeonato") as? Int
                    if (campeonatoExistente != null) {
                        call.respond(HttpStatusCode.BadRequest, "O time já pertence a um campeonato.")
                        return@post
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Time fundador não encontrado.")
                    return@post
                }
                // Cria o objeto Campeonato para calcular a pontuação
                val campeonatoTemp = GamePlan.model.Campeonato(
                    id = 0, // será gerado pelo banco
                    nome = dto.nome,
                    numero_times = dto.numeroTimes,
                    premio = dto.premio,
                    pontos = 0, // será definido pelo método
                    data_comeco = dto.dataComeco,
                    data_final = dto.dataFinal,
                    data_inscricao = dto.dataInscricao,
                    id_time_fundador = dto.idTimeFundador
                )
                campeonatoTemp.definirPontos()
                val pontosCalculados = campeonatoTemp.getPontos()
                println("[DEBUG] Pontos calculados para o campeonato: $pontosCalculados (numero_times=${dto.numeroTimes})")
                // Cria o campeonato no banco com a pontuação calculada
                val statement = dbConnection.prepareStatement(
                    "INSERT INTO Campeonato (nome, numero_times, premio, pontos, data_comeco, data_final, data_inscricao, campeao, times_inscritos, id_time_fundador, sorteio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
                )
                statement.setString(1, dto.nome)
                statement.setInt(2, dto.numeroTimes)
                statement.setString(3, dto.premio)
                statement.setInt(4, pontosCalculados)
                statement.setString(5, dto.dataComeco)
                statement.setString(6, dto.dataFinal)
                statement.setString(7, dto.dataInscricao)
                statement.setObject(8, dto.campeaoId)
                statement.setInt(9, 1) // times_inscritos inicia em 1
                statement.setInt(10, dto.idTimeFundador)
                statement.setBoolean(11, dto.sorteio)
                val rows = statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                var campeonatoId: Int? = null
                if (generatedKeys.next()) {
                    campeonatoId = generatedKeys.getInt(1)
                }
                println("[DEBUG] Campeonato inserido (linhas afetadas: $rows, id: $campeonatoId)")
                // Atualiza o campo campeonato do time fundador
                if (campeonatoId != null) {
                    val updateStmt = dbConnection.prepareStatement("UPDATE Team SET campeonato = ? WHERE id = ?")
                    updateStmt.setInt(1, campeonatoId)
                    updateStmt.setInt(2, dto.idTimeFundador)
                    updateStmt.executeUpdate()
                }
                call.respond(HttpStatusCode.Created)
            } catch (e: Exception) {
                println("[ERRO] Falha ao criar campeonato: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Erro ao criar campeonato: ${e.message}")
            }
        }

        get("/campeonatos/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val statement = dbConnection.prepareStatement("SELECT * FROM Campeonato WHERE id = ?")
            statement.setInt(1, id)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val dto = CampeonatoDTO(
                    id = resultSet.getInt("id"),
                    nome = resultSet.getString("nome"), // NOVO CAMPO
                    numeroTimes = resultSet.getInt("numero_times"),
                    premio = resultSet.getString("premio"),
                    pontos = resultSet.getInt("pontos"),
                    dataComeco = resultSet.getString("data_comeco"),
                    dataFinal = resultSet.getString("data_final"),
                    dataInscricao = resultSet.getString("data_inscricao"),
                    campeaoId = resultSet.getObject("campeao") as? Int,
                    timesInscritos = resultSet.getInt("times_inscritos"),
                    idTimeFundador = resultSet.getInt("id_time_fundador"),
                    sorteio = resultSet.getBoolean("sorteio")
                )
                call.respond(HttpStatusCode.OK, dto)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // GET /campeonatos?nome=... - busca campeonato pelo nome (prêmio)
        get("/campeonatos") {
            val nome = call.request.queryParameters["nome"]
            if (nome == null) {
                call.respond(HttpStatusCode.BadRequest, "Nome do campeonato não informado")
                return@get
            }
            val statement = dbConnection.prepareStatement("SELECT * FROM Campeonato WHERE nome = ?")
            statement.setString(1, nome)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val idTimeFundador = resultSet.getInt("id_time_fundador")
                // Buscar nome do time fundador
                var nomeTimeFundador: String? = null
                val timeStmt = dbConnection.prepareStatement("SELECT nome FROM Team WHERE id = ?")
                timeStmt.setInt(1, idTimeFundador)
                val timeResult = timeStmt.executeQuery()
                if (timeResult.next()) {
                    nomeTimeFundador = timeResult.getString("nome")
                }
                val dto = CampeonatoComTimeFundadorDTO(
                    id = resultSet.getInt("id"), // ADICIONADO
                    nome = resultSet.getString("nome"),
                    numeroTimes = resultSet.getInt("numero_times"),
                    premio = resultSet.getString("premio"),
                    pontos = resultSet.getInt("pontos"),
                    dataComeco = resultSet.getString("data_comeco"),
                    dataFinal = resultSet.getString("data_final"),
                    dataInscricao = resultSet.getString("data_inscricao"),
                    timesInscritos = resultSet.getInt("times_inscritos"),
                    nomeTimeFundador = nomeTimeFundador,
                    sorteio = resultSet.getBoolean("sorteio") // ADICIONADO
                )
                call.respond(HttpStatusCode.OK, dto)
            } else {
                call.respond(HttpStatusCode.NotFound, "Campeonato não encontrado")
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
                val campeonatoObj = resultSet.getObject("campeonato")
                val campeonatoId = when (campeonatoObj) {
                    is Int -> campeonatoObj
                    is Long -> campeonatoObj.toInt()
                    is Number -> campeonatoObj.toInt()
                    is String -> campeonatoObj.toIntOrNull()
                    else -> null
                }
                val dto = com.gameplan.dto.TimeDTO(
                    id = resultSet.getInt("id"),
                    nome = resultSet.getString("nome"),
                    nacionalidade = resultSet.getString("nacionalidade"),
                    dataFundacao = resultSet.getString("data_fundacao"),
                    artilheiroId = resultSet.getObject("artilheiro") as? Int,
                    maiorAssistenteId = resultSet.getObject("maior_assistente") as? Int,
                    partidasJogadas = resultSet.getInt("partidas_jogadas_totais"),
                    golsMarcados = resultSet.getInt("gols_marcados"),
                    golsSofridos = resultSet.getInt("gols_sofridos"),
                    pontos = resultSet.getInt("pontos"),
                    vitorias = resultSet.getInt("vitorias"),
                    derrotas = resultSet.getInt("derrotas"),
                    taticaId = resultSet.getObject("tatica") as? Int,
                    tecnicoId = resultSet.getObject("tecnico") as? Int,
                    campeonatoId = campeonatoId
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
            val statement = dbConnection.prepareStatement("INSERT INTO Partida (time_1, time_2, gols_time_1, gols_time_2, lugar, data_partida, hora_partida, empate, gols_time_1_penaltis, gols_time_2_penaltis, vencedor, campeonato, numero_partida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
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
            statement.setObject(13, dto.numeroPartida)
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

        // Inscrever time em campeonato
        post("/campeonatos/{id}/inscrever-time") {
            val campeonatoId = call.parameters["id"]?.toIntOrNull()
            val rawBody = call.receiveText()
            val json = kotlinx.serialization.json.Json.decodeFromString<Map<String, Int>>(rawBody)
            val timeId = json["timeId"]
            if (campeonatoId == null || timeId == null) {
                call.respond(HttpStatusCode.BadRequest, "ID do campeonato e do time são obrigatórios.")
                return@post
            }
            // Buscar dados do campeonato
            val campStmt = dbConnection.prepareStatement("SELECT numero_times, times_inscritos FROM Campeonato WHERE id = ?")
            campStmt.setInt(1, campeonatoId)
            val campRes = campStmt.executeQuery()
            if (!campRes.next()) {
                call.respond(HttpStatusCode.NotFound, "Campeonato não encontrado.")
                return@post
            }
            val numeroTimes = campRes.getInt("numero_times")
            val timesInscritos = campRes.getInt("times_inscritos")
            if (timesInscritos >= numeroTimes) {
                call.respond(HttpStatusCode.BadRequest, "Não é possível se inscrever: número máximo de times atingido.")
                return@post
            }
            // Verifica se o time já está inscrito em algum campeonato
            val timeStmt = dbConnection.prepareStatement("SELECT campeonato FROM Team WHERE id = ?")
            timeStmt.setInt(1, timeId)
            val timeRes = timeStmt.executeQuery()
            if (!timeRes.next()) {
                call.respond(HttpStatusCode.NotFound, "Time não encontrado.")
                return@post
            }
            val campeonatoAtual = timeRes.getObject("campeonato")
            if (campeonatoAtual != null) {
                call.respond(HttpStatusCode.BadRequest, "O time já está inscrito em um campeonato.")
                return@post
            }
            // Atualiza o número de times inscritos no campeonato
            val updCampStmt = dbConnection.prepareStatement("UPDATE Campeonato SET times_inscritos = times_inscritos + 1 WHERE id = ?")
            updCampStmt.setInt(1, campeonatoId)
            updCampStmt.executeUpdate()
            // Atualiza o campo campeonato do time
            val updTimeStmt = dbConnection.prepareStatement("UPDATE Team SET campeonato = ? WHERE id = ?")
            updTimeStmt.setInt(1, campeonatoId)
            updTimeStmt.setInt(2, timeId)
            updTimeStmt.executeUpdate()
            call.respond(HttpStatusCode.OK, "Time inscrito com sucesso!")
        }

        // SORTEIO/CHAVEAMENTO: Gera confrontos e registra partidas no banco
        post("/campeonatos/{id}/sorteio") {
            val campeonatoId = call.parameters["id"]?.toIntOrNull()
            if (campeonatoId == null) {
                call.respond(HttpStatusCode.BadRequest, "ID do campeonato inválido.")
                return@post
            }
            // Busca todos os times do campeonato
            val timesStmt = dbConnection.prepareStatement("SELECT id, pontos FROM Team WHERE campeonato = ?")
            timesStmt.setInt(1, campeonatoId)
            val timesRes = timesStmt.executeQuery()
            val timesList = mutableListOf<Pair<Int, Int>>() // Pair<id, pontos>
            while (timesRes.next()) {
                timesList.add(Pair(timesRes.getInt("id"), timesRes.getInt("pontos")))
            }
            if (timesList.size < 2) {
                call.respond(HttpStatusCode.BadRequest, "É necessário pelo menos 2 times para o sorteio.")
                return@post
            }
            // Ordena por pontos decrescente
            val timesOrdenados = timesList.sortedByDescending { it.second }.map { it.first }
            // Gera chaveamento (usando a lógica já implementada em gerarChaveamento)
            // Aqui, para simplificar, vamos gerar as triplas diretamente
            class No(val timeId: Int? = null) {
                var esq: No? = null
                var dir: No? = null
            }
            fun construir(times: List<Int>): No {
                if (times.size == 1) return No(times[0])
                val esquerda = construir(times.filterIndexed { idx, _ -> idx % 2 == 0 })
                val direita = construir(times.filterIndexed { idx, _ -> idx % 2 == 1 })
                val pai = No()
                pai.esq = esquerda
                pai.dir = direita
                return pai
            }
            val confrontos = mutableListOf<Triple<Int, Int, Int>>()
            var ordem = 1
            fun gerarPartidas(no: No?): No? {
                if (no == null) return null
                if (no.esq == null && no.dir == null) return no
                val esq = gerarPartidas(no.esq)
                val dir = gerarPartidas(no.dir)
                if (esq?.timeId != null && dir?.timeId != null) {
                    confrontos.add(Triple(esq.timeId, dir.timeId, ordem))
                    ordem++
                }
                return No()
            }
            val raiz = construir(timesOrdenados)
            gerarPartidas(raiz)
            // Insere as partidas no banco
            val insertStmt = dbConnection.prepareStatement(
                "INSERT INTO Partida (time_1, time_2, lugar, data_partida, hora_partida, empate, campeonato, numero_partida) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            )
            for ((id1, id2, numeroPartida) in confrontos) {
                insertStmt.setInt(1, id1)
                insertStmt.setInt(2, id2)
                insertStmt.setString(3, "A definir") // lugar
                insertStmt.setString(4, "A definir") // data_partida
                insertStmt.setString(5, "A definir") // hora_partida
                insertStmt.setBoolean(6, false) // empate
                insertStmt.setInt(7, campeonatoId)
                insertStmt.setInt(8, numeroPartida)
                insertStmt.addBatch()
            }
            insertStmt.executeBatch()
            // Atualiza o campo sorteio do campeonato
            val updCampStmt = dbConnection.prepareStatement("UPDATE Campeonato SET sorteio = TRUE WHERE id = ?")
            updCampStmt.setInt(1, campeonatoId)
            updCampStmt.executeUpdate()
            call.respond(HttpStatusCode.OK, "Chaveamento realizado e partidas registradas!")
        }

        // GET /partidas?campeonatoId=...&semVencedor=true - retorna partidas do campeonato sem vencedor
        get("/partidas") {
            val campeonatoId = call.request.queryParameters["campeonatoId"]?.toIntOrNull()
            val semVencedor = call.request.queryParameters["semVencedor"] == "true"
            if (campeonatoId == null) {
                call.respond(HttpStatusCode.BadRequest, "campeonatoId obrigatório")
                return@get
            }
            val query = StringBuilder("SELECT p.id, p.time_1, p.time_2, p.numero_partida, t1.nome as time1_nome, t2.nome as time2_nome, p.gols_time_1, p.gols_time_2, p.vencedor, p.gols_time_1_penaltis, p.gols_time_2_penaltis FROM Partida p ")
            query.append("JOIN Team t1 ON p.time_1 = t1.id ")
            query.append("JOIN Team t2 ON p.time_2 = t2.id ")
            query.append("WHERE p.campeonato = ? ")
            if (semVencedor) query.append("AND p.vencedor IS NULL ")
            query.append("ORDER BY p.numero_partida ASC")
            val stmt = dbConnection.prepareStatement(query.toString())
            stmt.setInt(1, campeonatoId)
            val rs = stmt.executeQuery()
            val partidas = mutableListOf<PartidaSimplesDTO>()
            while (rs.next()) {
                partidas.add(
                    PartidaSimplesDTO(
                        id = rs.getInt("id"),
                        time1Id = rs.getInt("time_1"),
                        time2Id = rs.getInt("time_2"),
                        numeroPartida = rs.getInt("numero_partida"),
                        time1Nome = rs.getString("time1_nome"),
                        time2Nome = rs.getString("time2_nome"),
                        golsTime1 = rs.getObject("gols_time_1") as? Int,
                        golsTime2 = rs.getObject("gols_time_2") as? Int,
                        vencedorId = rs.getObject("vencedor") as? Int,
                        penaltisTime1 = rs.getObject("gols_time_1_penaltis") as? Int,
                        penaltisTime2 = rs.getObject("gols_time_2_penaltis") as? Int
                    )
                )
            }
            call.respond(HttpStatusCode.OK, partidas)
        }

        // REGISTRA RESULTADO DA PARTIDA
        post("/partidas/{id}/registrar-resultado") {
            val partidaId = call.parameters["id"]?.toIntOrNull()
            if (partidaId == null) {
                call.respond(HttpStatusCode.BadRequest, "ID da partida inválido.")
                return@post
            }
            val rawBody = call.receiveText()
            println("[DEBUG] Corpo recebido em /partidas/{id}/registrar-resultado: $rawBody")
            val json = kotlinx.serialization.json.Json.parseToJsonElement(rawBody).jsonObject
            val golsTime1 = json["golsTime1"]?.jsonPrimitive?.intOrNull ?: 0
            val golsTime2 = json["golsTime2"]?.jsonPrimitive?.intOrNull ?: 0
            val vencedorId = json["vencedorId"]?.jsonPrimitive?.intOrNull
            val penaltisTime1 = json["penaltisTime1"]?.jsonPrimitive?.intOrNull
            val penaltisTime2 = json["penaltisTime2"]?.jsonPrimitive?.intOrNull
            val goleadores1 = json["goleadores1"]?.jsonArray?.mapNotNull { g ->
                val obj = g.jsonObject
                obj["goleador"]?.jsonPrimitive?.contentOrNull
            } ?: emptyList()
            val goleadores2 = json["goleadores2"]?.jsonArray?.mapNotNull { g ->
                val obj = g.jsonObject
                obj["goleador"]?.jsonPrimitive?.contentOrNull
            } ?: emptyList()

            // Atualiza gols dos jogadores
            fun atualizaJogador(nome: String, campo: String) {
                val stmt = dbConnection.prepareStatement("SELECT id FROM Jogador WHERE nome = ?")
                stmt.setString(1, nome)
                val rs = stmt.executeQuery()
                if (!rs.next()) throw IllegalArgumentException("Jogador '$nome' não encontrado!")
                val jogadorId = rs.getInt("id")
                val upd = dbConnection.prepareStatement("UPDATE Jogador SET $campo = $campo + 1 WHERE id = ?")
                upd.setInt(1, jogadorId)
                upd.executeUpdate()
            }
            try {
                for (nomeGoleador in goleadores1) {
                    if (!nomeGoleador.isNullOrBlank()) atualizaJogador(nomeGoleador, "gols_totais")
                }
                for (nomeGoleador in goleadores2) {
                    if (!nomeGoleador.isNullOrBlank()) atualizaJogador(nomeGoleador, "gols_totais")
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Jogador não encontrado!")
                return@post
            }
            // Atualiza partida
            val updPartida = dbConnection.prepareStatement("UPDATE Partida SET gols_time_1 = ?, gols_time_2 = ?, gols_time_1_penaltis = ?, gols_time_2_penaltis = ?, vencedor = ? WHERE id = ?")
            updPartida.setInt(1, golsTime1)
            updPartida.setInt(2, golsTime2)
            updPartida.setInt(3, penaltisTime1 ?: 0)
            updPartida.setInt(4, penaltisTime2 ?: 0)
            if (vencedorId != null) updPartida.setInt(5, vencedorId) else updPartida.setNull(5, java.sql.Types.INTEGER)
            updPartida.setInt(6, partidaId)
            updPartida.executeUpdate()

            // INÍCIO: Incrementa partidas jogadas, gols marcados e sofridos dos times
            val stmtTimes = dbConnection.prepareStatement("SELECT time_1, time_2 FROM Partida WHERE id = ?")
            stmtTimes.setInt(1, partidaId)
            val rsTimes = stmtTimes.executeQuery()
            if (rsTimes.next()) {
                val time1Id = rsTimes.getInt("time_1")
                val time2Id = rsTimes.getInt("time_2")
                // Incrementa partidas jogadas
                val updPJ1 = dbConnection.prepareStatement("UPDATE Team SET partidas_jogadas_totais = partidas_jogadas_totais + 1 WHERE id = ?")
                updPJ1.setInt(1, time1Id)
                updPJ1.executeUpdate()
                val updPJ2 = dbConnection.prepareStatement("UPDATE Team SET partidas_jogadas_totais = partidas_jogadas_totais + 1 WHERE id = ?")
                updPJ2.setInt(1, time2Id)
                updPJ2.executeUpdate()
                // Atualiza gols marcados e sofridos
                val updGM1 = dbConnection.prepareStatement("UPDATE Team SET gols_marcados = gols_marcados + ? , gols_sofridos = gols_sofridos + ? WHERE id = ?")
                updGM1.setInt(1, golsTime1)
                updGM1.setInt(2, golsTime2)
                updGM1.setInt(3, time1Id)
                updGM1.executeUpdate()
                val updGM2 = dbConnection.prepareStatement("UPDATE Team SET gols_marcados = gols_marcados + ? , gols_sofridos = gols_sofridos + ? WHERE id = ?")
                updGM2.setInt(1, golsTime2)
                updGM2.setInt(2, golsTime1)
                updGM2.setInt(3, time2Id)
                updGM2.executeUpdate()
            }
            // FIM: Incrementa partidas jogadas, gols marcados e sofridos dos times

            // Incrementa vitorias do vencedor, derrotas do perdedor e remove campeonato do perdedor
            if (vencedorId != null) {
                // Buscar times da partida
                val stmt = dbConnection.prepareStatement("SELECT time_1, time_2, campeonato, numero_partida FROM Partida WHERE id = ?")
                stmt.setInt(1, partidaId)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    val time1Id = rs.getInt("time_1")
                    val time2Id = rs.getInt("time_2")
                    val campeonatoId = rs.getInt("campeonato")
                    val numeroPartida = rs.getInt("numero_partida")
                    val perdedorId = if (vencedorId == time1Id) time2Id else time1Id
                    // Incrementa vitorias do vencedor
                    val updV = dbConnection.prepareStatement("UPDATE Team SET vitorias = vitorias + 1 WHERE id = ?")
                    updV.setInt(1, vencedorId)
                    updV.executeUpdate()
                    // Incrementa derrotas do perdedor
                    val updD = dbConnection.prepareStatement("UPDATE Team SET derrotas = derrotas + 1 WHERE id = ?")
                    updD.setInt(1, perdedorId)
                    updD.executeUpdate()
                    // Só remove campeonato do perdedor se ele NÃO for o fundador
                    val fundStmt = dbConnection.prepareStatement("SELECT id_time_fundador FROM Campeonato WHERE id = ?")
                    fundStmt.setInt(1, campeonatoId)
                    val fundRes = fundStmt.executeQuery()
                    var idTimeFundador: Int? = null
                    if (fundRes.next()) {
                        idTimeFundador = fundRes.getInt("id_time_fundador")
                    }
                    if (idTimeFundador != null && perdedorId != idTimeFundador) {
                        val updC = dbConnection.prepareStatement("UPDATE Team SET campeonato = NULL WHERE id = ?")
                        updC.setInt(1, perdedorId)
                        updC.executeUpdate()
                    }

                    // --- NOVO: Chaveamento eliminatório automático (GENÉRICO) ---
                    // Função para calcular o número da próxima partida e os jogos anteriores
                    fun proximaPartidaInfo(numeroPartida: Int, totalTimes: Int): Pair<Int, Pair<Int, Int>>? {
                        // totalTimes: 8, 16, 32, ...
                        // Jogos da primeira rodada: 1..(totalTimes/2)
                        // Segunda rodada: (totalTimes/2+1)..(totalTimes/2+totalTimes/4)
                        // ...
                        var rodada = 0
                        var jogosNaRodada = totalTimes / 2
                        var inicioRodada = 1
                        var fimRodada = jogosNaRodada
                        while (numeroPartida > fimRodada) {
                            rodada++
                            jogosNaRodada /= 2
                            inicioRodada = fimRodada + 1
                            fimRodada = inicioRodada + jogosNaRodada - 1
                        }
                        // Se já é a última rodada, não há próxima
                        if (jogosNaRodada == 1) return null
                        // Índice do jogo na rodada (0-based)
                        val idx = numeroPartida - inicioRodada
                        // Próxima partida está na próxima rodada
                        val proxInicio = fimRodada + 1
                        val proxIdx = idx / 2
                        val proxNum = proxInicio + proxIdx
                        // Os dois jogos que formam a próxima partida
                        val jogo1 = inicioRodada + (idx / 2) * 2
                        val jogo2 = jogo1 + 1
                        return Pair(proxNum, Pair(jogo1, jogo2))
                    }
                    // Descobrir o total de times do campeonato
                    val campStmt = dbConnection.prepareStatement("SELECT numero_times FROM Campeonato WHERE id = ?")
                    campStmt.setInt(1, campeonatoId)
                    val campRs = campStmt.executeQuery()
                    var totalTimes = 0
                    if (campRs.next()) totalTimes = campRs.getInt("numero_times")
                    val proxInfo = proximaPartidaInfo(numeroPartida, totalTimes)
                    if (proxInfo != null) {
                        val proxNum = proxInfo.first
                        val jogosAnt = proxInfo.second
                        // Buscar vencedores dos dois jogos anteriores
                        val stmtV = dbConnection.prepareStatement("SELECT numero_partida, vencedor FROM Partida WHERE campeonato = ? AND numero_partida IN (?, ?)")
                        stmtV.setInt(1, campeonatoId)
                        stmtV.setInt(2, jogosAnt.first)
                        stmtV.setInt(3, jogosAnt.second)
                        val rsV = stmtV.executeQuery()
                        val vencedores = mutableMapOf<Int, Int?>()
                        while (rsV.next()) {
                            vencedores[rsV.getInt("numero_partida")] = rsV.getObject("vencedor") as? Int
                        }
                        if (vencedores.size == 2 && vencedores.values.all { it != null }) {
                            // Verifica se a partida já existe
                            val check = dbConnection.prepareStatement("SELECT COUNT(*) FROM Partida WHERE campeonato = ? AND numero_partida = ?")
                            check.setInt(1, campeonatoId)
                            check.setInt(2, proxNum)
                            val checkRs = check.executeQuery()
                            if (checkRs.next() && checkRs.getInt(1) == 0) {
                                // Cria a próxima partida
                                val insert = dbConnection.prepareStatement(
                                    "INSERT INTO Partida (time_1, time_2, lugar, data_partida, hora_partida, empate, campeonato, numero_partida) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                                )
                                insert.setInt(1, vencedores[jogosAnt.first]!!)
                                insert.setInt(2, vencedores[jogosAnt.second]!!)
                                insert.setString(3, "A definir")
                                insert.setString(4, "A definir")
                                insert.setString(5, "A definir")
                                insert.setBoolean(6, false)
                                insert.setInt(7, campeonatoId)
                                insert.setInt(8, proxNum)
                                insert.executeUpdate()
                            }
                        }
                    } else {
                        // Se não há próxima partida, é a final.
                        // Remover todos os times restantes do campeonato (setar campo campeonato = NULL)
                        val updTimes = dbConnection.prepareStatement("UPDATE Team SET campeonato = NULL WHERE campeonato = ?")
                        updTimes.setInt(1, campeonatoId)
                        updTimes.executeUpdate()

                        // Atualizar o campo campeao na tabela Campeonato
                        val updCamp = dbConnection.prepareStatement("UPDATE Campeonato SET campeao = ? WHERE id = ?")
                        updCamp.setInt(1, vencedorId)
                        updCamp.setInt(2, campeonatoId)
                        updCamp.executeUpdate()

                        // Somar os pontos do campeonato ao time vencedor
                        val pontosStmt = dbConnection.prepareStatement("SELECT pontos FROM Campeonato WHERE id = ?")
                        pontosStmt.setInt(1, campeonatoId)
                        val pontosRs = pontosStmt.executeQuery()
                        if (pontosRs.next()) {
                            val pontos = pontosRs.getInt("pontos")
                            val updPontos = dbConnection.prepareStatement("UPDATE Team SET pontos = pontos + ? WHERE id = ?")
                            updPontos.setInt(1, pontos)
                            updPontos.setInt(2, vencedorId)
                            updPontos.executeUpdate()
                        }
                    }
                    // --- FIM chaveamento ---
                }
            }
            call.respond(HttpStatusCode.OK, "Resultado registrado com sucesso!")
        }

        // GET /times/{id}/jogadores-completos - retorna todos os jogadores do time como objetos completos
        get("/times/{id}/jogadores-completos") {
            val idRaw = call.parameters["id"]
            println("[DEBUG] GET /times/{id}/jogadores-completos chamado com id (raw): $idRaw (tipo: ${idRaw?.javaClass})")
            val id = idRaw?.toIntOrNull()
            if (id == null) {
                println("[DEBUG] ID inválido recebido: $idRaw")
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }
            val jogadoresStmt = dbConnection.prepareStatement("SELECT * FROM Jogador WHERE clube = ?")
            jogadoresStmt.setInt(1, id)
            val jogadoresRes = jogadoresStmt.executeQuery()
            val jogadores = mutableListOf<com.gameplan.dto.JogadorDTO>()
            while (jogadoresRes.next()) {
                jogadores.add(
                    com.gameplan.dto.JogadorDTO(
                        id = jogadoresRes.getInt("id"),
                        nome = jogadoresRes.getString("nome"),
                        altura = jogadoresRes.getInt("altura"),
                        nacionalidade = jogadoresRes.getString("nacionalidade"),
                        dataNascimento = jogadoresRes.getString("data_nascimento"),
                        numeroCamisa = jogadoresRes.getInt("numero_camisa"),
                        posicao = jogadoresRes.getString("posicao"),
                        peDominante = jogadoresRes.getString("pe_dominante"),
                        golsTotais = jogadoresRes.getInt("gols_totais"),
                        assistenciasTotais = jogadoresRes.getInt("assistencias_totais"),
                        cartoesAmarelos = jogadoresRes.getInt("cartoes_amarelos"),
                        cartoesVermelhos = jogadoresRes.getInt("cartoes_vermelhos"),
                        clubeId = jogadoresRes.getObject("clube") as? Int
                    )
                )
            }
            println("[DEBUG] Jogadores completos encontrados para o time $id: $jogadores (total: ${jogadores.size})")
            call.respond(HttpStatusCode.OK, jogadores)
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
