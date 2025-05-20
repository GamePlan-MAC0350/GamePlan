import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Pesquisar_Jogador.css';
function Pesquisar_Jogador() {
  const navigate = useNavigate();
  const [nome, setNome] = useState('');
  
  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`Nome do jogador: ${nome}`);
    // Aqui você pode fazer a lógica para enviar pro backend
  };

  useEffect(() => {
    document.body.classList.add('pesquisar-jogador-page');
    return () => {
    document.body.classList.remove('pesquisar-jogador-page');
    };
  }, []);

  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>Pesquise o jogador: </h1>
      <form onSubmit={handleSubmit}>
        <div className="input-group">
            <p>Nome do jogador: </p>
            <input
            type="text"
            placeholder="ex: Cristiano Ronaldo"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
            />
            <button className="botao-pesquisa" type="submit" />
        </div>
    </form>
    </div>
  );
}

export default Pesquisar_Jogador;
