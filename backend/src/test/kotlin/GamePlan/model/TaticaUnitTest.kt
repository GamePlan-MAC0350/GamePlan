package GamePlan.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import GamePlan.model.*

class TaticaUnitTest {
    @Test
    fun `getters e setters funcionam`() {
        val t = Tatica(1, "4-4-2", "Ofensiva", "Ataque rápido", "Meio compacto", "Defesa alta", 5, 2, 90)
        assertEquals("4-4-2", t.getPlanoDeJogo())
        t.setPlanoDeJogo("3-5-2")
        assertEquals("3-5-2", t.getPlanoDeJogo())
        t.setConduta("Defensiva")
        assertEquals("Defensiva", t.getConduta())
        t.setInstrucaoAtaque("Posse de bola")
        assertEquals("Posse de bola", t.getInstrucaoAtaque())
        t.setPressao(7)
        assertEquals(7, t.getPressao())
        t.setTempo(80)
        assertEquals(80, t.getTempo())
    }
}
