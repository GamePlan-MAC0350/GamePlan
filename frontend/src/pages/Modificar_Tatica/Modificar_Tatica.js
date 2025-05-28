import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Modificar_Tatica.css';
function Modificar_Tatica() {
  const navigate = useNavigate();
  const [plano_jogo, setPlanoJogo] = useState('');
  const [conduta, setConduta] = useState('');
  const [instrucao_ataq, setInstrucaoAtaq] = useState('');
  const [instrucao_meio, setInstrucaoMeio] = useState('');
  const [instrucao_def, setInstrucaoDef] = useState('');

  const [pressao, setPressao] = useState(50);
  const [estilo, setEstilo] = useState(50);
  const [tempo, setTempo] = useState(50);

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

  const handleSubmit = (e) => {
    e.preventDefault();
    //alert(`Nome: ${nome}\nTime: ${time}\nNacionalidade: ${nacionalidade}\nPosição: ${posicao}\nAltura: ${altura}\nData de Nascimento: ${dataNascimento}\nNúmero da camisa: ${numeroCamisa}\nPé Dominante: ${peDominante}`);
    // Aqui você pode fazer a lógica para enviar pro backend
  };

  useEffect(() => {
    document.body.classList.add('modificar-tatica-page');
    return () => {
      document.body.classList.remove('modificar-tatica-page');
    };
  }, []);
  
  const mensagemPressao = ["Sem pressionar", "Pressão leve", "Pressão moderada", "Pressão alta", "Pressão extrema"];
  const mensagemEstilo = ["Retranca", "Defensivo", "Equilibrado", "Ofensivo", "Ataque total"];
  const mensagemTempo = ["Jogar na defesa", "Construção Lenta", "Posse de Bola", "Passes Rápidos", "Passes de Primeira"];
  var mp = mensagemPressao[parseInt(pressao / 20.000001)];
  var me = mensagemEstilo[parseInt(estilo / 20.0000001)];
  var mt = mensagemTempo[parseInt(tempo / 20.0000001)];
  
  return (

    <div className="modificar-tatica-page">
      

      <div className="button-grid">
        <button className="botao-imagem" onClick={goToHome}></button>
        <button onClick={goToHomeTreinador}>Pesquisar Jogadores </button>
        <button onClick={goToPesquisarTimesTreinador}>Pesquisar Times</button>
        <button onClick={goToPesquisarCampeonatosTreinador}>Pesquisar Campeonatos</button>
        <button className="botao-destaque">Modificar Táticas</button>
        <button>Registrar Resultados</button>
        <button onClick={goToCadastrarJogadores}>Cadastrar Jogadores </button>
        <button onClick={goToCriarCampeonato}>Criar Campeonato</button>
      </div>
    
    <div style={{ textAlign: 'center', marginTop: '150px' }}>
      <h1>Modifique a Tática: </h1>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '1px' }}>
        <div style={{ width: '45%' }}>
          
          <form onSubmit={handleSubmit}>
            <div>
              <p>Defina o plano de jogo:
                <input
                  type="String"
                  placeholder="ex: Posse de bola, Jogo direto"
                  value={plano_jogo}
                  onChange={(e) => setPlanoJogo(e.target.value)}
                  required
                  style={{ padding: '8px', margin: '10px' }}
                />
              </p>
            </div>
            <div>
              <p>Defina a conduta de marcação:
                <input
                  type="String"
                  placeholder="ex: Agressivo, Cuidadoso, Normal"
                  value={conduta}
                  onChange={(e) => setConduta(e.target.value)}
                  required
                  style={{ padding: '8px', margin: '10px' }}
                />
              </p>
            </div>
            <div>
              <p>Defina a instrução de ataque:
                <input
                  type="String"
                  placeholder="ex: Apoiar o meio-campo, Apenas atacar"
                  value={instrucao_ataq}
                  onChange={(e) => setInstrucaoAtaq(e.target.value)}
                  required
                  style={{ padding: '8px', margin: '10px' }}
                />
              </p>
            </div>
            <div>
              <p>Defina a instrução do meio-campo:
                <input
                  type="String"
                  placeholder="ex: Apoiar a defesa, Ficar em posição"
                  value={instrucao_meio}
                  onChange={(e) => setInstrucaoMeio(e.target.value)}
                  required
                  style={{ padding: '8px', margin: '10px' }}
                />
              </p>
            </div>
            <div>
              <p>Defina a instrução de defesa:
                <input
                  type="String"
                  placeholder="ex: Marcação alta, Marcação baixa"
                  value={instrucao_def}
                  onChange={(e) => setInstrucaoDef(e.target.value)}
                  required
                  style={{ padding: '8px', margin: '10px' }}
                />
              </p>
            </div>
          </form>
        </div>

        <div style={{ width: '50%' }}>
          
          <form onSubmit={handleSubmit}>
            <div>
              <h4>Defina o nível de pressão:
                <p></p>
                <p>{mp}</p>
                <p></p>
                <input
                  type="range"
                  min="1"
                  max="100"
                  value={pressao}
                  onChange={(e) => setPressao(e.target.value)}
                  required
                  style={{ padding: '8px', margin: '10px' }}
                />
                <span>{pressao}</span>
              </h4>
            </div>
            <div>
              <h4>Defina o estilo de jogo:
                <p>{me}</p>
                <input
                  type="range"
                  min="1"
                  max="100"
                  value={estilo}
                  onChange={(e) => setEstilo(e.target.value)}
                  required
                  style={{ padding: '8px', margin: '10px' }}
                />
                <span>{estilo}</span>
              </h4>
            </div>
            <p></p>
            <div>
              <h4>Defina o tempo de passe:
                <p>{mt}</p>
                <input
                  type="range"
                  min="1"
                  max="100"
                  value={tempo}
                  onChange={(e) => setTempo(e.target.value)}
                  required
                  style={{ padding: '8px', margin: '10px' }}
                />
                <span>{tempo}</span>
              </h4>
            </div>
            
          </form>
        </div>
      </div>
      <div>
        <button type="submit" className="botao-destaque">Salvar Tática</button>
    </div>
    </div>
    </div>
  );
}

export default Modificar_Tatica;
