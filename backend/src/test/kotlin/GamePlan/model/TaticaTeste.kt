package GamePlan.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TaticaTest {

    private lateinit var tatica: Tatica

    @BeforeEach
    fun setUp() {
        tatica = Tatica(
            id = 1,
            plano_de_jogo = "Ofensivo",
            conduta = "Agressiva",
            instrucao_ataque = "Pressionar alto",
            instrucao_meio = "Controle de bola",
            instrucao_defesa = "Linha alta",
            pressao = 8,
            estilo = 7,
            tempo = 90
        )
    }

    @Test
    fun testGetters() {
        assertEquals(1, tatica.getId())
        assertEquals("Ofensivo", tatica.getPlanoDeJogo())
        assertEquals("Agressiva", tatica.getConduta())
        assertEquals("Pressionar alto", tatica.getInstrucaoAtaque())
        assertEquals("Controle de bola", tatica.getInstrucaoMeio())
        assertEquals("Linha alta", tatica.getInstrucaoDefesa())
        assertEquals(8, tatica.getPressao())
        assertEquals(7, tatica.getEstilo())
        assertEquals(90, tatica.getTempo())
    }

    @Test
    fun testSetters() {
        tatica.setId(2)
        tatica.setPlanoDeJogo("Defensivo")
        tatica.setConduta("Reativa")
        tatica.setInstrucaoAtaque("Contra-ataque")
        tatica.setInstrucaoMeio("Marcação individual")
        tatica.setInstrucaoDefesa("Retranca")
        tatica.setPressao(5)
        tatica.setEstilo(3)
        tatica.setTempo(45)

        assertEquals(2, tatica.getId())
        assertEquals("Defensivo", tatica.getPlanoDeJogo())
        assertEquals("Reativa", tatica.getConduta())
        assertEquals("Contra-ataque", tatica.getInstrucaoAtaque())
        assertEquals("Marcação individual", tatica.getInstrucaoMeio())
        assertEquals("Retranca", tatica.getInstrucaoDefesa())
        assertEquals(5, tatica.getPressao())
        assertEquals(3, tatica.getEstilo())
        assertEquals(45, tatica.getTempo())
    }
}
