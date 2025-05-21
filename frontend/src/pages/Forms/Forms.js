import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Forms.css';
function Forms() {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [nome, setNome] = useState('');
  const [nacionalidade, setNacionalidade] = useState('');
  const [time, setTime] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`Nome: ${nome}\nTime: ${time}\nNacionalidade: ${nacionalidade}\nEmail: ${email}\nSenha: ${senha}`);

    goToHomeTreinador();
    // Aqui você pode fazer a lógica para enviar pro backend
  };
  
  const goToHomeTreinador = () => {
    navigate('/home_treinador');
  };

  useEffect(() => {
    document.body.classList.add('forms-page');
    return () => {
      document.body.classList.remove('forms-page');
    };
  }, []);

  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>Preencha seus dados: </h1>
      <form onSubmit={handleSubmit}>
      <div>
        <p>Nome completo:
          <input
            type="String"
            placeholder="ex: Cristiano Ronaldo"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Time:
          <input
            type="String"
            placeholder="ex: Manchester United"
            value={time}
            onChange={(e) => setTime(e.target.value)}
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
        <div>
        <p>Email:
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Senha:
          <input
            type="password"
            placeholder="Senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </p>
        </div>
        <button type="submit" style={{ padding: '8px 16px' }}>
          Criar login
        </button>
      </form>
    </div>
  );
}

export default Forms;
