import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import './Criar_Campeonato.css';
import { TimeContext } from '../../context/TimeContext';

function Criar_Campeonato() {
  const navigate = useNavigate();
  const { timeId } = useContext(TimeContext);
  const [nome, setNome] = useState('');
  const [premio, setPremio] = useState('');
  const [numTimes, setNumTimes] = useState('');
  const [dataComeco, setDataComeco] = useState('');
  const [dataFinal, setDataFinal] = useState('');
  const [dataInscricao, setDataInscricao] = useState('');
  
  

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

  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`Por favor, verifique os dados do campeonato:\n\nNome: ${nome}\nPrêmio: ${premio}\nNúmero de times: ${numTimes}\nData de início: ${dataComeco}\nData da final: ${dataFinal}\nData máxima para inscrição: ${dataInscricao}`);
    // Aqui você pode fazer a lógica para enviar pro backend
  };

  useEffect(() => {
    document.body.classList.add('criar-campeonato-page');
    return () => {
      document.body.classList.remove('criar-campeonato-page');
    };
  }, []);

  return (

    <div className="criar_campeonato-page">
      <h1></h1>

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button onClick={goToHomeTreinador} >Pesquisar Jogadores </button>
        <button onClick={goToPesquisarTimesTreinador}>Pesquisar Times</button>
        <button onClick={goToPesquisarCampeonatosTreinador}>Pesquisar Campeonatos</button>
        <button onClick={goToModificarTatica}>Modificar Táticas</button>
        <button>Registrar Resultados</button>
        <button onClick={goToCadastrarJogadores}>Cadastrar Jogadores </button>
        <button className="botao-destaque">Criar Campeonato</button>
      </div>

    <div style={{ textAlign: 'center', marginTop: '150px' }}>
      <h1>Preencha os dados do campeonato: </h1>
      <form onSubmit={handleSubmit}>
      <div>
        <p>Nome do campeonato:
          <input
            type="String"
            placeholder="ex: Campeonato Brasileiro"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Prêmio:
          <input
            type="String"
            placeholder="ex: R$ 1000,00"
            value={premio}
            onChange={(e) => setPremio(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Número de times:
          <input
            type="Integer"
            placeholder="ex: 32"
            value={numTimes}
            onChange={(e) => setNumTimes(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Data do começo do campeonato:
          <input
            type="String"
            placeholder="ex: 30/01/2025"
            value={dataComeco}
            onChange={(e) => setDataComeco(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Data da final do campeonato:
          <input
            type="String"
            placeholder="ex: 30/02/2025"
            value={dataFinal}
            onChange={(e) => setDataFinal(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Data máxima para inscrição:
          <input
            type="String"
            placeholder="ex: 30/01/2025"
            value={dataInscricao}
            onChange={(e) => setDataInscricao(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <button type="submit" style={{ padding: '8px 16px' }}>
          Criar Campeonato</button>
      </form>
      <p style={{color: 'green'}}>ID do time: {timeId}</p>
    </div>
    </div>
  );
}

export default Criar_Campeonato;
