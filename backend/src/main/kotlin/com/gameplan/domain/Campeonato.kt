package GamePlan.model

import GamePlan.model.Jogador
import GamePlan.model.Time

const val PONTUACAO_MAXIMA = 2000
const val NUM_MAX_TIMES = 64

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
        val pontosCalculados = numero_times * PONTUACAO_MAXIMA / NUM_MAX_TIMES
        println("[DEBUG] definirPontos: numero_times=$numero_times, pontosCalculados=$pontosCalculados")
        setPontos(pontosCalculados)
    }

    fun ordenaTimes(): List<Time> {
        return times.sortedByDescending { it.getPontos() }
    }

    fun gerarChaveamento(): List<Triple<Int, Int, Int>> {
        // Classe interna para o nó da árvore
        class No(val time: Time? = null) {
            var esq: No? = null
            var dir: No? = null
        }

        // Função recursiva para construir a árvore
        fun construir(times: List<Time>): No {
            if (times.size == 1) return No(times[0])
            val esquerda = construir(times.filterIndexed { idx, _ -> idx % 2 == 0 })
            val direita = construir(times.filterIndexed { idx, _ -> idx % 2 == 1 })
            val pai = No()
            pai.esq = esquerda
            pai.dir = direita
            return pai
        }

        val confrontos = mutableListOf<Triple<Int, Int, Int>>()
        var ordem = 1

        // Função recursiva para gerar as partidas
        fun gerarPartidas(no: No?): No? {
            if (no == null) return null
            if (no.esq == null && no.dir == null) return no
            val esq = gerarPartidas(no.esq)
            val dir = gerarPartidas(no.dir)
            if (esq?.time != null && dir?.time != null) {
                confrontos.add(Triple(esq.time.getId(), dir.time.getId(), ordem))
                ordem++
            }
            return No() // nó interno
        }

        val timesOrdenados = ordenaTimes()
        if (timesOrdenados.size < 2) return emptyList() // Não há confrontos
        val raiz = construir(timesOrdenados)
        gerarPartidas(raiz)
        return confrontos
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
