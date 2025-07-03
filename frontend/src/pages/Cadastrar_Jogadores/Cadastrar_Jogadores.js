import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import './Cadastrar_Jogadores.css';
import { TimeContext } from '../../context/TimeContext';

function Cadastrar_Jogadores() {
  const { timeId } = useContext(TimeContext);
  const navigate = useNavigate();
  const [nome, setNome] = useState('');
  const [nacionalidade, setNacionalidade] = useState('');

  const [posicao, setPosição] = useState('');
  const [altura, setAltura] = useState('');
  const [dataNascimento, setDataNascimento] = useState('');
  const [numeroCamisa, setNumeroCamisa] = useState('');
  const [peDominante, setPeDominante] = useState('');

  const goToCadastrarJogadores = () => {
    navigate('/Cadastrar_Jogadores');
  };
const goToCriarCampeonato = () => {
  navigate('/Criar_Campeonato');
};
const goToHomeTreinador = () => {
  navigate('/Home_Treinador');
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
const goToRegistrarResultados = () => {
  navigate('/Registrar_Resultados');
};
  const handleSubmit = async (e) => {
    e.preventDefault();
    // Monta o objeto do jogador
    const jogador = {
      nome,
      altura: parseInt(altura),
      nacionalidade,
      dataNascimento,
      numeroCamisa: parseInt(numeroCamisa),
      posicao,
      peDominante,
      golsTotais: 0,
      assistenciasTotais: 0,
      cartoesAmarelos: 0,
      cartoesVermelhos: 0,
      clubeId: timeId // timeId do contexto
    };
    console.log('Enviando jogador para backend:', jogador);
    try {
      const response = await fetch('http://localhost:8080/jogadores', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(jogador)
      });
      if (response.ok) {
        alert('Jogador cadastrado com sucesso!');
        setNome('');
        setNacionalidade('');
        setPosição('');
        setAltura('');
        setDataNascimento('');
        setNumeroCamisa('');
        setPeDominante('');
      } else {
        const erro = await response.text();
        alert('Erro ao cadastrar jogador: ' + erro);
      }
    } catch (err) {
      alert('Erro de conexão com o backend!');
    }
  };

  useEffect(() => {
    document.body.classList.add('cadastrar-jogadores-page');
    return () => {
      document.body.classList.remove('cadastrar-jogadores-page');
    };
  }, []);

  return (

    <div className="cadastrar-jogadores-page">
      <h1></h1>

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button onClick={goToHomeTreinador} >Pesquisar Jogadores </button>
        <button onClick={goToPesquisarTimesTreinador}>Pesquisar Times</button>
        <button onClick={goToPesquisarCampeonatosTreinador}>Pesquisar Campeonatos</button>
        <button onClick={goToModificarTatica}>Modificar Táticas</button>
        <button onClick={goToRegistrarResultados}>Registrar Resultados</button>
        <button className="botao-destaque">Cadastrar Jogadores </button>
        <button onClick={goToCriarCampeonato}>Criar Campeonato</button>
      </div>

  <div class = "forms-box">
      <h1 style={{ marginLeft: '0vw' }}>Preencha os dados do jogador: </h1>
      {/* <p style={{color: 'green'}}>ID do time: {timeId}</p> */}
      <form onSubmit={handleSubmit}>
      <div>
        <p>Nome completo</p>
          <input
            type="String"
            placeholder="ex: Cristiano Ronaldo"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        <div>
        <p>Nacionalidade</p>
          <input
            type="String"
            placeholder="ex: Português"
            value={nacionalidade}
            onChange={(e) => setNacionalidade(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        <div>
        <p>Posição</p>
          <input
            type="String"
            placeholder="ex: Atacante, Meia, Lateral, Zagueiro, Goleiro"
            value={posicao}
            onChange={(e) => setPosição(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        <div>
        <p>Altura em cm</p>
          <input
            type="Integer"
            placeholder="ex: 180"
            value={altura}
            onChange={(e) => setAltura(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        <div>
        <p>Data de Nascimento</p>
          <input
            type="String"
            placeholder="ex: 30/01/2000"
            value={dataNascimento}
            onChange={(e) => setDataNascimento(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        <div>
        <p>Número da camisa</p>
          <input
            type="Integer"
            placeholder="ex: 7"
            value={numeroCamisa}
            onChange={(e) => setNumeroCamisa(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        <div>
        <p>Pé Dominante</p>
          <input
            type="String"
            placeholder="ex: Direito ou Esquerdo"
            value={peDominante}
            onChange={(e) => setPeDominante(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        <button type="submit">
          Registar Jogador      
        </button>
      </form>
    </div>
  </div>
  );
}

export default Cadastrar_Jogadores;
