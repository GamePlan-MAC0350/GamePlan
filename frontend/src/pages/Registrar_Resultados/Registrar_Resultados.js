import React, { useContext, useEffect, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import './Registrar_Resultados.css';
import { TimeContext } from '../../context/TimeContext';

function Registrar_Resultados() {
  const navigate = useNavigate();
  const { timeId } = useContext(TimeContext);
  const [fundadorMsg, setFundadorMsg] = useState('');
  const [sorteioMsgJSX, setSorteioMsgJSX] = useState(null);
  const [campeonatoId, setCampeonatoId] = useState(null);
  const campeonatoIdRef = useRef(null); // Adiciona ref para o id
  const [sorteio, setSorteio] = useState(false);
  const [partidas, setPartidas] = useState([]); // NOVO: lista de partidas sem vencedor
  const [partidaSelecionada, setPartidaSelecionada] = useState(null); // NOVO: partida selecionada para formulário

  useEffect(() => {
    async function fetchCampeonatoFundador() {
      setFundadorMsg('');
      setSorteioMsgJSX(null);
      setCampeonatoId(null);
      campeonatoIdRef.current = null;
      setSorteio(false);
      if (!timeId) {
        setFundadorMsg('Você não está conectado a um time.');
        return;
      }
      try {
        const resp = await fetch(`http://localhost:8080/times/${timeId}`);
        console.log('[DEBUG] fetch /times/', timeId, resp);
        if (!resp.ok) {
          setFundadorMsg('Não foi possível buscar informações do time.');
          return;
        }
        const timeData = await resp.json();
        console.log('[DEBUG] timeData:', timeData);
        if (timeData.campeonatoId) {
          // Buscar dados do campeonato
          const campResp = await fetch(`http://localhost:8080/campeonatos/${timeData.campeonatoId}`);
          console.log('[DEBUG] fetch /campeonatos/', timeData.campeonatoId, campResp);
          if (!campResp.ok) {
            setFundadorMsg('Você não é fundador de nenhum campeonato.');
            return;
          }
          const campData = await campResp.json();
          console.log('[DEBUG] campData:', campData);
          if (campData.idTimeFundador === timeId) {
            setFundadorMsg(
              <span>
                Você é fundador do campeonato: <b>{campData.nome}</b>.
              </span>
            );
            // DEBUG: log do id do campeonato
            console.log('[DEBUG] setCampeonatoId:', campData.id);
            setCampeonatoId(() => {
              console.log('[DEBUG] setCampeonatoId (callback):', campData.id);
              campeonatoIdRef.current = campData.id; // Atualiza ref junto com o state
              return campData.id;
            });
            setSorteio(campData.sorteio);
            setSorteioMsgJSX(
              <div style={{marginTop: '18px'}}>
                <span style={{color: campData.sorteio ? '#28a745' : '#d9534f', fontWeight: 'bold', fontSize: '1.1rem'}}>
                  {campData.sorteio ? 'O chaveamento já foi sorteado.' : 'O chaveamento ainda não foi sorteado.'}
                </span>
                {!campData.sorteio && (
                  <div style={{marginTop: '18px'}}>
                    <button className="botao-destaque" onClick={handleRealizarSorteio}>Realizar sorteio</button>
                  </div>
                )}
              </div>
            );
            // NOVO: buscar partidas se já sorteado
            if (campData.sorteio) {
              console.log('[DEBUG] Sorteio já realizado, buscando partidas...');
              fetchPartidasSemVencedor(campData.id);
            }
          } else {
            setFundadorMsg('Você não é fundador de nenhum campeonato.');
            setSorteioMsgJSX(null);
            setCampeonatoId(null);
            campeonatoIdRef.current = null;
          }
        } else {
          setFundadorMsg('Você não é fundador de nenhum campeonato.');
          setSorteioMsgJSX(null);
          setCampeonatoId(null);
          campeonatoIdRef.current = null;
        }
      } catch (e) {
        setFundadorMsg('Erro ao buscar informações do campeonato.');
        setSorteioMsgJSX(null);
        console.log('[DEBUG] Erro geral:', e);
      }
    }
    fetchCampeonatoFundador();
    // eslint-disable-next-line
  }, [timeId]);

  // NOVO: buscar partidas sem vencedor
  async function fetchPartidasSemVencedor(campId) {
    try {
      console.log('[DEBUG] Buscando partidas sem vencedor para campeonato', campId);
      const resp = await fetch(`http://localhost:8080/partidas?campeonatoId=${campId}&semVencedor=true`);
      console.log('[DEBUG] Resposta fetch partidas:', resp);
      if (resp.ok) {
        const data = await resp.json();
        console.log('[DEBUG] Partidas recebidas:', data);
        setPartidas(data);
      } else {
        const msg = await resp.text();
        console.log('[DEBUG] Erro ao buscar partidas:', msg);
      }
    } catch (e) {
      console.log('[DEBUG] Erro ao buscar partidas:', e);
    }
  }

  async function handleRealizarSorteio() {
    console.log('[DEBUG] Botão Realizar Sorteio clicado');
    console.log('[DEBUG] campeonatoId atual (ref):', campeonatoIdRef.current);
    const id = campeonatoIdRef.current;
    if (!id) {
      console.log('[DEBUG] campeonatoId não definido');
      return;
    }
    try {
      const resp = await fetch(`http://localhost:8080/campeonatos/${id}`);
      console.log('[DEBUG] Resposta do fetch campeonato:', resp);
      if (!resp.ok) {
        alert('Não foi possível buscar informações do campeonato.');
        return;
      }
      const campData = await resp.json();
      console.log('[DEBUG] Dados do campeonato:', campData);
      if (parseInt(campData.timesInscritos) !== parseInt(campData.numeroTimes)) {
        alert('O sorteio só pode ser realizado quando todos os times estiverem inscritos!');
        return;
      }
      // Chama o endpoint de sorteio
      const sorteioResp = await fetch(`http://localhost:8080/campeonatos/${id}/sorteio`, {
        method: 'POST',
      });
      if (sorteioResp.ok) {
        alert('Chaveamento realizado e partidas registradas!');
        // Atualiza a tela para refletir o sorteio
        window.location.reload();
      } else {
        const msg = await sorteioResp.text();
        alert('Erro ao realizar sorteio: ' + msg);
      }
    } catch (e) {
      console.log('[DEBUG] Erro ao buscar informações do campeonato:', e);
      alert('Erro ao buscar informações do campeonato.');
    }
  }

  // Handler para abrir formulário ao clicar em uma partida
  function handleAbrirFormulario(partida) {
    setPartidaSelecionada(partidaSelecionada && partidaSelecionada.id === partida.id ? null : partida);
  }

  // NOVO: Remove partida da lista após registrar resultado
  function handleRemoverPartida(partidaId) {
    setPartidas(partidas => partidas.filter(p => p.id !== partidaId));
    setPartidaSelecionada(null);
  }

  // Mini formulário de resultado
  function MiniFormulario({ partida, onClose, onResultadoRegistrado }) {
    const [golsTime1, setGolsTime1] = useState('');
    const [golsTime2, setGolsTime2] = useState('');
    const [etapa, setEtapa] = useState('resultado'); // 'resultado' ou 'goleadores'
    const [goleadores1, setGoleadores1] = useState([]); // [{goleador: '', assistencia: ''}, ...]
    const [goleadores2, setGoleadores2] = useState([]);
    const [penaltis, setPenaltis] = useState({
      show: false,
      golsTime1: '',
      golsTime2: ''
    });
    const [finalizado, setFinalizado] = useState(false);

    // Atualiza arrays de goleadores ao avançar para a etapa de detalhamento
    useEffect(() => {
      if (etapa === 'goleadores') {
        setGoleadores1(Array(Number(golsTime1) || 0).fill().map(() => ({goleador: ''})));
        setGoleadores2(Array(Number(golsTime2) || 0).fill().map(() => ({goleador: ''})));
      }
      // eslint-disable-next-line
    }, [etapa]);

    function handleRegistrarResultado(e) {
      e.preventDefault();
      if (golsTime1 === '' || golsTime2 === '') return;
      setEtapa('goleadores');
    }

    function handleChangeGoleador1(idx, valor) {
      setGoleadores1(arr => arr.map((item, i) => i === idx ? {...item, goleador: valor} : item));
    }
    function handleChangeGoleador2(idx, valor) {
      setGoleadores2(arr => arr.map((item, i) => i === idx ? {...item, goleador: valor} : item));
    }

    async function enviarResultadoParaBackend({
      penaltisUsados = false
    } = {}) {
      // Determina vencedor
      let vencedorId = null;
      if (penaltisUsados) {
        if (Number(penaltis.golsTime1) > Number(penaltis.golsTime2)) vencedorId = partida.time1Id;
        else if (Number(penaltis.golsTime2) > Number(penaltis.golsTime1)) vencedorId = partida.time2Id;
      } else {
        if (Number(golsTime1) > Number(golsTime2)) vencedorId = partida.time1Id;
        else if (Number(golsTime2) > Number(golsTime1)) vencedorId = partida.time2Id;
      }
      const body = {
        golsTime1: Number(golsTime1),
        golsTime2: Number(golsTime2),
        goleadores1,
        goleadores2,
        penaltisTime1: penaltisUsados ? Number(penaltis.golsTime1) : undefined,
        penaltisTime2: penaltisUsados ? Number(penaltis.golsTime2) : undefined,
        vencedorId
      };
      try {
        const resp = await fetch(`http://localhost:8080/partidas/${partida.id}/registrar-resultado`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(body)
        });
        if (!resp.ok) {
          const msg = await resp.text();
          alert(msg || 'Erro ao registrar resultado.');
          return false;
        }
        return true;
      } catch (e) {
        alert('Erro ao registrar resultado.');
        return false;
      }
    }

    async function handleFinalizar(e) {
      e.preventDefault();
      // Verifica empate
      if (Number(golsTime1) === Number(golsTime2)) {
        setPenaltis(p => ({ ...p, show: true }));
      } else {
        const ok = await enviarResultadoParaBackend();
        if (ok) {
          setFinalizado(true);
          if (onResultadoRegistrado) onResultadoRegistrado(partida.id);
        }
      }
    }

    async function handleFinalizarPenaltis(e) {
      e.preventDefault();
      const ok = await enviarResultadoParaBackend({ penaltisUsados: true });
      if (ok) {
        setFinalizado(true);
        if (onResultadoRegistrado) onResultadoRegistrado(partida.id);
      }
    }

    return (
      <div className="mini-formulario-resultado">
        <div style={{margin: '12px 0', background: '#f0f8ff', borderRadius: 8, padding: 16, boxShadow: '0 2px 8px #0001'}}>
          <div style={{fontWeight: 'bold', marginBottom: 8}}>
            {partida.time1Nome} x {partida.time2Nome} (Jogo {partida.numeroPartida})
          </div>
          {etapa === 'resultado' && (
            <form onSubmit={handleRegistrarResultado}>
              <div style={{display: 'flex', gap: 12, justifyContent: 'center', alignItems: 'center'}}>
                <label>
                  {partida.time1Nome}: <input type="number" min="0" value={golsTime1} onChange={e => setGolsTime1(e.target.value)} style={{width: 50}} required />
                </label>
                <span style={{fontWeight: 'bold'}}>x</span>
                <label>
                  {partida.time2Nome}: <input type="number" min="0" value={golsTime2} onChange={e => setGolsTime2(e.target.value)} style={{width: 50}} required />
                </label>
              </div>
              <div style={{marginTop: 12, display: 'flex', gap: 8, justifyContent: 'center'}}>
                <button className="botao-destaque" style={{padding: '6px 18px', fontSize: '1rem'}} type="submit">Registrar</button>
                <button type="button" onClick={onClose} style={{padding: '6px 18px', fontSize: '1rem', background: '#ccc', color: '#333', borderRadius: 5, border: 'none'}}>Cancelar</button>
              </div>
            </form>
          )}
          {etapa === 'goleadores' && !finalizado && (
            <form onSubmit={handleFinalizar}>
              <div style={{marginBottom: 10, fontWeight: 'bold'}}>Detalhe dos gols</div>
              {goleadores1.length > 0 && (
                <div style={{marginBottom: 12}}>
                  <div style={{marginBottom: 4}}>{partida.time1Nome}:</div>
                  {goleadores1.map((g, idx) => (
                    <div key={idx} style={{display: 'flex', gap: 8, marginBottom: 4, alignItems: 'center', justifyContent: 'center'}}>
                      <input type="text" placeholder="Autor do gol" value={g.goleador} onChange={e => handleChangeGoleador1(idx, e.target.value)} style={{width: 120}} />
                    </div>
                  ))}
                </div>
              )}
              {goleadores2.length > 0 && (
                <div style={{marginBottom: 12}}>
                  <div style={{marginBottom: 4}}>{partida.time2Nome}:</div>
                  {goleadores2.map((g, idx) => (
                    <div key={idx} style={{display: 'flex', gap: 8, marginBottom: 4, alignItems: 'center', justifyContent: 'center'}}>
                      <input type="text" placeholder="Autor do gol" value={g.goleador} onChange={e => handleChangeGoleador2(idx, e.target.value)} style={{width: 120}} />
                    </div>
                  ))}
                </div>
              )}
              <div style={{marginTop: 12, display: 'flex', gap: 8, justifyContent: 'center'}}>
                <button className="botao-destaque" style={{padding: '6px 18px', fontSize: '1rem'}} type="submit">Finalizar</button>
                <button type="button" onClick={onClose} style={{padding: '6px 18px', fontSize: '1rem', background: '#ccc', color: '#333', borderRadius: 5, border: 'none'}}>Cancelar</button>
              </div>
            </form>
          )}
          {penaltis.show && !finalizado && (
            <form onSubmit={handleFinalizarPenaltis} style={{marginTop: 16}}>
              <div style={{fontWeight: 'bold', marginBottom: 8}}>Empate! Informe o placar dos pênaltis:</div>
              <div style={{display: 'flex', gap: 12, justifyContent: 'center', alignItems: 'center'}}>
                <label>
                  {partida.time1Nome}: <input type="number" min="0" value={penaltis.golsTime1} onChange={e => setPenaltis(p => ({...p, golsTime1: e.target.value}))} style={{width: 50}} required />
                </label>
                <span style={{fontWeight: 'bold'}}>x</span>
                <label>
                  {partida.time2Nome}: <input type="number" min="0" value={penaltis.golsTime2} onChange={e => setPenaltis(p => ({...p, golsTime2: e.target.value}))} style={{width: 50}} required />
                </label>
              </div>
              <div style={{marginTop: 12, display: 'flex', gap: 8, justifyContent: 'center'}}>
                <button className="botao-destaque" style={{padding: '6px 18px', fontSize: '1rem'}} type="submit">Finalizar</button>
                <button type="button" onClick={onClose} style={{padding: '6px 18px', fontSize: '1rem', background: '#ccc', color: '#333', borderRadius: 5, border: 'none'}}>Cancelar</button>
              </div>
            </form>
          )}
          {finalizado && (
            <div style={{marginTop: 16, color: '#28a745', fontWeight: 'bold'}}>Resultado registrado com sucesso!</div>
          )}
        </div>
      </div>
    );
  }

  const goToHomeTreinador = () => navigate('/Home_Treinador');
  const goToPesquisarTimesTreinador = () => navigate('/Pesquisar_Times_Treinador');
  const goToPesquisarCampeonatosTreinador = () => navigate('/Pesquisar_Campeonatos_Treinador');
  const goToModificarTatica = () => navigate('/Modificar_Tatica');
  const goToCadastrarJogadores = () => navigate('/Cadastrar_Jogadores');
  const goToCriarCampeonato = () => navigate('/Criar_Campeonato');
  const goToHome = () => navigate('/');

  return (
    <div className="registrar-resultados-page">
      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button onClick={goToHomeTreinador}>Pesquisar Jogadores</button>
        <button onClick={goToPesquisarTimesTreinador}>Pesquisar Times</button>
        <button onClick={goToPesquisarCampeonatosTreinador}>Pesquisar Campeonatos</button>
        <button onClick={goToModificarTatica}>Modificar Táticas</button>
        <button className="botao-destaque">Registrar Resultados</button>
        <button onClick={goToCadastrarJogadores}>Cadastrar Jogadores</button>
        <button onClick={goToCriarCampeonato}>Criar Campeonato</button>
      </div>
      <div className="info-container">
        <h1>Registrar Resultados</h1>
        {fundadorMsg && <p>{fundadorMsg}</p>}
        {sorteioMsgJSX}
        {/* NOVO: exibe botões das partidas */}
        {sorteio && partidas.length > 0 && (
          <div style={{marginTop: '32px'}}>
            <h2>Partidas pendentes</h2>
            <div className="partidas-grid">
              {partidas.map((partida) => (
                <div key={partida.id}>
                  <button className="botao-partida" onClick={() => handleAbrirFormulario(partida)}>
                    {partida.time1Nome} x {partida.time2Nome} (Jogo {partida.numeroPartida})
                  </button>
                  {partidaSelecionada && partidaSelecionada.id === partida.id && (
                    <MiniFormulario partida={partida} onClose={() => setPartidaSelecionada(null)} onResultadoRegistrado={handleRemoverPartida} />
                  )}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Registrar_Resultados;
