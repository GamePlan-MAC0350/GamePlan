package GamePlan.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import GamePlan.model.*

class JogadorUnitTest {
    @Test
    fun `getters e setters funcionam`() {
        val time = mockTime()
        val j = Jogador(1, "João", 180, "BR", "01/01/2000", 10, "Atacante", "Destro", 5, 2, 10, 900, 1, 0, time)
        assertEquals("João", j.getNome())
        j.setNome("Pedro")
        assertEquals("Pedro", j.getNome())
        j.setAltura(185)
        assertEquals(185, j.getAltura())
        j.setNumeroDaCamisa(9)
        assertEquals(9, j.getNumeroDaCamisa())
        j.setGolsTotais(7)
        assertEquals(7, j.getGolsTotais())
        j.setTime(time)
        assertEquals(time, j.getTime())
    }

    @Test
    fun `calcularIdade retorna idade correta`() {
        val time = mockTime()
        val j = Jogador(1, "João", 180, "BR", "01/01/2000", 10, "Atacante", "Destro", 5, 2, 10, 900, 1, 0, time)
        val idade = j.calcularIdade()
        assertTrue(idade >= 24) // depende do ano atual
    }
}
