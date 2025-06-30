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
                <button key={partida.id} className="botao-partida">
                  {partida.time1Nome} x {partida.time2Nome} (Jogo {partida.numeroPartida})
                </button>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Registrar_Resultados;
