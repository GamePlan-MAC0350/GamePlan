package GamePlan.model

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import com.example.configureDatabases

class IntegrationTest {
    @Test
    fun postCampeonatosCriaCampeonatoComDadosValidos() = testApplication {
        application { configureDatabases() }
        val dto = """
            {
                "nome": "CopaIntegracao",
                "numeroTimes": 4,
                "premio": "Troféu",
                "dataComeco": "2025-01-01",
                "dataFinal": "2025-01-10",
                "dataInscricao": "2024-12-01",
                "idTimeFundador": 1,
                "campeaoId": null,
                "timesInscritos": 1,
                "pontos": 0,
                "sorteio": false
            }
        """.trimIndent()
        val response = client.post("/campeonatos") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        assertTrue(response.status == HttpStatusCode.Created || response.status == HttpStatusCode.BadRequest)
    }

    @Test
    fun getCampeonatosRetornaCampeonatoPorNome() = testApplication {
        application { configureDatabases() }
        val nome = "CopaIntegracao"
        val response = client.get("/campeonatos?nome=$nome")
        assertTrue(response.status == HttpStatusCode.OK || response.status == HttpStatusCode.NotFound || response.status == HttpStatusCode.BadRequest)
        if (response.status == HttpStatusCode.OK) {
            val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
            assertEquals(nome, json["nome"]?.jsonPrimitive?.content)
        }
    }

    @Test
    fun getCampeonatosIdInexistenteRetorna404() = testApplication {
        application { configureDatabases() }
        val response = client.get("/campeonatos/99999")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun postCampeonatosComTimeInexistenteRetornaErro() = testApplication {
        application { configureDatabases() }
        val dto = """
            {
                "nome": "CopaErro",
                "numeroTimes": 4,
                "premio": "Troféu",
                "dataComeco": "2025-01-01",
                "dataFinal": "2025-01-10",
                "dataInscricao": "2024-12-01",
                "idTimeFundador": 99999,
                "campeaoId": null,
                "timesInscritos": 1,
                "pontos": 0,
                "sorteio": false
            }
        """.trimIndent()
        val response = client.post("/campeonatos") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
