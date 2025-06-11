import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './Cadastrar_Times.css';
function Cadastrar_Times() {
  const navigate = useNavigate();
  const location = useLocation();
  const tecnicoId = location.state?.tecnicoId;
  const [nome, setNome] = useState('');
  const [nacionalidade, setNacionalidade] = useState('');
  const [dataFundacao, setDataFundacao] = useState('');
  

  const handleSubmit = async (e) => {
    e.preventDefault();
    const time = {
      nome: nome,
      nacionalidade: nacionalidade,
      dataFundacao: dataFundacao,
      tecnicoId: tecnicoId // Associa o técnico ao time
    };
    try {
      const response = await fetch('http://localhost:8080/times', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(time)
      });
      if (response.ok) {
        goToHomeTreinador();
      } else {
        alert('Erro ao criar time!');
      }
    } catch (err) {
      alert('Erro de conexão com o backend!');
    }
  };
  
  const goToHomeTreinador = () => {
    navigate('/home_treinador');
  };

  useEffect(() => {
    document.body.classList.add('cadastrar-times-page');
    return () => {
      document.body.classList.remove('cadastrar-times-page');
    };
  }, []);

  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>Preencha os dados do time: </h1>
      <form onSubmit={handleSubmit}>
      <div>
        <p>Nome completo do clube:
          <input
            type="String"
            placeholder="ex: Manchester United"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Data de Fundacao:
          <input
            type="String"
            placeholder="ex: 30/11/2000"
            value={dataFundacao}
            onChange={(e) => setDataFundacao(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Nacionalidade:
          <input
            type="String"
            placeholder="ex: Brasileiro"
            value={nacionalidade}
            onChange={(e) => setNacionalidade(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <button type="submit" style={{ padding: '8px 16px' }}>
          Criar Time
        </button>
      </form>
    </div>
  );
}

export default Cadastrar_Times;
