import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Pesquisar_Times_Treinador.css';



function PesquisarTimesTreinador() {
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

    const goToMostrarTime = () => {
      navigate('/Mostrar_Time_Treinador', { state: { nomeT: nomeTime} });
    };
    const goToRegistrarResultados = () => {
      navigate('/Registrar_Resultados');
    };

    const [nomeTime, setNome] = useState('');
      
      const handleSubmit = (e) => {
        e.preventDefault();
        goToMostrarTime();
        alert(`Nome do time: ${nomeTime}`);
        // Aqui você pode fazer a lógica para enviar pro backend
      };
    
      useEffect(() => {
        document.body.classList.add('pesquisar-times-treinador-page');
        return () => {
        document.body.classList.remove('pesquisar-times-treinador-page');
        };
      }, []);
  return (
    <div className="pesquisar-times-treinador-page">
      <h1></h1>

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button   onClick={goToHomeTreinador}>Pesquisar Jogadores </button>
        <button className="botao-destaque">Pesquisar Times</button>
        <button onClick={goToPesquisarCampeonatosTreinador}>Pesquisar Campeonatos</button>
        <button onClick={goToModificarTatica}>Modificar Táticas</button>
        <button onClick={goToRegistrarResultados}>Registrar Resultados</button>
        <button onClick={goToCadastrarJogadores}>Cadastrar Jogadores </button>
        <button onClick={goToCriarCampeonato}>Criar Campeonato</button>
      </div>

      
      <div class = "forms-box">
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

export default PesquisarTimesTreinador;