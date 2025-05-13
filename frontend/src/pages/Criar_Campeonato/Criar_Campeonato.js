import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Criar_Campeonato.css';
function Criar_Campeonato() {
  const navigate = useNavigate();
  const [nome, setNome] = useState('');
  const [premio, setPremio] = useState('');
  const [numTimes, setNumTimes] = useState('');
  const [dataComeco, setDataComeco] = useState('');
  const [dataFinal, setDataFinal] = useState('');
  const [dataInscricao, setDataInscricao] = useState('');
  
  

  
  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`Por favor, verifique os dados do campeonato:\n\nNome: ${nome}\nPrêmio: ${premio}\nNúmero de times: ${numTimes}\nData de início: ${dataComeco}\nData da final: ${dataFinal}\nData máxima para inscrição: ${dataInscricao}`);
    // Aqui você pode fazer a lógica para enviar pro backend
  };

  useEffect(() => {
    document.body.classList.add('forms-page');
    return () => {
      document.body.classList.remove('forms-page');
    };
  }, []);

  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>Preencha os dados do campeonato: </h1>
      <form onSubmit={handleSubmit}>
      <div>
        <p>Nome do campeonato:
          <input
            type="String"
            placeholder="Nome, ex: Campeonato Brasileiro"
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
            placeholder="Prêmio, ex: R$ 1000,00"
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
            type="integer"
            placeholder="Número de times, ex: 32"
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
            placeholder="Data do começo do campeonato, ex: 30/01/2025"
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
            placeholder="Data da final do campeonato, ex: 30/02/2025"
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
            placeholder="Data máxima para inscrição, ex: 30/01/2025"
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
    </div>
  );
}

export default Criar_Campeonato;
