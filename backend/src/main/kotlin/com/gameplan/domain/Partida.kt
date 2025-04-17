package GamePlan.model

import GamePlan.model.Time

class Partida (
    private var id: Int,
    private var time_1: Time,
    private var time_2: Time,
    private var gols_time1: Int,
    private var gols_time2: Int,
    private var local: String,
    private var data_partida: String,
    private var horario_partida: String,
    private var empate: Boolean,
    private var gols_time1_penaltis: Int,
    private var gols_time2_penaltis: Int,
    private var time_vencedor: Time?
) {
    // Getters
    fun getId(): Int = id
    fun getTime1(): Time = time_1
    fun getTime2(): Time = time_2
    fun getGolsTime1(): Int = gols_time1
    fun getGolsTime2(): Int = gols_time2
    fun getLocal(): String = local
    fun getDataPartida(): String = data_partida
    fun getHorarioPartida(): String = horario_partida
    fun isEmpate(): Boolean = empate
    fun getGolsTime1Penaltis(): Int = gols_time1_penaltis
    fun getGolsTime2Penaltis(): Int = gols_time2_penaltis
    fun getTimeVencedor(): Time? = time_vencedor

    // Setters
    fun setId(novoId: Int) { id = novoId }
    fun setTime1(novoTime: Time) { time_1 = novoTime }
    fun setTime2(novoTime: Time) { time_2 = novoTime }
    fun setGolsTime1(novosGols: Int) { gols_time1 = novosGols }
    fun setGolsTime2(novosGols: Int) { gols_time2 = novosGols }
    fun setLocal(novoLocal: String) { local = novoLocal }
    fun setDataPartida(novaData: String) { data_partida = novaData }
    fun setHorarioPartida(novoHorario: String) { horario_partida = novoHorario }
    fun setEmpate(novoEmpate: Boolean) { empate = novoEmpate }
    fun setGolsTime1Penaltis(novosGols: Int) { gols_time1_penaltis = novosGols }
    fun setGolsTime2Penaltis(novosGols: Int) { gols_time2_penaltis = novosGols }
    fun setTimeVencedor(novoVencedor: Time?) { time_vencedor = novoVencedor }

    // Método para exibir os dados da partida
    fun exibirPartida() {
        println("Partida ID: $id")
        println("${time_1.getNome()} ($gols_time1) x ($gols_time2) ${time_2.getNome()}")
        println("Local: $local | Data: $data_partida | Horário: $horario_partida")
        
        if (empate) {
            println("A partida terminou empatada!")
        } else {
            println("Vencedor: ${time_vencedor?.getNome() ?: "A definir"}")
        }

        if (gols_time1_penaltis > 0 || gols_time2_penaltis > 0) {
            println("Disputa de pênaltis: ${gols_time1_penaltis} x ${gols_time2_penaltis}")
        }
    }
}
