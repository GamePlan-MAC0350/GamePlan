package GamePlan.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import GamePlan.model.*

class PartidaUnitTest {
    @Test
    fun `getters e setters funcionam`() {
        val time1 = mockTime(1, "Time A")
        val time2 = mockTime(2, "Time B")
        val p = Partida(1, time1, time2, 2, 1, "Estádio", "2025-01-01", "16:00", false, 0, 0, time1)
        assertEquals("Time B", p.getTime2().getNome())
        p.setGolsTime1(3)
        assertEquals(3, p.getGolsTime1())
        p.setEmpate(true)
        assertEquals(true, p.isEmpate())
        p.setTimeVencedor(time2)
        assertEquals(time2, p.getTimeVencedor())
    }
}
