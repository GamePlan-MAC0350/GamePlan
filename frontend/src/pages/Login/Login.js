import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`Email: ${email}\nSenha: ${senha}`);
    // Aqui você pode fazer a lógica para enviar pro backend
  };

  const goToForms = () => {
    navigate('/forms');
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
        <button type="submit" style={{ padding: '8px 16px' }}>
          Entrar
        </button>
        <p>Ainda não possui sua conta?       
        <button onClick={goToForms}>Crie sua conta</button>
        </p>
      </form>
    </div>
  );
}

export default Login;
