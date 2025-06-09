import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Home.css';
import GamePlanLogo from '../../assets/images/GamePlanLogo.png';

function Home() {
  const navigate = useNavigate();

  const goToLogin = () => {
    navigate('/login');
  };
  const goToHome_Usuario = () => {
    navigate('/home_usuario');
  };

  return (
    <div className="home-container">
      <div className="home-box">
        <img src={GamePlanLogo} alt="GamePlan Logo" className="logo" />
        <h1>Bem-vindo ao GamePlan!</h1>
        <h2>Em qual modo você deseja acessar?</h2>
        <div className="button-group">
          <button onClick={goToHome_Usuario}>Modo usuário</button>
          <button onClick={goToLogin}>Modo treinador</button>
        </div>
      </div>
    </div>
  );
}

export default Home;
