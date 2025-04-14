import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Home.css'; // Importa o CSS

function Home() {
  const navigate = useNavigate();

  const goToLogin = () => {
    navigate('/login');
  };

  return (
    <div className="home-container">
      <h1>Bem-vindo ao GamePlan</h1>
      <h2>Em qual modo você quer acessar?</h2>
      <button onClick={goToLogin}>Modo usuário</button>
      <button onClick={goToLogin}>Modo treinador</button>
    </div>
  );
}

export default Home;
