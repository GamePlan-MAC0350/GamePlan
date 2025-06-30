package GamePlan.model

import GamePlan.model.Jogador
import GamePlan.model.Time

const val PONTUACAO_MAXIMA = 2000
const val NUM_MAX_TIMES = 128

class Campeonato(
    private var id: Int,
    private var nome: String, // NOVO CAMPO
    private var numero_times: Int,
    private var premio: String,
    private var pontos: Int,
    private var data_comeco: String,
    private var data_final: String,
    private var data_inscricao: String,
    private var id_time_fundador: Int // NOVO CAMPO
) {
    private val times: MutableList<Time> = mutableListOf()
    private var campeao: Time? = null
    private var times_inscritos: Int = 0 // NOVA VARIÁVEL

    // Getters
    fun getId(): Int = id
    fun getNome(): String = nome // NOVO GETTER
    fun getNumeroTimes(): Int = numero_times
    fun getPremio(): String = premio
    fun getPontos(): Int = pontos
    fun getDataComeco(): String = data_comeco
    fun getDataFinal(): String = data_final
    fun getDataInscricao(): String = data_inscricao
    fun getTimes(): List<Time> = times
    fun getCampeao(): Time? = campeao
    fun getTimesInscritos(): Int = times_inscritos // GETTER NOVO
    fun getIdTimeFundador(): Int = id_time_fundador

    // Setters
    fun setId(novoId: Int) { id = novoId }
    fun setNome(novoNome: String) { nome = novoNome } // NOVO SETTER
    fun setNumeroTimes(novoNumero: Int) { numero_times = novoNumero }
    fun setPremio(novoPremio: String) { premio = novoPremio }
    fun setPontos(novosPontos: Int) { pontos = novosPontos }
    fun setDataComeco(novaData: String) { data_comeco = novaData }
    fun setDataFinal(novaData: String) { data_final = novaData }
    fun setDataInscricao(novaData: String) { data_inscricao = novaData }
    fun setCampeao(novoCampeao: Time?) { campeao = novoCampeao }
    fun setTimesInscritos(novoValor: Int) { times_inscritos = novoValor } // SETTER NOVO
    fun setIdTimeFundador(novoId: Int) { id_time_fundador = novoId }

    // Método para adicionar times ao campeonato
    fun adicionarTime(time: Time) {
        if (times.size < numero_times) {
            times.add(time)
            times_inscritos = times.size // Atualiza o contador
            println("Time ${time.getNome()} adicionado ao campeonato!")
        } else {
            println("O campeonato já atingiu o número máximo de times!")
        }
    }

    // Método para listar os times inscritos
    fun listarTimes() {
        println("Times inscritos no campeonato:")
        times.forEach { println("- ${it.getNome()}") }
    }

    fun definirPontos() {
        setPontos(numero_times / NUM_MAX_TIMES * PONTUACAO_MAXIMA)
    }

    fun ordenaTimes(): List<Time> {
        return times.sortedByDescending { it.getPontos() }
    }

    fun gerarChaveamento(): List<Pair<Time, Time>> {
        val chaveamento = mutableListOf<Pair<Time, Time>>()
        val timesOrdenados = ordenaTimes() // Usa os times já ordenados

        var i = 0
        var j = timesOrdenados.size - 1

        while (i < j) {
            val confronto = Pair(timesOrdenados[i], timesOrdenados[j])
            chaveamento.add(confronto)
            i++
            j--
        }

        return chaveamento
    }

    // Método para exibir informações do campeonato
    fun exibirCampeonato() {
        println("Campeonato ID: $id")
        println("Nome do Campeonato: $nome") // Exibe o nome do campeonato
        println("Número de times: $numero_times")
        println("Prêmio: $premio")
        println("Pontos: $pontos")
        println("Datas: $data_comeco até $data_final")
        println("Inscrições até: $data_inscricao")
        println("Times participantes:")
        listarTimes()
        println("Campeão: ${campeao?.getNome() ?: "A definir"}")
    }
}
