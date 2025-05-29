import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom'; 
import { data, useNavigate } from 'react-router-dom';
import './Mostrar_Jogador_Treinador.css';



function MostrarJogadorTreinador() {
    const navigate = useNavigate();
    const goToCadastrarJogadores = () => {
        navigate('/Cadastrar_Jogadores');
      };
    const goToCriarCampeonato = () => {
      navigate('/Criar_Campeonato');
    };
    const goToHome = () => {
      navigate('/');
    };
    const goToPesquisarTimesTreinador = () => {
      navigate('/Pesquisar_Times_Treinador');
    };
    const goToPesquisarCampeonatosTreinador = () => {
      navigate('/Pesquisar_Campeonatos_Treinador');
    };
    const goToModificarTatica = () => {
      navigate('/Modificar_Tatica');
    };
    const goToMostrarJogador = () => {
        navigate('/Mostrar_Jogador_Treinador', { state: { nomeJog: nome} });
      };
    const [nome, setNome] = useState('');
    
    //exemplo do banco de dados
    var jogador = { 
        nome: 'Cristiano Ronaldo', 
        time: 'Al Nassr', 
        nacionalidade: 'Português', 
        posicao: 'Atacante',
        altura: '1.87m',
        idade: '38 anos',
        numeroCamisa: '7',
        peDominante: 'Direito',
        gols: 800,
        assistencias: 230,
        partidasJogadas: 1100,
        minutosJogados: 90000,
        cartoesAmarelos: 100,
        cartoesVermelhos: 10,
     };






      const handleSubmit = (e) => {
        e.preventDefault();
        alert(`Nome do jogador: ${nome}`);
        
        //fazer a lógica para buscar o jogador no banco de dados
        // Aqui você pode fazer a lógica para enviar pro backend
      };
    
      const location = useLocation(); // dentro do componente
        const nomeRecebido = location.state?.nomeJog;

        useEffect(() => {
        document.body.classList.add('mostrar-jogador-treinador-page');

        if (nomeRecebido) {
            setNome(nomeRecebido);
        }

        return () => {
            document.body.classList.remove('mostrar-jogador-treinador-page');
        };
        }, [nomeRecebido]);

  return (
    <div className="mostrar-jogador-treinador-page">
     

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button className="botao-destaque" >Pesquisar Jogadores </button>
        <button onClick={goToPesquisarTimesTreinador}>Pesquisar Times</button>
        <button onClick={goToPesquisarCampeonatosTreinador}>Pesquisar Campeonatos</button>
        <button onClick={goToModificarTatica}>Modificar Táticas</button>
        <button>Registrar Resultados</button>
        <button onClick={goToCadastrarJogadores}>Cadastrar Jogadores </button>
        <button onClick={goToCriarCampeonato}>Criar Campeonato</button>
      </div>

      
      <div style={{ textAlign: 'center', marginTop: '150px' }}>
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

       
    <div className="jogador-info">
        <div className="jogador-header">
            <h2 className="jogador-nome">{jogador.nome}</h2>
            <h3 className="jogador-time">{jogador.time}</h3>
        </div>

        <div className="jogador-grid">
            <div className="jogador-card">
            <p><strong>Idade:</strong> {jogador.idade}</p>
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
            <p><strong>Gols:</strong> {jogador.gols}</p>
            <p><strong>Assistências:</strong> {jogador.assistencias}</p>
            </div>
            <div className="jogador-card">
            <p><strong>Partidas Jogadas:</strong> {jogador.partidasJogadas}</p>
            <p><strong>Minutos Jogados:</strong> {jogador.minutosJogados}</p>
            </div>
            <div className="jogador-card">
            <p><strong>Cartões Amarelos:</strong> {jogador.cartoesAmarelos}</p>
            <p><strong>Cartões Vermelhos:</strong> {jogador.cartoesVermelhos}</p>
            </div>
        </div>
    </div>



    </div>

    
  );
}

export default MostrarJogadorTreinador;