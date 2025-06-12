import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { TimeContext } from '../../context/TimeContext';
import './Cadastrar_Times.css';
function Cadastrar_Times() {
  const navigate = useNavigate();
  const location = useLocation();
  const tecnicoId = location.state?.tecnicoId;
  const { setTimeId } = useContext(TimeContext);
  const [nome, setNome] = useState('');
  const [nacionalidade, setNacionalidade] = useState('');
  const [dataFundacao, setDataFundacao] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    // 1. Cria a tática padrão
    const taticaPadrao = {
      planoJogo: 'Posse de Bola',
      conduta: 'Agressivo',
      instrucaoAtaque: 'Apenas Ataque',
      instrucaoDefesa: 'Marcação Alta',
      instrucaoMeio: 'Apoiar a Defesa',
      pressao: 50,
      estilo: 50,
      tempo: 50
    };
    let taticaId = null;
    try {
      const taticaResp = await fetch('http://localhost:8080/taticas', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(taticaPadrao)
      });
      if (taticaResp.ok) {
        const taticaData = await taticaResp.json();
        taticaId = taticaData.id;
        console.log('Tática criada com id:', taticaId);
      } else {
        alert('Erro ao criar tática padrão!');
        return;
      }
    } catch (err) {
      alert('Erro de conexão ao criar tática!');
      return;
    }
    // 2. Cria o time com o id da tática
    const time = {
      nome: nome,
      nacionalidade: nacionalidade,
      dataFundacao: dataFundacao,
      tecnicoId: tecnicoId,
      taticaId: taticaId
    };
    try {
      const response = await fetch('http://localhost:8080/times', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(time)
      });
      if (response.ok) {
        const data = await response.json();
        setTimeId(data.id);
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
