import React from 'react';
import './ChaveamentoCampeonato.css';

function ChaveamentoCampeonato({ partidas, timesMap, numeroTimes }) {
  if (!Array.isArray(partidas) || partidas.length === 0) return <p>Nenhum chaveamento disponível.</p>;

  // Usa o número de times real da competição
  const n = numeroTimes || new Set(partidas.flatMap(p => [p.time1Id, p.time2Id])).size;
  const totalRodadas = Math.ceil(Math.log2(n));

  // Agrupa partidas por rodada usando intervalos explícitos corretos
  const rodadas = [];
  let partidaAtual = 1;
  for (let rodada = 0; rodada < totalRodadas; rodada++) {
    const jogosNaRodada = Math.floor(n / Math.pow(2, rodada + 1));
    const inicio = partidaAtual;
    const fim = inicio + jogosNaRodada - 1;
    rodadas.push(partidas.filter(p => p.numeroPartida >= inicio && p.numeroPartida <= fim));
    partidaAtual = fim + 1;
  }

  // Verifica se a final foi realizada e pega o campeão
  let campeao = null;
  if (rodadas.length > 0) {
    const ultimaRodada = rodadas[rodadas.length - 1];
    if (ultimaRodada.length === 1 && ultimaRodada[0].vencedorId) {
      campeao = timesMap[ultimaRodada[0].vencedorId] || `Time ${ultimaRodada[0].vencedorId}`;
    }
  }

  // Log para depuração
  console.log('n =', n, 'totalRodadas =', totalRodadas, 'partidas:', partidas.map(p => p.numeroPartida));
  console.log('Rodadas agrupadas:', rodadas.map((r, i) => ({ rodada: i + 1, partidas: r.map(p => p.numeroPartida) })));

  return (
    <div className="chaveamento-container">
      {rodadas.map((partidasRodada, idx) =>
        partidasRodada.length > 0 && (
          <div key={idx} className="chaveamento-coluna">
            <div className="chaveamento-rodada-titulo">
              {`Rodada ${idx + 1}`}
            </div>
            {partidasRodada.map((partida, i) => (
              <React.Fragment key={partida.id}>
                <div className="chaveamento-partida">
                  <div className="chaveamento-times">
                    {timesMap[partida.time1Id] || `Time ${partida.time1Id}`}<br />
                    <span style={{ color: '#007bff', fontWeight: 400 }}>vs</span><br />
                    {timesMap[partida.time2Id] || `Time ${partida.time2Id}`}
                  </div>
                  <div className="chaveamento-resultado">
                    {partida.vencedorId ? (
                      <>
                        <span>Resultado: <b>{
                          (partida.golsTime1 !== null && partida.golsTime1 !== undefined && partida.golsTime1 !== '')
                            ? parseInt(partida.golsTime1)
                            : '?'
                        } x {
                          (partida.golsTime2 !== null && partida.golsTime2 !== undefined && partida.golsTime2 !== '')
                            ? parseInt(partida.golsTime2)
                            : '?'
                        }</b></span>
                        {/* Exibe pênaltis se houve empate nos gols e campos de pênaltis presentes */}
                        {(parseInt(partida.golsTime1) === parseInt(partida.golsTime2) &&
                          partida.golsTime1 !== null && partida.golsTime2 !== null &&
                          partida.golsTime1 !== undefined && partida.golsTime2 !== undefined &&
                          partida.golsTime1 !== '' && partida.golsTime2 !== '' &&
                          (partida.penaltisTime1 !== undefined && partida.penaltisTime1 !== null && partida.penaltisTime2 !== undefined && partida.penaltisTime2 !== null)
                        ) && (
                          <div className="chaveamento-penaltis">
                            Pênaltis: <b>{partida.penaltisTime1} x {partida.penaltisTime2}</b>
                          </div>
                        )}
                      </>
                    ) : (
                      <span className="chaveamento-aguardando">Aguardando resultado</span>
                    )}
                  </div>
                  {partida.vencedorId && (
                    <div className="chaveamento-vencedor">
                      Vencedor: {timesMap[partida.vencedorId] || `Time ${partida.vencedorId}`}
                    </div>
                  )}
                </div>
                {i < partidasRodada.length - 1 && (
                  <div className="chaveamento-linha" />
                )}
              </React.Fragment>
            ))}
          </div>
        )
      )}
      {/* Coluna do campeão */}
      {campeao && (
        <div className="chaveamento-coluna">
          <div className="chaveamento-rodada-titulo">Campeão</div>
          <div className="chaveamento-partida" style={{ background: '#d4edda', border: '2px solid #28a745' }}>
            <span role="img" aria-label="Troféu" style={{ fontSize: 28 }}>🏆</span><br />
            <b>{campeao}</b>
          </div>
        </div>
      )}
    </div>
  );
}

export default ChaveamentoCampeonato;
