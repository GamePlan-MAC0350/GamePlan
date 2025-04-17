package GamePlan.model

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
    private var cartoes_vermelhos: Int
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

    // Exibir dados
    fun exibirDados() {
        println("Jogador: $nome, Posição: $posicao, Número: $numero_da_camisa")
        println("Gols: $gols_totais, Assistências: $assistencias_totais, Partidas: $partidas_jogadas_totais")
        println("Cartões Amarelos: $cartoes_amarelos, Cartões Vermelhos: $cartoes_vermelhos")
    }
}