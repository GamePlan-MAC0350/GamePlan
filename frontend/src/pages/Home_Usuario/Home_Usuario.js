import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Home_Usuario.css';



function HomeUsuario() {
    const navigate = useNavigate();
    const goToPesquisarTimesUsuario = () => {
      navigate('/pesquisar_times_usuario');
    };
    const goToHome = () => {
      navigate('/');
    };
    const goToPesquisarCampeonatosUsuario = () => {
      navigate('/pesquisar_campeonatos_usuario');
    };

    const goToMostrarJogador = () => {
      navigate('/mostrar_jogador_usuario', { state: { nomeJog: nome} });
    }

  

    const [nome, setNome] = useState('');
      
      const handleSubmit = (e) => {
        e.preventDefault();
        goToMostrarJogador();
        //alert(`Nome do jogador: ${nome}`);
        // Aqui você pode fazer a lógica para enviar pro backend
      };
    
      useEffect(() => {
        document.body.classList.add('home-usuario-page');
        return () => {
        document.body.classList.remove('home-usuario-page');
        };
      }, []);
  return (
    <div className="home-usuario-page">
      <h1></h1>

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button className="botao-destaque" >Pesquisar Jogadores </button>
        <button onClick={goToPesquisarTimesUsuario}>Pesquisar Times</button>
        <button onClick={goToPesquisarCampeonatosUsuario}>Pesquisar Campeonatos</button>
      </div>

      
      <div class = "forms-box">
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

export default HomeUsuario;