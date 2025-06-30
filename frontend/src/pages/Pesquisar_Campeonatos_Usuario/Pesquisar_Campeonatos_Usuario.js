import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Pesquisar_Campeonatos_Usuario.css';



function PesquisarCampeonatosUsuario() {
    const navigate = useNavigate();
    const goToHomeUsuario = () => {
      navigate('/home_usuario');
    };
    const goToHome = () => {
      navigate('/');
    };
    const goToPesquisarTimesUsuario = () => {
      navigate('/pesquisar_times_usuario');
    };
  
 const goToMostrarCampeonato = () => {
      navigate('/Mostrar_Campeonatos_Usuario', { state: { nomeT: nomeCampeonato} });
    };
    const [nomeCampeonato, setNome] = useState('');
      
      const handleSubmit = (e) => {
        e.preventDefault();
        goToMostrarCampeonato();
        alert(`Nome do campeonato: ${nomeCampeonato}`);
        // Aqui você pode fazer a lógica para enviar pro backend
      };
    
      useEffect(() => {
        document.body.classList.add('pesquisar-campeonatos-usuario-page');
        return () => {
        document.body.classList.remove('pesquisar-campeonatos-usuario-page');
        };
      }, []);
  return (
    <div className="pesquisar-campeonatos-usuario-page">
      <h1></h1>

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button onClick={goToHomeUsuario}>Pesquisar Jogadores </button>
        <button onClick={goToPesquisarTimesUsuario} >Pesquisar Times</button>
        <button className="botao-destaque">Pesquisar Campeonatos</button>
      </div>

      
      <div style={{ textAlign: 'center', marginTop: '150px' }}>
      <h1>Pesquise o campeonato: </h1>
      <form onSubmit={handleSubmit}>
        <div className="input-group">
            <p>Nome do campeonato: </p>
            <input
            type="text"
            placeholder="ex: Champions League"
            value={nomeCampeonato}
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

export default PesquisarCampeonatosUsuario;