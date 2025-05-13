import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home/Home';
import Login from './pages/Login/Login';
import Forms from './pages/Forms/Forms';
import Home_Usuario from './pages/Home_Usuario/Home_Usuario';
import Home_Treinador from './pages/Home_Treinador/Home_Treinador';
import Cadastrar_Jogadores from './pages/Cadastrar_Jogadores/Cadastrar_Jogadores';
import Criar_Campeonato from './pages/Criar_Campeonato/Criar_Campeonato';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/forms" element={<Forms />} />
        <Route path="/home_usuario" element={<Home_Usuario />} />
        <Route path="/home_treinador" element={<Home_Treinador />} />
        <Route path="/cadastrar_jogadores" element={<Cadastrar_Jogadores />} />
        <Route path="/criar_campeonato" element={<Criar_Campeonato />} />

        <Route path="*" element={<div>404 Not Found</div>} />
      </Routes>
    </Router>
  );
}

export default App;
