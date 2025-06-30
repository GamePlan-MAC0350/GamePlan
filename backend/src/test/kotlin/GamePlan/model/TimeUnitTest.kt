package GamePlan.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import GamePlan.model.*

class TimeUnitTest {
    @Test
    fun `adicionar, remover e buscar jogador`() {
        val time = mockTime()
        val jogador = mockJogador()
        time.adicionarJogador(jogador)
        assertEquals(1, time.getJogadores().size)
        assertEquals(jogador, time.buscarJogadorPorNome(jogador.getNome()))
        assertTrue(time.removerJogadorPorNome(jogador.getNome()))
        assertEquals(0, time.getJogadores().size)
    }

    @Test
    fun `jogador com mais gols e assistências`() {
        val time = mockTime()
        val j1 = Jogador(2, "A", 180, "BR", "01/01/2000", 10, "Atacante", "Destro", 10, 2, 10, 900, 1, 0, time)
        val j2 = Jogador(3, "B", 180, "BR", "01/01/2000", 11, "Meia", "Canhoto", 5, 5, 10, 900, 1, 0, time)
        time.adicionarJogador(j1)
        time.adicionarJogador(j2)
        assertEquals(j1, time.jogadorComMaisGols())
        assertEquals(j2, time.jogadorComMaisAssistencias())
        assertEquals(j2, time.jogadorComMaisParticipacoesEmGols())
    }
}
