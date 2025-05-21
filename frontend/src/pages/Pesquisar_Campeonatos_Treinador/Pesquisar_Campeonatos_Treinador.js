import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Pesquisar_Campeonatos_Treinador.css';



function PesquisarCampeonatosTreinador() {
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

    const goToPesquisarTimesTreinador = () => {
        navigate('/Pesquisar_Times_Treinador');
      };

    const [nomeCampeonato, setNome] = useState('');
      
      const handleSubmit = (e) => {
        e.preventDefault();
        alert(`Nome do campeonato: ${nomeCampeonato}`);
        // Aqui você pode fazer a lógica para enviar pro backend
      };
    
      useEffect(() => {
        document.body.classList.add('pesquisar-campeonatos-treinador-page');
        return () => {
        document.body.classList.remove('pesquisar-campeonatos-treinador-page');
        };
      }, []);
  return (
    <div className="pesquisar-campeonatos-treinador-page">
      <h1></h1>

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button   onClick={goToHomeTreinador}>Pesquisar Jogadores </button>
        <button onClick={goToPesquisarTimesTreinador}>Pesquisar Times</button>
        <button className="botao-destaque">Pesquisar Campeonatos</button>
        <button>Modificar Táticas</button>
        <button>Registrar Resultados</button>
        <button onClick={goToCadastrarJogadores}>Cadastrar Jogadores </button>
        <button onClick={goToCriarCampeonato}>Criar Campeonato</button>
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

export default PesquisarCampeonatosTreinador;