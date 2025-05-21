import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Pesquisar_Times_Usuario.css';



function PesquisarTimesUsuario() {
    const navigate = useNavigate();
    const goToHomeUsuario = () => {
      navigate('/home_usuario');
    };
    const goToHome = () => {
      navigate('/');
    };

  

    const [nomeTime, setNome] = useState('');
      
      const handleSubmit = (e) => {
        e.preventDefault();
        alert(`Nome do jogador: ${nomeTime}`);
        // Aqui você pode fazer a lógica para enviar pro backend
      };
    
      useEffect(() => {
        document.body.classList.add('pesquisar-times-usuario-page');
        return () => {
        document.body.classList.remove('pesquisar-times-usuario-page');
        };
      }, []);
  return (
    <div className="pesquisar-times-usuario-page">
      <h1></h1>

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button onClick={goToHomeUsuario}>Pesquisar Jogadores </button>
        <button className="botao-destaque">Pesquisar Times</button>
        <button>Pesquisar Campeonatos</button>
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
    </div>

    
  );
}

export default PesquisarTimesUsuario;