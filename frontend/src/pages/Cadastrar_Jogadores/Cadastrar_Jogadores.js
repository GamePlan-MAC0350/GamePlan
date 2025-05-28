import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Cadastrar_Jogadores.css';
function Cadastrar_Jogadores() {
  const navigate = useNavigate();
  const [nome, setNome] = useState('');
  const [nacionalidade, setNacionalidade] = useState('');
  const [time, setTime] = useState('');
  const [posicao, setPosição] = useState('');
  const [altura, setAltura] = useState('');
  const [dataNascimento, setDataNascimento] = useState('');
  const [numeroCamisa, setNumeroCamisa] = useState('');
  const [peDominante, setPeDominante] = useState('');

  const goToCadastrarJogadores = () => {
    navigate('/Cadastrar_Jogadores');
  };
const goToCriarCampeonato = () => {
  navigate('/Criar_Campeonato');
};
const goToHomeTreinador = () => {
  navigate('/Home_Treinador');
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
  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`Nome: ${nome}\nTime: ${time}\nNacionalidade: ${nacionalidade}\nPosição: ${posicao}\nAltura: ${altura}\nData de Nascimento: ${dataNascimento}\nNúmero da camisa: ${numeroCamisa}\nPé Dominante: ${peDominante}`);
    // Aqui você pode fazer a lógica para enviar pro backend
  };

  useEffect(() => {
    document.body.classList.add('cadastrar-jogadores-page');
    return () => {
      document.body.classList.remove('cadastrar-jogadores-page');
    };
  }, []);

  return (

    <div className="cadastrar-jogadores-page">
      <h1></h1>

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button onClick={goToHomeTreinador} >Pesquisar Jogadores </button>
        <button onClick={goToPesquisarTimesTreinador}>Pesquisar Times</button>
        <button onClick={goToPesquisarCampeonatosTreinador}>Pesquisar Campeonatos</button>
        <button onClick={goToModificarTatica}>Modificar Táticas</button>
        <button>Registrar Resultados</button>
        <button className="botao-destaque">Cadastrar Jogadores </button>
        <button onClick={goToCriarCampeonato}>Criar Campeonato</button>
      </div>

    <div style={{ textAlign: 'center', marginTop: '150px' }}>
      <h1>Preencha os dados do jogador: </h1>
      <form onSubmit={handleSubmit}>
      <div>
        <p>Nome completo:
          <input
            type="String"
            placeholder="ex: Cristiano Ronaldo"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Time:
          <input
            type="String"
            placeholder="ex: Manchester United"
            value={time}
            onChange={(e) => setTime(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Nacionalidade:
          <input
            type="String"
            placeholder="ex: Português"
            value={nacionalidade}
            onChange={(e) => setNacionalidade(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Posição:
          <input
            type="String"
            placeholder="ex: Atacante, Meia, Lateral, Zagueiro, Goleiro"
            value={posicao}
            onChange={(e) => setPosição(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Altura em cm:
          <input
            type="Integer"
            placeholder="ex: 180"
            value={altura}
            onChange={(e) => setAltura(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Data de Nascimento:
          <input
            type="String"
            placeholder="ex: 30/01/2000"
            value={dataNascimento}
            onChange={(e) => setDataNascimento(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Número da camisa:
          <input
            type="Integer"
            placeholder="ex: 7"
            value={numeroCamisa}
            onChange={(e) => setNumeroCamisa(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <div>
        <p>Pé Dominante:
          <input
            type="String"
            placeholder="ex: Direito ou Esquerdo"
            value={peDominante}
            onChange={(e) => setPeDominante(e.target.value)}
            required
            style={{ padding: '8px', margin: '10px' }}
          />
          </p>
        </div>
        <button type="submit" style={{ padding: '8px 16px' }}>
          Registar Jogador       </button>
      </form>
    </div>
  </div>
  );
}

export default Cadastrar_Jogadores;
