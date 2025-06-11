package GamePlan.model

import GamePlan.model.Time

class Tecnico (
    private var id: Int,
    private var nome: String,
    private var nacionalidade: String,
    private var data_nascimento: String,
    private var email: String,
    private var senha: String
) {
    // Getters
    fun getId(): Int = id
    fun getNome(): String = nome
    fun getNacionalidade(): String = nacionalidade
    fun getDataNascimento(): String = data_nascimento
    fun getEmail(): String = email
    fun getSenha(): String = senha

    // Setters
    fun setId(novoId: Int) { id = novoId }
    fun setNome(novoNome: String) { nome = novoNome }
    fun setNacionalidade(novaNacionalidade: String) { nacionalidade = novaNacionalidade }
    fun setDataNascimento(novaData: String) { data_nascimento = novaData }
    fun setEmail(novoEmail: String) { email = novoEmail }
    fun setSenha(novaSenha: String) {senha = novaSenha}

    // Método para exibir os dados do técnico
    fun exibirTecnico() {
        println("Técnico: $nome ($nacionalidade)")
        println("Nascimento: $data_nascimento | Email: $email")
    }
}
