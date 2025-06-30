import React, { useEffect, useState, useContext } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { TimeContext } from '../../context/TimeContext';
import ChaveamentoCampeonato from '../../components/ChaveamentoCampeonato';
import './Mostrar_Campeonatos_Treinador.css';

function MostrarCampeonatosTreinador() {
  const navigate = useNavigate();
  const location = useLocation();
  const { timeId } = useContext(TimeContext);
  const nomeRecebido = location.state?.nomeT;
  const [campeonato, setCampeonato] = useState(null);
  const [erro, setErro] = useState(null);
  const [busca, setBusca] = useState('');
  const [timeInfo, setTimeInfo] = useState(null);
  const [partidas, setPartidas] = useState([]);
  const [timesMap, setTimesMap] = useState({});

  // Funções de navegação
  const goToCadastrarJogadores = () => navigate('/Cadastrar_Jogadores');
  const goToCriarCampeonato = () => navigate('/Criar_Campeonato');
  const goToHome = () => navigate('/');
  const goToHomeTreinador = () => navigate('/Home_Treinador');
  const goToPesquisarTimesTreinador = () => navigate('/Pesquisar_Times_Treinador');
  const goToPesquisarCampeonatosTreinador = () => navigate('/Pesquisar_Campeonatos_Treinador');
  const goToModificarTatica = () => navigate('/Modificar_Tatica');
  const goToRegistrarResultados = () => navigate('/Registrar_Resultados');

  useEffect(() => {
    document.body.classList.add('mostrar-campeonatos-treinador-page');
    return () => {
      document.body.classList.remove('mostrar-campeonatos-treinador-page');
    };
  }, []);

  // Função de busca agora definida fora do useEffect para ser reutilizável
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

  // Buscar info do time logado (para saber se já está inscrito em algum campeonato)
  useEffect(() => {
    if (timeId) {
      fetch(`http://localhost:8080/times/${timeId}`)
        .then(resp => resp.ok ? resp.json() : null)
        .then(data => {
          if (data) {
            setTimeInfo(data);
          } else {
            setTimeInfo(null);
          }
        })
        .catch(() => setTimeInfo(null));
    } else {
      setTimeInfo(null);
    }
  }, [timeId]);

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
    <div className="mostrar-campeonatos-treinador-page">
      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button onClick={goToHomeTreinador}>Pesquisar Jogadores</button>
        <button onClick={goToPesquisarTimesTreinador}>Pesquisar Times</button>
        <button className="botao-destaque" onClick={goToPesquisarCampeonatosTreinador}>Pesquisar Campeonatos</button>
        <button onClick={goToModificarTatica}>Modificar Táticas</button>
        <button onClick={goToRegistrarResultados}>Registrar Resultados</button>
        <button onClick={goToCadastrarJogadores}>Cadastrar Jogadores</button>
        <button onClick={goToCriarCampeonato}>Criar Campeonato</button>
      </div>
      <div style={{ textAlign: 'center', marginTop: '540px' }}>
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
      <div className="campeonato-info" style={{ position: 'relative', minHeight: 320 }}>
        {erro && <p style={{ color: 'red' }}>{erro}</p>}
        {campeonato && (
          <>
            {console.log('DEBUG timeInfo:',  JSON.stringify(timeInfo))}
            {console.log('DEBUG campeonato:', JSON.stringify(campeonato))}
            {console.log('DEBUG timeId:', timeId)}
            {timeInfo && timeInfo.campeonatoId ? (
              <div style={{
                position: 'absolute',
                top: 0,
                right: 20,
                color: '#dc3545',
                background: '#fff3cd',
                border: '1px solid #ffeeba',
                borderRadius: '5px',
                padding: '10px 20px',
                fontWeight: 'bold',
                zIndex: 2
              }}>
                Seu time já está inscrito nesse ou em outro campeonato
              </div>
            ) : campeonato.sorteio ? (
              <>
                <div style={{
                  position: 'absolute',
                  top: 20,
                  right: 20,
                  color: '#dc3545',
                  background: '#fff3cd',
                  border: '1px solid #ffeeba',
                  borderRadius: '5px',
                  padding: '10px 20px',
                  fontWeight: 'bold',
                  zIndex: 2
                }}>
                  O campeonato já começou e não aceita mais inscrições.
                </div>
                
              </>
            ) : (
              <button
                style={{
                  position: 'absolute',
                  top: 20,
                  right: 20,
                  padding: '10px 20px',
                  background: '#28a745',
                  color: 'white',
                  border: 'none',
                  borderRadius: '5px',
                  cursor: 'pointer',
                  fontWeight: 'bold',
                  zIndex: 2,
                  marginRight: '40px' // aumenta o espaço entre o botão e o nome do campeonato
                }}
                onClick={async () => {
                  if (!timeId || !campeonato) return;
                  setErro(null);
                  try {
                    const resp = await fetch(`http://localhost:8080/campeonatos/${campeonato.id}/inscrever-time`, {
                      method: 'POST',
                      headers: { 'Content-Type': 'application/json' },
                      body: JSON.stringify({ timeId })
                    });
                    if (resp.ok) {
                      // Atualiza info do time e campeonato
                      fetch(`http://localhost:8080/times/${timeId}`)
                        .then(r => r.ok ? r.json() : null)
                        .then(data => setTimeInfo(data));
                      buscarCampeonatoPorNome(campeonato.nome);
                      alert('Time inscrito com sucesso!');
                    } else {
                      const msg = await resp.text();
                      setErro(msg || 'Erro ao inscrever time!');
                    }
                  } catch (err) {
                    setErro('Erro de conexão ao inscrever time!');
                  }
                }}
              >
                Inscrever-se
              </button>
            )}
            <div style={{ marginTop: 60 }}>
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

export default MostrarCampeonatosTreinador;
