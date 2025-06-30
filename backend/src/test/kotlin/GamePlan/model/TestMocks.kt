package GamePlan.model

// Centralização dos mocks para todos os testes

fun mockJogador(id: Int = 1, nome: String = "Jogador", time: Time? = null) = Jogador(
    id = id,
    nome = nome,
    altura = 180,
    nacionalidade = "BR",
    data_nascimento = "01/01/2000",
    numero_da_camisa = 10,
    posicao = "Atacante",
    pe_dominante = "Destro",
    gols_totais = 0,
    assistencias_totais = 0,
    partidas_jogadas_totais = 0,
    minutos_jogados_totais = 0,
    cartoes_amarelos = 0,
    cartoes_vermelhos = 0,
    time = time ?: mockTime(999, "Time Mock", true)
)

fun mockTatica() = Tatica(
    id = 1,
    plano_de_jogo = "4-4-2",
    conduta = "Ofensiva",
    instrucao_ataque = "Ataque rápido",
    instrucao_meio = "Meio compacto",
    instrucao_defesa = "Defesa alta",
    pressao = 5,
    estilo = 2,
    tempo = 90
)

fun mockTecnico() = Tecnico(
    id = 1,
    nome = "Técnico",
    nacionalidade = "BR",
    data_nascimento = "01/01/1970",
    email = "tecnico@email.com",
    senha = "123456"
)

fun mockTime(id: Int = 1, nome: String = "Time Teste", simple: Boolean = false): Time {
    val t = Time(
        id = id,
        nome = nome,
        nacionalidade = "BR",
        data_fundacao = "2000-01-01",
        artilheiro = mockJogador(1000, "Artilheiro", null),
        maior_assistente = mockJogador(1001, "Assistente", null),
        partidas_jogadas = 0,
        gols_marcados = 0,
        gols_sofridos = 0,
        vitorias = 0,
        derrotas = 0,
        pontos = 0,
        tatica = mockTatica(),
        tecnico = mockTecnico()
    )
    if (!simple) {
        // Corrige referência circular
        t.setArtilheiro(mockJogador(1000, "Artilheiro", t))
        t.setMaiorAssistente(mockJogador(1001, "Assistente", t))
    }
    return t
}
