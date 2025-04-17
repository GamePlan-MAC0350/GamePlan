package GamePlan.model

class Tatica (
    private var id: Int,
    private var plano_de_jogo: String,
    private var conduta: String,
    private var instrucao_ataque: String,
    private var instrucao_meio: String,
    private var instrucao_defesa: String,
    private var pressao: Int,
    private var estilo: Int,
    private var tempo: Int
) {
    // Getters
    fun getId(): Int = id
    fun getPlanoDeJogo(): String = plano_de_jogo
    fun getConduta(): String = conduta
    fun getInstrucaoAtaque(): String = instrucao_ataque
    fun getInstrucaoMeio(): String = instrucao_meio
    fun getInstrucaoDefesa(): String = instrucao_defesa
    fun getPressao(): Int = pressao
    fun getEstilo(): Int = estilo
    fun getTempo(): Int = tempo

    // Setters
    fun setId(novoId: Int) { id = novoId }
    fun setPlanoDeJogo(novoPlano: String) { plano_de_jogo = novoPlano }
    fun setConduta(novaConduta: String) { conduta = novaConduta }
    fun setInstrucaoAtaque(novaInstrucao: String) { instrucao_ataque = novaInstrucao }
    fun setInstrucaoMeio(novaInstrucao: String) { instrucao_meio = novaInstrucao }
    fun setInstrucaoDefesa(novaInstrucao: String) { instrucao_defesa = novaInstrucao }
    fun setPressao(novaPressao: Int) { pressao = novaPressao }
    fun setEstilo(novoEstilo: Int) { estilo = novoEstilo }
    fun setTempo(novoTempo: Int) { tempo = novoTempo }

    // Método para exibir a tática
    fun exibirTatica() {
        println("Tática ID: $id")
        println("Plano de Jogo: $plano_de_jogo")
        println("Conduta: $conduta")
        println("Instruções de Ataque: $instrucao_ataque")
        println("Instruções de Meio: $instrucao_meio")
        println("Instruções de Defesa: $instrucao_defesa")
        println("Pressão: $pressao | Estilo: $estilo | Tempo: $tempo")
    }
}
