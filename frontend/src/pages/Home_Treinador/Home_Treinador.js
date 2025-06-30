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
    const goToPesquisarTimesTreinador = () => {
      navigate('/Pesquisar_Times_Treinador');
    };
    const goToPesquisarCampeonatosTreinador = () => {
      navigate('/Pesquisar_Campeonatos_Treinador');
    };
    const goToModificarTatica = () => {
      navigate('/Modificar_Tatica');
    };

    const goToMostrarJogador = () => {
      navigate('/Mostrar_Jogador_Treinador', { state: { nomeJog: nome} });
    };

    const goToRegistrarResultados = () => {
      navigate('/Registrar_Resultados');
  };

    const [nome, setNome] = useState('');
      
      const handleSubmit = (e) => {
        e.preventDefault();
        goToMostrarJogador();
        //alert(`Nome do jogador: ${nome}`);
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
      

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button className="botao-destaque" >Pesquisar Jogadores </button>
        <button onClick={goToPesquisarTimesTreinador}>Pesquisar Times</button>
        <button onClick={goToPesquisarCampeonatosTreinador}>Pesquisar Campeonatos</button>
        <button onClick={goToModificarTatica}>Modificar Táticas</button>
        <button onClick={goToRegistrarResultados}>Registrar Resultados</button>
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