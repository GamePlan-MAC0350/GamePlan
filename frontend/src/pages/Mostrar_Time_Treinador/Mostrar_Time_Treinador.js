import React, { useState, useEffect } from 'react';
import { data, useNavigate } from 'react-router-dom';
import { useLocation } from 'react-router-dom'; 
import './Mostrar_Time_Treinador.css';



function MostrarTimeTreinador() {
    const navigate = useNavigate();
    const goToCadastrarJogadores = () => {
        navigate('/Cadastrar_Jogadores');
      };
    const goToCriarCampeonato = () => {
      navigate('/Criar_Campeonato');
    };
    const goToHome = () => {
      navigate('/');
    };
    const goToHomeTreinador = () => {
      navigate('/Home_Treinador');
    };

    const goToPesquisarCampeonatosTreinador = () => {
      navigate('/Pesquisar_Campeonatos_Treinador');
    };
  
    const goToModificarTatica = () => {
      navigate('/Modificar_Tatica');
    };
    const [nomeTime, setNome] = useState('');
    const [jogadores, setJogadores] = useState([]); // Estado para armazenar jogadores
      
    var time = {
        nome: 'Real Madrid',
        pais: 'Espanha',
        data_de_Fundacao: '06-03-1902',
        treinador: 'Carlo Ancelotti',
        artilheiro: 'Cristiano Ronaldo',
        maior_assistente: 'Karim Benzema',
        partidas_Jogadas: 500,
        gols_marcados: 1500,
        gols_sofridos: 600,
        vitorias: 350,
        derrotas: 100,
        empates: 50, // Adicionei um valor padrão para evitar undefined
        pontos: 1500,
    }

    var tatica={
        plano_de_jogo: 'posse de bola',
        conduta: 'Agressiva',
        instrução_ataque: 'Apenas atacar',
        instrução_defesa: 'Apenas defender',
        instrução_meio: 'Apenas meio campo',
        pressao: 50,
        estilo: 50,
        tempo: 50,
    }




      const handleSubmit = (e) => {
        e.preventDefault();
        alert(`Nome do time: ${nomeTime}`);
        // Aqui você pode fazer a lógica para enviar pro backend
      };
    
      const location = useLocation(); // dentro do componente
        const nomeRecebido = location.state?.nomeT;

        useEffect(() => {
        document.body.classList.add('mostrar-time-treinador-page');

        if (nomeRecebido) {
            setNome(nomeRecebido);
        }

        // Chamada ao backend para buscar jogadores
        if (nomeRecebido) {
            fetch(`http://localhost:8080/team/${nomeRecebido}/players`) 
                .then((response) => {
                    if (!response.ok) {
                        throw new Error('Erro ao buscar jogadores');
                    }
                    return response.json();
                })
                .then((data) => {
                    if (data && data.players) {
                        setJogadores(data.players);
                    } else {
                        console.error('Resposta inesperada do backend:', data);
                    }
                })
                .catch((error) => {
                    console.error('Erro:', error);
                });
        }

        return () => {
            document.body.classList.remove('mostrar-time-treinador-page');
        };
        }, [nomeRecebido]);
  return (
    <div className="mostrar-time-treinador-page">
      <h1>Mostrar Time Treinador</h1>

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button onClick={goToHomeTreinador}>Pesquisar Jogadores </button>
        <button className="botao-destaque">Pesquisar Times</button>
        <button onClick={goToPesquisarCampeonatosTreinador}>Pesquisar Campeonatos</button>
        <button onClick={goToModificarTatica}>Modificar Táticas</button>
        <button>Registrar Resultados</button>
        <button onClick={goToCadastrarJogadores}>Cadastrar Jogadores </button>
        <button onClick={goToCriarCampeonato}>Criar Campeonato</button>
      </div>

      <div style={{ textAlign: 'center', marginTop: '50px' }}>
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
            <button className="botao-pesquisa" type="submit">Pesquisar</button>
        </div>
    </form>
    </div>

    {nomeTime && (
      <div className="time-info-container" style={{ marginTop: '50px' }}>
        {/* Nome do time em destaque */}
        <div style={{ textAlign: 'center', marginBottom: '30px' }}>
          <h2 style={{ fontSize: '2.5rem', fontWeight: 'bold', color: '#333' }}>{time.nome}</h2>
        </div>

        <div style={{ display: 'flex', justifyContent: 'space-around' }}>
          {/* Seção de informações do time */}
          <div className="time-info" style={{ width: '30%', padding: '20px', borderRadius: '10px', backgroundColor: '#f9f9f9', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)' }}>
            <h3 style={{ textAlign: 'center', marginBottom: '20px', color: '#555' }}>Informações do Time</h3>
            {Object.entries(time).map(([key, value]) => (
              <p key={key} style={{ marginBottom: '10px', fontSize: '1rem', color: '#444' }}>
                <strong style={{ color: '#222', textTransform: 'capitalize' }}>{key.replace('_', ' ')}:</strong> {value}
              </p>
            ))}
          </div>

          {/* Seção de lista de jogadores */}
          <div className="jogadores-info" style={{ width: '30%', padding: '20px', borderRadius: '10px', backgroundColor: '#f9f9f9', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)' }}>
            <h3 style={{ textAlign: 'center', marginBottom: '20px', color: '#555' }}>Jogadores</h3>
            <ul style={{ listStyleType: 'none', padding: 0 }}>
              {jogadores.map((jogador, index) => (
                <li key={index} style={{ marginBottom: '10px', fontSize: '1rem', color: '#444' }}>
                  <span style={{ fontWeight: 'bold', color: '#222' }}>{jogador}</span>
                </li>
              ))}
            </ul>
          </div>

          {/* Seção de tática */}
          <div className="tatica-info" style={{ width: '30%', padding: '20px', borderRadius: '10px', backgroundColor: '#f9f9f9', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)' }}>
            <h3 style={{ textAlign: 'center', marginBottom: '20px', color: '#555' }}>Tática</h3>
            {Object.entries(tatica).map(([key, value]) => (
              <p key={key} style={{ marginBottom: '10px', fontSize: '1rem', color: '#444' }}>
                <strong style={{ color: '#222', textTransform: 'capitalize' }}>{key.replace('_', ' ')}:</strong> {value}
              </p>
            ))}
          </div>
        </div>
      </div>
    )}
    </div>

    
  );
}

export default MostrarTimeTreinador;