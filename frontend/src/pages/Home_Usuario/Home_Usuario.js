import React from 'react';
import './Home_Usuario.css';
function HomeUsuario() {
  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>O que você deseja pesquisar?</h1>
      <div style={{ marginTop: '20px' }}>
        <button style={{ padding: '10px 20px', margin: '10px' }}>Jogadores</button>
        <button style={{ padding: '10px 20px', margin: '10px' }}>Times</button>
        <button style={{ padding: '10px 20px', margin: '10px' }}>Campeonatos</button>
      </div>
    </div>
  );
}

export default HomeUsuario;