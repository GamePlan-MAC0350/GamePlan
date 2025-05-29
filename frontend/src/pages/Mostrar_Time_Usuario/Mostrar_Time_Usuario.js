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

    var jogadores = [
        'Cristiano Ronaldo',
        'Karim Benzema',
        'Luka Modric',  
        'Sergio Ramos',
        'Toni Kroos',
        'Luka Jovic',
        'Eden Hazard',
        'Vinicius Junior',
        'Thibaut Courtois',
        'Casemiro',
        'David Alaba',
        'Ferland Mendy',
        'Marco Asensio',
        'Rodrygo Goes',
        'Federico Valverde',
        'Isco Alarcon',
        'Nacho Fernandez',
        'Dani Carvajal',
        'Jesus Vallejo',
        'Marco Asensio',
        'Andriy Lunin',
        'Eduardo Camavinga',
    ];

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

    const [nomeTime, setNome] = useState('');
      
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

    return () => {
        document.body.classList.remove('mostrar-time-treinador-page');
    };
    }, [nomeRecebido]);

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

export default MostrarTimeUsuario;