package GamePlan.model

import GamePlan.model.Jogador
import GamePlan.model.Time

class Campeonato(
    private var id: Int,
    private var numero_times: Int,
    private var premio: String,
    private var pontos: Int,
    private var data_comeco: String,
    private var data_final: String,
    private var data_inscricao: String
) {
    private val times: MutableList<Time> = mutableListOf()
    private var campeao: Time? = null
    private var artilheiro: Jogador? = null
    private var maior_assistente: Jogador? = null

    // Getters
    fun getId(): Int = id
    fun getNumeroTimes(): Int = numero_times
    fun getPremio(): String = premio
    fun getPontos(): Int = pontos
    fun getDataComeco(): String = data_comeco
    fun getDataFinal(): String = data_final
    fun getDataInscricao(): String = data_inscricao
    fun getTimes(): List<Time> = times
    fun getCampeao(): Time? = campeao
    fun getArtilheiro(): Jogador? = artilheiro
    fun getMaiorAssistente(): Jogador? = maior_assistente

    // Setters
    fun setId(novoId: Int) { id = novoId }
    fun setNumeroTimes(novoNumero: Int) { numero_times = novoNumero }
    fun setPremio(novoPremio: String) { premio = novoPremio }
    fun setPontos(novosPontos: Int) { pontos = novosPontos }
    fun setDataComeco(novaData: String) { data_comeco = novaData }
    fun setDataFinal(novaData: String) { data_final = novaData }
    fun setDataInscricao(novaData: String) { data_inscricao = novaData }
    fun setCampeao(novoCampeao: Time?) { campeao = novoCampeao }
    fun setArtilheiro(novoArtilheiro: Jogador?) { artilheiro = novoArtilheiro }
    fun setMaiorAssistente(novoAssistente: Jogador?) { maior_assistente = novoAssistente }

    // Método para adicionar times ao campeonato
    fun adicionarTime(time: Time) {
        if (times.size < numero_times) {
            times.add(time)
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

    // Método para exibir informações do campeonato
    fun exibirCampeonato() {
        println("Campeonato ID: $id")
        println("Número de times: $numero_times")
        println("Prêmio: $premio")
        println("Pontos: $pontos")
        println("Datas: $data_comeco até $data_final")
        println("Inscrições até: $data_inscricao")
        println("Times participantes:")
        listarTimes()
        println("Campeão: ${campeao?.getNome() ?: "A definir"}")
        println("Artilheiro: ${artilheiro?.getNome() ?: "A definir"}")
        println("Maior Assistente: ${maior_assistente?.getNome() ?: "A definir"}")
    }
}
