import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Mostrar_Time_Usuario.css';
import { useLocation } from 'react-router-dom'; 



function MostrarTimeUsuario() {
    const navigate = useNavigate();
    const goToHomeUsuario = () => {
      navigate('/home_usuario');
    };
    const goToHome = () => {
      navigate('/');
    };
    const goToPesquisarCampeonatosUsuario = () => {
        navigate('/pesquisar_campeonatos_usuario');
      };
    

    const [nomeTime, setNome] = useState('');
    const [jogadores, setJogadores] = useState([]);
    const [time, setTime] = useState(null);
    const [tatica, setTatica] = useState({});

    const location = useLocation(); // dentro do componente
    const nomeRecebido = location.state?.nomeT;

    // Função para buscar time por nome (igual ao treinador)
    const buscarTimePorNome = async (nomeBusca) => {
      console.log('[DEBUG] Iniciando busca do time com nome:', nomeBusca);
      try {
        const resp = await fetch(`http://localhost:8080/times?nome=${encodeURIComponent(nomeBusca)}`);
        console.log('[DEBUG] Resposta recebida do backend:', resp);
        if (resp.ok) {
          const data = await resp.json();
          setTime(data);
          setJogadores(data.jogadores || []);
          setTatica(data.tatica || {});
          console.log('[DEBUG] Dados do time recebidos:', data);
        } else {
          const erro = await resp.text();
          console.log('[DEBUG] Erro ao buscar time:', erro);
          setTime(null);
          setJogadores([]);
          setTatica({});
          alert('Time não encontrado!');
        }
      } catch (err) {
        console.log('[DEBUG] Erro de conexão ao buscar time:', err);
        setTime(null);
        setJogadores([]);
        setTatica({});
        alert('Erro de conexão com o backend!');
      }
    };

    const handleSubmit = (e) => {
      e.preventDefault();
      console.log('[DEBUG] handleSubmit chamado com nome:', nomeTime);
      if (nomeTime) {
        buscarTimePorNome(nomeTime);
      }
    };

    // useEffect para buscar automaticamente apenas na primeira navegação com nomeT
    useEffect(() => {
      document.body.classList.add('mostrar-time-treinador-page');
      if (nomeRecebido) {
        setNome(nomeRecebido);
        buscarTimePorNome(nomeRecebido);
      }
      return () => {
        document.body.classList.remove('mostrar-time-treinador-page');
      };
      // eslint-disable-next-line
    }, [nomeRecebido]);

    // Função para navegar para a página do jogador ao clicar no nome
    const goToMostrarJogador = (nomeJog) => {
      navigate('/Mostrar_Jogador_Usuario', { state: { nomeJog } });
    };

  return (
    <div className="mostrar-time-usuario-page">
      

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button onClick={goToHomeUsuario}>Pesquisar Jogadores </button>
        <button className="botao-destaque">Pesquisar Times</button>
        <button onClick={goToPesquisarCampeonatosUsuario}>Pesquisar Campeonatos</button>
      </div>

      
      <div style={{ textAlign: 'center', marginTop: '150px' }}>
      <h1>Pesquise o time: </h1>
      <form onSubmit={handleSubmit}>
        <div className="input-group">
            <p>Nome do time: </p>
            <input
            type="text"
            placeholder="ex: Real Madrid"
            value={nomeTime}
            onChange={(e) => setNome(e.target.value)}
            required
            />
            <button className="botao-pesquisa" type="submit" />
        </div>
    </form>
    </div>

    {time && (
      <div className="time-info-container" style={{ marginTop: '50px' }}>
        {/* Nome do time em destaque */}
        
        <div style={{ display: 'flex', justifyContent: 'space-around' }}>
          {/* Seção de informações do time */}
          <div className="time-info" style={{ width: '30%', padding: '20px', borderRadius: '10px', basckgroundColor: '#f9f9f9', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)' }}>
            <h3 style={{ textAlign: 'center', marginBottom: '20px', color: '#555' }}>Informações do Time</h3>
            {/* Nome do time e técnico no mesmo padrão dos outros atributos */}
            <p style={{ marginBottom: '10px', fontSize: '1rem', color: '#444' }}>
              <strong style={{ color: '#222', textTransform: 'capitalize' }}>Nome:</strong> {time.nome}
            </p>
            {time.tecnicoNome && (
              <p style={{ marginBottom: '10px', fontSize: '1rem', color: '#444' }}>
                <strong style={{ color: '#222', textTransform: 'capitalize' }}>Técnico:</strong> {time.tecnicoNome}
              </p>
            )}
            {Object.entries(time).map(([key, value]) => (
              key !== 'jogadores' && key !== 'tatica' && key !== 'id' && key !== 'tecnicoId' && key !== 'tecnicoNome' && key !== 'nome' ? (
                <p key={key} style={{ marginBottom: '10px', fontSize: '1rem', color: '#444' }}>
                  <strong style={{ color: '#222', textTransform: 'capitalize' }}>{key.replace('_', ' ')}:</strong> {value}
                </p>
              ) : null
            ))}
          </div>
          {/* Seção de lista de jogadores */}
          <div className="jogadores-info" style={{ width: '30%', padding: '20px', borderRadius: '10px', backgroundColor: '#f9f9f9', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)' }}>
            <h3 style={{ textAlign: 'center', marginBottom: '20px', color: '#555' }}>Jogadores</h3>
            <ul style={{ listStyleType: 'none', padding: 0 }}>
              {jogadores.map((jogador, index) => (
                <li key={index} style={{ marginBottom: '10px', fontSize: '1rem', color: '#444', cursor: 'pointer' }}
                    onClick={() => goToMostrarJogador(jogador)}>
                  <span style={{ fontWeight: 'bold', color: '#222', textDecoration: 'underline' }}>{jogador}</span>
                </li>
              ))}
            </ul>
          </div>
          {/* Seção de tática */}
          <div className="tatica-info" style={{ width: '30%', padding: '20px', borderRadius: '10px', backgroundColor: '#f9f9f9', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)' }}>
            <h3 style={{ textAlign: 'center', marginBottom: '20px', color: '#555' }}>Tática</h3>
            {tatica && Object.entries(tatica).length > 0 ? (
              Object.entries(tatica).map(([key, value]) => (
                key !== 'id' && (
                  <p key={key} style={{ marginBottom: '10px', fontSize: '1rem', color: '#444' }}>
                    <strong style={{ color: '#222', textTransform: 'capitalize' }}>{key.replace('_', ' ')}:</strong> {value}
                  </p>
                )
              ))
            ) : (
              <p style={{ color: '#888' }}>Sem tática cadastrada</p>
            )}
          </div>
        </div>
      </div>
    )}

    </div>

    
  );
}

export default MostrarTimeUsuario;