package GamePlan.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.time.LocalDate

class JogadorTest {

    @Test
    fun `deve criar um jogador corretamente e acessar seus atributos`() {
        val jogador = Jogador(
            id = 1,
            nome = "Pelé",
            altura = 170,
            nacionalidade = "Brasileiro",
            data_nascimento = "23/10/1940",
            numero_da_camisa = 10,
            posicao = "Atacante",
            pe_dominante = "Destro",
            gols_totais = 1000,
            assistencias_totais = 300,
            partidas_jogadas_totais = 800,
            minutos_jogados_totais = 70000,
            cartoes_amarelos = 10,
            cartoes_vermelhos = 2
        )

        assertEquals(1, jogador.getId())
        assertEquals("Pelé", jogador.getNome())
        assertEquals(170, jogador.getAltura())
        assertEquals("Brasileiro", jogador.getNacionalidade())
        assertEquals("23/10/1940", jogador.getDataNascimento())
        assertEquals(10, jogador.getNumeroDaCamisa())
        assertEquals("Atacante", jogador.getPosicao())
        assertEquals("Destro", jogador.getPeDominante())
        assertEquals(1000, jogador.getGolsTotais())
        assertEquals(300, jogador.getAssistenciasTotais())
        assertEquals(800, jogador.getPartidasJogadasTotais())
        assertEquals(70000, jogador.getMinutosJogadosTotais())
        assertEquals(10, jogador.getCartoesAmarelos())
        assertEquals(2, jogador.getCartoesVermelhos())
    }

    @Test
    fun `deve alterar atributos usando setters`() {
        val jogador = Jogador(
            id = 2,
            nome = "Jogador Antigo",
            altura = 180,
            nacionalidade = "Argentina",
            data_nascimento = "01/01/1980",
            numero_da_camisa = 9,
            posicao = "Meia",
            pe_dominante = "Canhoto",
            gols_totais = 500,
            assistencias_totais = 200,
            partidas_jogadas_totais = 600,
            minutos_jogados_totais = 50000,
            cartoes_amarelos = 20,
            cartoes_vermelhos = 1
        )

        jogador.setNome("Messi")
        jogador.setAltura(169)
        jogador.setNumeroDaCamisa(10)
        jogador.setPosicao("Atacante")
        jogador.setGolsTotais(800)

        assertEquals("Messi", jogador.getNome())
        assertEquals(169, jogador.getAltura())
        assertEquals(10, jogador.getNumeroDaCamisa())
        assertEquals("Atacante", jogador.getPosicao())
        assertEquals(800, jogador.getGolsTotais())
    }

    @Test
    fun `deve exibir dados corretamente`() {
        val jogador = Jogador(
            id = 3,
            nome = "Cristiano Ronaldo",
            altura = 187,
            nacionalidade = "Português",
            data_nascimento = "05/02/1985",
            numero_da_camisa = 7,
            posicao = "Atacante",
            pe_dominante = "Destro",
            gols_totais = 850,
            assistencias_totais = 220,
            partidas_jogadas_totais = 1100,
            minutos_jogados_totais = 90000,
            cartoes_amarelos = 70,
            cartoes_vermelhos = 11
        )

        // Capturar o output do println
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        jogador.exibirDados()

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Jogador: Cristiano Ronaldo, Posição: Atacante, Número: 7"))
        assertTrue(output.contains("Gols: 850, Assistências: 220, Partidas: 1100"))
        assertTrue(output.contains("Cartões Amarelos: 70, Cartões Vermelhos: 11"))
    }

    @Test
    fun `deve calcular a idade corretamente`() {
        // Data de nascimento no formato dd/MM/yyyy
        val jogador = Jogador(
            id = 4,
            nome = "Jogador Teste",
            altura = 180,
            nacionalidade = "Teste",
            data_nascimento = "01/01/2000",
            numero_da_camisa = 99,
            posicao = "Zagueiro",
            pe_dominante = "Direito",
            gols_totais = 0,
            assistencias_totais = 0,
            partidas_jogadas_totais = 0,
            minutos_jogados_totais = 0,
            cartoes_amarelos = 0,
            cartoes_vermelhos = 0
        )

        // Calcula a idade esperado com base na data atual do sistema
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val dataNasc = java.time.LocalDate.parse(jogador.getDataNascimento(), formatter)
        val hoje = LocalDate.now()
        val idadeEsperada = java.time.Period.between(dataNasc, hoje).years

        assertEquals(idadeEsperada, jogador.calcularIdade())
    }
}
