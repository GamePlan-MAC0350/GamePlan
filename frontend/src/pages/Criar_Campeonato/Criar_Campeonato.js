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
const goToRegistrarResultados = () => {
  navigate('/Registrar_Resultados');
};

  const handleSubmit = async (e) => {
    e.preventDefault();
    alert(`Por favor, verifique os dados do campeonato:\n\nNome: ${nome}\nPrêmio: ${premio}\nNúmero de times: ${numTimes}\nData de início: ${dataComeco}\nData da final: ${dataFinal}\nData máxima para inscrição: ${dataInscricao}`);
    // Envia para o backend
    try {
      const response = await fetch('http://localhost:8080/campeonatos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          nome: nome, // Adicionado campo nome
          numeroTimes: parseInt(numTimes),
          premio: premio,
          pontos: 0, // Inicialmente zero
          dataComeco: dataComeco,
          dataFinal: dataFinal,
          dataInscricao: dataInscricao,
          campeaoId: null,
          idTimeFundador: timeId // NOVO CAMPO
        })
      });
      if (response.ok) {
        alert('Campeonato criado com sucesso!');
        setNome(''); setPremio(''); setNumTimes(''); setDataComeco(''); setDataFinal(''); setDataInscricao('');
      } else {
        const errorMsg = await response.text();
        if (errorMsg.includes('já pertence a um campeonato')) {
          alert('Erro: O seu time já está associado a um campeonato. Não é possível criar outro.');
        } else {
          alert('Erro ao criar campeonato!');
        }
      }
    } catch (err) {
      alert('Erro de conexão com o backend!');
    }
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
        <button onClick={goToRegistrarResultados}>Registrar Resultados</button>
        <button onClick={goToCadastrarJogadores}>Cadastrar Jogadores </button>
        <button className="botao-destaque">Criar Campeonato</button>
      </div>

    <div class = "forms-box">
      <h1>Preencha os dados do campeonato: </h1>
      <form onSubmit={handleSubmit}>
      <div>
        <p>Nome do campeonato:</p>
          <input
            type="String"
            placeholder="ex: Campeonato Brasileiro"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
          />
        </div>
        <div>
        <p>Prêmio:</p>
          <input
            type="String"
            placeholder="ex: R$ 1000,00"
            value={premio}
            onChange={(e) => setPremio(e.target.value)}
            required
          />
        </div>
        <div>
        <p>Número de times:</p>
          <input
            type="Integer"
            placeholder="ex: 32"
            value={numTimes}
            onChange={(e) => setNumTimes(e.target.value)}
            required

          />
        </div>
        <div>
        <p>Data do começo do campeonato:</p>
          <input
            type="String"
            placeholder="ex: 30/01/2025"
            value={dataComeco}
            onChange={(e) => setDataComeco(e.target.value)}
            required
          />
        </div>
        <div>
        <p>Data da final do campeonato:</p>
          <input
            type="String"
            placeholder="ex: 30/02/2025"
            value={dataFinal}
            onChange={(e) => setDataFinal(e.target.value)}
            required
          />
        </div>
        <div>
        <p>Data máxima para inscrição:</p>
          <input
            type="String"
            placeholder="ex: 30/01/2025"
            value={dataInscricao}
            onChange={(e) => setDataInscricao(e.target.value)}
            required
          />
        </div>
        <button type="submit" style={{ padding: '8px 16px' }}>
          Criar Campeonato</button>
      </form>
      {/* <p style={{color: 'green'}}>ID do time: {timeId}</p> */}
    </div>
    </div>
  );
}

export default Criar_Campeonato;
