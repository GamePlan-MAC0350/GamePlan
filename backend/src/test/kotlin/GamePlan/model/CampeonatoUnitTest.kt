package GamePlan.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import GamePlan.model.*

class CampeonatoUnitTest {
    @Test
    fun `adicionar time aumenta contador de inscritos`() {
        val c = Campeonato(1, "Copa Teste", 4, "Troféu", 0, "2025-01-01", "2025-01-10", "2024-12-01", 1)
        val t = mockTime(1, "Time A")
        c.adicionarTime(t)
        assertEquals(1, c.getTimesInscritos())
        assertEquals("Time A", c.getTimes()[0].getNome())
    }

    @Test
    fun `definirPontos calcula corretamente`() {
        val c = Campeonato(1, "Copa Teste", 8, "Troféu", 0, "2025-01-01", "2025-01-10", "2024-12-01", 1)
        c.definirPontos()
        assertEquals(250, c.getPontos())
    }

    @Test
    fun `gerarChaveamento retorna confrontos corretos`() {
        val c = Campeonato(1, "Copa Teste", 4, "Troféu", 0, "2025-01-01", "2025-01-10", "2024-12-01", 1)
        val t1 = mockTime(1, "A")
        val t2 = mockTime(2, "B")
        val t3 = mockTime(3, "C")
        val t4 = mockTime(4, "D")
        c.adicionarTime(t1)
        c.adicionarTime(t2)
        c.adicionarTime(t3)
        c.adicionarTime(t4)
        val chaveamento = c.gerarChaveamento()
        assertEquals(2, chaveamento.size) // 4 times = 2 confrontos na primeira rodada
        val ids = chaveamento.flatMap { listOf(it.first, it.second) }
        assertTrue(ids.containsAll(listOf(1,2,3,4)))
    }
}
