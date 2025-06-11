import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home/Home';
import Login from './pages/Login/Login';
import Forms from './pages/Forms/Forms';
import Home_Usuario from './pages/Home_Usuario/Home_Usuario';
import Home_Treinador from './pages/Home_Treinador/Home_Treinador';
import Cadastrar_Jogadores from './pages/Cadastrar_Jogadores/Cadastrar_Jogadores';
import Criar_Campeonato from './pages/Criar_Campeonato/Criar_Campeonato';
import Pesquisar_Jogador from './pages/Pesquisar_Jogador/Pesquisar_Jogador';
import Pesquisar_Times_Usuario from './pages/Pesquisar_Times_Usuario/Pesquisar_Times_Usuario';
import Pesquisar_Campeonatos_Usuario from './pages/Pesquisar_Campeonatos_Usuario/Pesquisar_Campeonatos_Usuario';
import Pesquisar_Times_Treinador from './pages/Pesquisar_Times_Treinador/Pesquisar_Times_Treinador';
import Pesquisar_Campeonatos_Treinador from './pages/Pesquisar_Campeonatos_Treinador/Pesquisar_Campeonatos_Treinador';
import Modificar_Tatica from './pages/Modificar_Tatica/Modificar_Tatica';
import Mostrar_Jogador_Treinador from './pages/Mostrar_Jogador_Treinador/Mostrar_Jogador_Treinador';
import Mostrar_Jogador_Usuario from './pages/Mostrar_Jogador_Usuario/Mostrar_Jogador_Usuario';  
import MostrarTimeTreinador from './pages/Mostrar_Time_Treinador/Mostrar_Time_Treinador';
import MostrarTimeUsuario from './pages/Mostrar_Time_Usuario/Mostrar_Time_Usuario';
import CadastrarTimes from './pages/Cadastrar_Times/Cadastrar_Times';


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
        <Route path="/pesquisar_jogador" element={<Pesquisar_Jogador />} />
        <Route path="/pesquisar_times_usuario" element={<Pesquisar_Times_Usuario />} />
        <Route path="/pesquisar_campeonatos_usuario" element={<Pesquisar_Campeonatos_Usuario />} />
        <Route path="/pesquisar_times_treinador" element={<Pesquisar_Times_Treinador />} />
        <Route path="/pesquisar_campeonatos_treinador" element={<Pesquisar_Campeonatos_Treinador />} />
        <Route path="/modificar_tatica" element={<Modificar_Tatica />} />
        <Route path="/mostrar_jogador_treinador" element={<Mostrar_Jogador_Treinador />} />
        <Route path="/mostrar_jogador_usuario" element={<Mostrar_Jogador_Usuario />} />
        <Route path="/mostrar_time_treinador" element={<MostrarTimeTreinador />} />
        <Route path="/mostrar_time_usuario" element={<MostrarTimeUsuario />} />
        <Route path="/cadastrar_times" element={<CadastrarTimes />} />
        <Route path="*" element={<div>404 Not Found</div>} />
      </Routes>
    </Router>
  );
}

export default App;
