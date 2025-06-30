import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ChaveamentoCampeonato from '../../components/ChaveamentoCampeonato';
import './Mostrar_Campeonatos_Usuario.css';

function MostrarCampeonatosUsuario() {
  const navigate = useNavigate();
  const location = useLocation();
  const nomeRecebido = location.state?.nomeT;
  const [campeonato, setCampeonato] = useState(null);
  const [erro, setErro] = useState(null);
  const [busca, setBusca] = useState(nomeRecebido || '');
  const [partidas, setPartidas] = useState([]);
  const [timesMap, setTimesMap] = useState({});

  // Navegação topo (usuário)
  const goToHome = () => navigate('/');
  const goToHomeUsuario = () => navigate('/home_usuario');
  const goToPesquisarTimesUsuario = () => navigate('/pesquisar_times_usuario');
  const goToPesquisarCampeonatosUsuario = () => navigate('/pesquisar_campeonatos_usuario');

  useEffect(() => {
    document.body.classList.add('mostrar-campeonatos-usuario-page');
    return () => {
      document.body.classList.remove('mostrar-campeonatos-usuario-page');
    };
  }, []);

  async function buscarCampeonatoPorNome(nomeBusca) {
    try {
      const resp = await fetch(`http://localhost:8080/campeonatos?nome=${encodeURIComponent(nomeBusca)}`);
      if (resp.ok) {
        const data = await resp.json();
        setCampeonato(data);
        setErro(null);
      } else {
        const erroMsg = await resp.text();
        setErro(erroMsg || 'Campeonato não encontrado!');
        setCampeonato(null);
      }
    } catch (err) {
      setErro('Erro de conexão ao buscar campeonato!');
      setCampeonato(null);
    }
  }

  useEffect(() => {
    if (nomeRecebido) {
      buscarCampeonatoPorNome(nomeRecebido);
    }
  }, [nomeRecebido]);

  // Buscar partidas e nomes dos times se o campeonato já foi sorteado
  useEffect(() => {
    async function fetchPartidasENomes() {
      if (campeonato && campeonato.sorteio) {
        try {
          const resp = await fetch(`http://localhost:8080/partidas?campeonatoId=${campeonato.id}`);
          if (resp.ok) {
            const partidasData = await resp.json();
            setPartidas(partidasData);
            // Buscar nomes dos times
            const ids = Array.from(new Set(partidasData.flatMap(p => [p.time1Id, p.time2Id, p.vencedorId].filter(Boolean))));
            const nomes = {};
            for (const id of ids) {
              try {
                const respTime = await fetch(`http://localhost:8080/times/${id}`);
                if (respTime.ok) {
                  const t = await respTime.json();
                  nomes[id] = t.nome;
                }
              } catch {}
            }
            setTimesMap(nomes);
          }
        } catch {}
      } else {
        setPartidas([]);
        setTimesMap({});
      }
    }
    fetchPartidasENomes();
  }, [campeonato]);

  const handleBuscar = (e) => {
    e.preventDefault();
    if (busca.trim()) {
      setErro(null);
      setCampeonato(null);
      buscarCampeonatoPorNome(busca.trim());
    }
  };

  return (
    <div className="mostrar-campeonatos-usuario-page">
      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button onClick={goToHomeUsuario}>Pesquisar Jogadores</button>
        <button onClick={goToPesquisarTimesUsuario}>Pesquisar Times</button>
        <button className="botao-destaque" onClick={goToPesquisarCampeonatosUsuario}>Pesquisar Campeonatos</button>
      </div>
      <div className="busca-campeonato-container">
        <h1>Pesquise o campeonato: </h1>
        <form onSubmit={handleBuscar}>
          <div className="input-group">
            <p>Nome do campeonato: </p>
            <input
              type="text"
              placeholder="ex: Champions League"
              value={busca}
              onChange={e => setBusca(e.target.value)}
              required
            />
            <button className="botao-pesquisa" type="submit" />
          </div>
        </form>
      </div>
      <div className="campeonato-info" style={{ marginTop: 10, position: 'relative', minHeight: 320 }}>
        {erro && <p style={{ color: 'red' }}>{erro}</p>}
        {campeonato && (
          <>
            <div style={{ marginTop: 40 }}>
              <h2>Campeonato: {campeonato.nome}</h2>
              <p><b>Número de Times:</b> {campeonato.numeroTimes}</p>
              <p><b>Prêmio:</b> {campeonato.premio}</p>
              <p><b>Pontos:</b> {campeonato.pontos}</p>
              <p><b>Data de Início:</b> {campeonato.dataComeco}</p>
              <p><b>Data da Final:</b> {campeonato.dataFinal}</p>
              <p><b>Data de Inscrição:</b> {campeonato.dataInscricao}</p>
              <p><b>Times Inscritos:</b> {campeonato.timesInscritos}</p>
              <p><b>Time Fundador:</b> {campeonato.nomeTimeFundador || 'Não encontrado'}</p>
            </div>
            {campeonato.sorteio === true && (
              <div style={{ marginTop: 40 }}>
                <h2>Chaveamento</h2>
                <ChaveamentoCampeonato partidas={partidas} timesMap={timesMap} />
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default MostrarCampeonatosUsuario;
