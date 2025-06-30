import React, { useState } from 'react';
import { useLocation } from 'react-router-dom'; 
import { useNavigate } from 'react-router-dom';
import './Mostrar_Jogador_Usuario.css';

function Mostrar_Jogador_Usuario() {
    const navigate = useNavigate();
    const location = useLocation();
    const [nome, setNome] = useState(location.state?.nomeJog || '');
    const [jogador, setJogador] = useState(null);
    const [nomeTime, setNomeTime] = useState('');

    const goToPesquisarTimesUsuario = () => {
      navigate('/pesquisar_times_usuario');
    };
    const goToHome = () => {
      navigate('/');
    };
    const goToPesquisarCampeonatosUsuario = () => {
      navigate('/pesquisar_campeonatos_usuario');
    };

    // Função para buscar jogador por nome
    const buscarJogadorPorNome = async (nomeBusca) => {
        console.log('[DEBUG] Iniciando busca do jogador com nome:', nomeBusca);
        try {
            const resp = await fetch(`http://localhost:8080/jogadores?nome=${encodeURIComponent(nomeBusca)}`);
            console.log('[DEBUG] Resposta recebida do backend:', resp);
            if (resp.ok) {
                const data = await resp.json();
                setJogador(data);
                setNomeTime(data.nomeTime || '');
                console.log('[DEBUG] Dados do jogador recebidos:', data, 'Nome do time:', data.nomeTime);
            } else {
                const erro = await resp.text();
                console.log('[DEBUG] Erro ao buscar jogador:', erro);
                setJogador(null);
                setNomeTime('');
                alert('Jogador não encontrado!');
            }
        } catch (err) {
            console.log('[DEBUG] Erro de conexão ao buscar jogador:', err);
            setJogador(null);
            setNomeTime('');
            alert('Erro de conexão com o backend!');
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('[DEBUG] handleSubmit chamado com nome:', nome);
        if (nome) {
            buscarJogadorPorNome(nome);
        }
    };

    // useEffect para buscar automaticamente apenas na primeira navegação com nomeJog
    React.useEffect(() => {
        if (location.state?.nomeJog) {
            buscarJogadorPorNome(location.state.nomeJog);
        }
        // eslint-disable-next-line
    }, []);

    return (
    <div className="mostrar-jogador-usuario-page">
      <h1></h1>
      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button onClick={() => {}} className="botao-destaque">Pesquisar Jogadores</button>
        <button onClick={goToPesquisarTimesUsuario}>Pesquisar Times</button>
        <button onClick={goToPesquisarCampeonatosUsuario}>Pesquisar Campeonatos</button>
      </div>
      <div className="busca-jogador-container">
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
            <button className="botao-pesquisa" type="submit"></button>
          </div>
        </form>
      </div>
      {/* Exibe informações do jogador se encontrado */}
      {jogador && (
      <div className="jogador-info" style={{ marginTop: 10 }}>
        <div className="jogador-header">
            <h2 className="jogador-nome">{jogador.nome}</h2>
            <h3 className="jogador-time">{nomeTime ? nomeTime : 'Sem clube'}</h3>
        </div>
        <div className="jogador-grid">
            <div className="jogador-card">
            <p><strong>Data de Nascimento:</strong> {jogador.dataNascimento}</p>
            <p><strong>Nacionalidade:</strong> {jogador.nacionalidade}</p>
            </div>
            <div className="jogador-card">
            <p><strong>Posição:</strong> {jogador.posicao}</p>
            <p><strong>Número da Camisa:</strong> {jogador.numeroCamisa}</p>
            </div>
            <div className="jogador-card">
            <p><strong>Altura:</strong> {jogador.altura}</p>
            <p><strong>Pé Dominante:</strong> {jogador.peDominante}</p>
            </div>
            <div className="jogador-card">
            <p><strong>Gols:</strong> {jogador.golsTotais}</p>
            <p><strong>Assistências:</strong> {jogador.assistenciasTotais}</p>
            </div>
            <div className="jogador-card">
            <p><strong>Cartões Amarelos:</strong> {jogador.cartoesAmarelos}</p>
            <p><strong>Cartões Vermelhos:</strong> {jogador.cartoesVermelhos}</p>
            </div>
        </div>
      </div>
      )}
    </div>
    );
}

export default Mostrar_Jogador_Usuario;