import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';
import { TimeContext } from '../../context/TimeContext';
function Login() {
  useEffect(() => {
    document.body.classList.add('login-page');
    return () => {
      document.body.classList.remove('login-page');
    };
  }, []);

  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState('');
  const { setTimeId } = useContext(TimeContext);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro('');
    try {
      const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, senha })
      });
      if (response.ok) {
        const data = await response.json();
        setTimeId(data.timeId); // Salva o id do time no contexto global
        goToHomeTreinador();
      } else if (response.status === 401) {
        setErro('Email ou senha incorretos.');
      } else {
        setErro('Erro ao tentar fazer login.');
      }
    } catch (err) {
      setErro('Erro de conexão com o backend!');
    }
  };

  const goToForms = () => {
    navigate('/forms');
  };
  const goToHomeTreinador = () => {
    navigate('/home_treinador');
  };
  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        <div>
          <input
            type="password"
            placeholder="Senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
        </div>
        {erro && <p style={{ color: 'red' }}>{erro}</p>}
        <button type="submit" style={{ padding: '8px 16px' }}>
          Entrar
        </button>
        <p>Ainda não possui sua conta?       
        <button type="button" onClick={goToForms}>Crie sua conta</button>
        </p>
      </form>
    </div>
  );
}

export default Login;
