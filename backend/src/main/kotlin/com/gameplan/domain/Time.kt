package GamePlan.model

import GamePlan.model.Jogador
import GamePlan.model.Tatica
import GamePlan.model.Tecnico

class Time (
    private var id: Int,
    private var nome: String,
    private var nacionalidade: String,
    private var data_fundacao: String,
    private var artilheiro: Jogador,
    private var maior_assistente: Jogador,
    private var partidas_jogadas: Int,
    private var gols_marcados: Int,
    private var gols_sofridos: Int,
    private var vitorias: Int,
    private var derrotas: Int,
    private var pontos: Int,
    private var tatica: Tatica,
    private var tecnico: Tecnico
) {
    // ✅ GETTERS
    fun getId(): Int = id
    fun getNome(): String = nome
    fun getNacionalidade(): String = nacionalidade
    fun getDataFundacao(): String = data_fundacao
    fun getArtilheiro(): Jogador = artilheiro
    fun getMaiorAssistente(): Jogador = maior_assistente
    fun getPartidasJogadas(): Int = partidas_jogadas
    fun getGolsMarcados(): Int = gols_marcados
    fun getGolsSofridos(): Int = gols_sofridos
    fun getVitorias(): Int = vitorias
    fun getDerrotas(): Int = derrotas
    fun getPontos(): Int = pontos
    fun getTatica(): Tatica = tatica
    fun getTecnico(): Tecnico = tecnico

    // ✅ SETTERS
    fun setId(novoId: Int) { id = novoId }
    fun setNome(novoNome: String) { nome = novoNome }
    fun setNacionalidade(novaNacionalidade: String) { nacionalidade = novaNacionalidade }
    fun setDataFundacao(novaData: String) { data_fundacao = novaData }
    fun setArtilheiro(novoArtilheiro: Jogador) { artilheiro = novoArtilheiro }
    fun setMaiorAssistente(novoAssistente: Jogador) { maior_assistente = novoAssistente }
    fun setPartidasJogadas(novasPartidas: Int) { partidas_jogadas = novasPartidas }
    fun setGolsMarcados(novosGols: Int) { gols_marcados = novosGols }
    fun setGolsSofridos(novosGols: Int) { gols_sofridos = novosGols }
    fun setVitorias(novasVitorias: Int) { vitorias = novasVitorias }
    fun setDerrotas(novasDerrotas: Int) { derrotas = novasDerrotas }
    fun setPontos(novosPontos: Int) { pontos = novosPontos }
    fun setTatica(novaTatica: Tatica) { tatica = novaTatica }
    fun setTecnico(novoTecnico: Tecnico) { tecnico = novoTecnico }

    // ✅ Método para exibir os dados do time
    fun exibirTime() {
        println("Time: $nome ($nacionalidade) | Fundado em: $data_fundacao")
        println("Artilheiro: ${artilheiro.getNome()} | Maior Assistente: ${maior_assistente.getNome()}")
        println("Partidas: $partidas_jogadas | Vitórias: $vitorias | Derrotas: $derrotas | Pontos: $pontos")
        println("Gols Marcados: $gols_marcados | Gols Sofridos: $gols_sofridos")
        println("Técnico: ${tecnico.getNome()} | Tática: ${tatica.getPlanoDeJogo()}")
    }
}
