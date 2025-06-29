import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Forms.css';

function Forms() {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [nome, setNome] = useState('');
  const [nacionalidade, setNacionalidade] = useState('');
  const [dataNascimento, setDataNascimento] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    const tecnico = {
      nome: nome,
      nacionalidade: nacionalidade,
      dataNascimento: dataNascimento,
      email: email,
      senha: senha
    };
    try {
      const response = await fetch('http://localhost:8080/tecnicos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(tecnico)
      });
      if (response.ok) {
        const data = await response.json();
        const tecnicoId = data.id;
        goToCadastrarTimes(tecnicoId);
      } else {
        alert('Erro ao criar técnico!');
      }
    } catch (err) {
      alert('Erro de conexão com o backend!');
    }
  };
  
  const goToCadastrarTimes = (tecnicoId) => {
    navigate('/cadastrar_times', { state: { tecnicoId } });
  };

  useEffect(() => {
    document.body.classList.add('forms-page');
    return () => {
      document.body.classList.remove('forms-page');
    };
  }, []);

  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}  className = "forms-box">
      <h1>Preencha seus dados: </h1>
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
        <p>Data de Nascimento</p>
          <input
            type="String"
            placeholder="ex: 30/11/2000"
            value={dataNascimento}
            onChange={(e) => setDataNascimento(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        <div>
        <p>Nacionalidade:</p>
          <input
            type="String"
            placeholder="ex: Brasileiro"
            value={nacionalidade}
            onChange={(e) => setNacionalidade(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        <div>
        <p>Email:</p>
          <input
            type="String"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        <div>
        <p>Senha:</p>
          <input
            type="password"
            placeholder="Senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        <button type="submit" style={{ padding: '8px 16px' }}>
          Criar login
        </button>
      </form>
    </div>
  );
}

export default Forms;
