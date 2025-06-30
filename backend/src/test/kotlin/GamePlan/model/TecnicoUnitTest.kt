package GamePlan.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import GamePlan.model.*

class TecnicoUnitTest {
    @Test
    fun `getters e setters funcionam`() {
        val t = Tecnico(1, "Tite", "BR", "01/01/1960", "tite@email.com", "senha123")
        assertEquals("Tite", t.getNome())
        t.setNome("Abel")
        assertEquals("Abel", t.getNome())
        t.setNacionalidade("PT")
        assertEquals("PT", t.getNacionalidade())
        t.setEmail("abel@email.com")
        assertEquals("abel@email.com", t.getEmail())
        t.setSenha("novaSenha")
        assertEquals("novaSenha", t.getSenha())
    }
}
