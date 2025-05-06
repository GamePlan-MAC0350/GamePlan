import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Home_Treinador.css';



function HomeTreinador() {
    const navigate = useNavigate();
    const goToCadastrarJogadores = () => {
        navigate('/Cadastrar_Jogadores');
      };
  return (
    <div className="home-treinador-page">
      <h1>O que você deseja fazer?</h1>
      <div className="button-grid">
        <button>Pesquisar Jogadores</button>
        <button>Pesquisar Times</button>
        <button>Pesquisar Campeonatos</button>
        <button>Modificar Táticas</button>
        <button>Registrar Resultados</button>
        <button onClick={goToCadastrarJogadores}>Cadastrar Jogadores </button>
        <button>Criar Campeonato</button>
      </div>
    </div>
  );
}

export default HomeTreinador;