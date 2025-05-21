import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Home_Treinador.css';



function HomeTreinador() {
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

  

    const [nome, setNome] = useState('');
      
      const handleSubmit = (e) => {
        e.preventDefault();
        alert(`Nome do jogador: ${nome}`);
        // Aqui você pode fazer a lógica para enviar pro backend
      };
    
      useEffect(() => {
        document.body.classList.add('home-treinador-page');
        return () => {
        document.body.classList.remove('home-treinador-page');
        };
      }, []);
  return (
    <div className="home-treinador-page">
      <h1></h1>

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button className="botao-destaque" >Pesquisar Jogadores </button>
        <button>Pesquisar Times</button>
        <button>Pesquisar Campeonatos</button>
        <button>Modificar Táticas</button>
        <button>Registrar Resultados</button>
        <button onClick={goToCadastrarJogadores}>Cadastrar Jogadores </button>
        <button onClick={goToCriarCampeonato}>Criar Campeonato</button>
      </div>

      
      <div style={{ textAlign: 'center', marginTop: '150px' }}>
      <h1>Pesquise o jogador: </h1>
      <form onSubmit={handleSubmit}>
        <div className="input-group">
            <p>Nome do jogador: </p>
            <input
            type="text"
            placeholder="ex: Cristiano Ronaldo"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
            />
            <button className="botao-pesquisa" type="submit" />
        </div>
    </form>
    </div>
    </div>

    
  );
}

export default HomeTreinador;