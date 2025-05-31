package GamePlan.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.Period

import GamePlan.model.Time

class Jogador (
    private var id: Int,
    private var nome: String,
    private var altura: Int,
    private var nacionalidade: String,
    private var data_nascimento: String,
    private var numero_da_camisa: Int,
    private var posicao: String,
    private var pe_dominante: String,
    private var gols_totais: Int,
    private var assistencias_totais: Int,
    private var partidas_jogadas_totais: Int,
    private var minutos_jogados_totais: Int,
    private var cartoes_amarelos: Int,
    private var cartoes_vermelhos: Int,
    private var time: Time
) {
    // Getters
    fun getId(): Int = id
    fun getNome(): String = nome
    fun getAltura(): Int = altura
    fun getNacionalidade(): String = nacionalidade
    fun getDataNascimento(): String = data_nascimento
    fun getNumeroDaCamisa(): Int = numero_da_camisa
    fun getPosicao(): String = posicao
    fun getPeDominante(): String = pe_dominante
    fun getGolsTotais(): Int = gols_totais
    fun getAssistenciasTotais(): Int = assistencias_totais
    fun getPartidasJogadasTotais(): Int = partidas_jogadas_totais
    fun getMinutosJogadosTotais(): Int = minutos_jogados_totais
    fun getCartoesAmarelos(): Int = cartoes_amarelos
    fun getCartoesVermelhos(): Int = cartoes_vermelhos
    fun getTime(): Time = time

    // Setters
    fun setId(novoId: Int) { id = novoId }
    fun setNome(novoNome: String) { nome = novoNome }
    fun setAltura(novaAltura: Int) { altura = novaAltura }
    fun setNacionalidade(novaNacionalidade: String) { nacionalidade = novaNacionalidade }
    fun setDataNascimento(novaData: String) { data_nascimento = novaData }
    fun setNumeroDaCamisa(novoNumero: Int) { numero_da_camisa = novoNumero }
    fun setPosicao(novaPosicao: String) { posicao = novaPosicao }
    fun setPeDominante(novoPe: String) { pe_dominante = novoPe }
    fun setGolsTotais(novosGols: Int) { gols_totais = novosGols }
    fun setAssistenciasTotais(novasAssistencias: Int) { assistencias_totais = novasAssistencias }
    fun setPartidasJogadasTotais(novasPartidas: Int) { partidas_jogadas_totais = novasPartidas }
    fun setMinutosJogadosTotais(novosMinutos: Int) { minutos_jogados_totais = novosMinutos }
    fun setCartoesAmarelos(novosCartoes: Int) { cartoes_amarelos = novosCartoes }
    fun setCartoesVermelhos(novosCartoes: Int) { cartoes_vermelhos = novosCartoes }
    fun setTime(novoTime: Time) { time = novoTime }

    /**
     * Calcula a idade do jogador com base na data de nascimento.
     *
     * A data de nascimento deve estar no formato "dd/MM/yyyy".
     * A função utiliza a data atual do sistema para calcular a idade em anos completos.
     *
     * @return A idade do jogador em anos.
     * @throws DateTimeParseException se a data de nascimento estiver em um formato inválido.
     */
    fun calcularIdade(): Int {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val dataNasc = LocalDate.parse(data_nascimento, formatter)
        val hoje = LocalDate.now()
        return Period.between(dataNasc, hoje).years
    }

    // Exibir dados
    fun exibirDados() {
        println("Jogador: $nome, Posição: $posicao, Número: $numero_da_camisa")
        println("Time: ${time.getNome()}")
        println("Gols: $gols_totais, Assistências: $assistencias_totais, Partidas: $partidas_jogadas_totais")
        println("Cartões Amarelos: $cartoes_amarelos, Cartões Vermelhos: $cartoes_vermelhos")
    }
}