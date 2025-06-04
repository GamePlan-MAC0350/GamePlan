CREATE TABLE Campeonato (
    id SERIAL PRIMARY KEY,
    numero_times INT NOT NULL,
    premio VARCHAR(255) NOT NULL,
    pontos INT NOT NULL,
    data_comeco VARCHAR(255) NOT NULL,
    data_final VARCHAR(255) NOT NULL,
    data_inscricao VARCHAR(255) NOT NULL,
    campeao VARCHAR(255),
    artilheiro VARCHAR(255),
    maior_assistencia VARCHAR(255)
);

--Tirei o partidas_jogadas_totais e minutos_jogados_totais, pois não são necessários para o campeonato
CREATE TABLE Jogador (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    altura INT NOT NULL,
    nacionalidade VARCHAR(255) NOT NULL,
    data_nascimento VARCHAR(255) NOT NULL,
    numero_camisa INT NOT NULL,
    posicao VARCHAR(255) NOT NULL,
    pe_dominante VARCHAR(255) NOT NULL,
    gols_totais INT NOT NULL DEFAULT 0,
    assistencias_totais INT NOT NULL DEFAULT 0,
    cartoes_amarelos INT NOT NULL DEFAULT 0,
    cartoes_vermelhos INT NOT NULL DEFAULT 0,
    clube INT REFERENCES Team(id)
);

CREATE TABLE Team (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    nacionalidade VARCHAR(255) NOT NULL,
    data_fundacao VARCHAR(255) NOT NULL,
    artilheiro INT REFERENCES Jogador(id),
    maior_assistente INT REFERENCES Jogador(id),
    partidas_jogadas_totais INT NOT NULL DEFAULT 0,
    gols_marcados INT NOT NULL DEFAULT 0,
    gols_sofridos INT NOT NULL DEFAULT 0,
    pontos INT NOT NULL DEFAULT 0,
    vitorias INT NOT NULL DEFAULT 0,
    derrotas INT NOT NULL DEFAULT 0,
    tatica INT REFERENCES Tatica(id),
    tecnico INT REFERENCES Tecnico(id),
    campeonato INT REFERENCES Campeonato(id)
);

-- tirei a data_contratacao
CREATE TABLE Tecnico (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    nacionalidade VARCHAR(255) NOT NULL,
    data_nascimento VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    clube INT REFERENCES Team(id),
    senha VARCHAR(255) NOT NULL,
);

CREATE TABLE Tatica (
    id SERIAL PRIMARY KEY,
    plano_jogo VARCHAR(255) NOT NULL DEFAULT 'Posse de Bola',
    conduta VARCHAR(255) NOT NULL DEFAULT 'Agressivo',
    instrucao_ataque VARCHAR(255) NOT NULL DEFAULT 'Apenas Ataque',
    instrucao_defesa VARCHAR(255) NOT NULL DEFAULT 'Marcação Alta',
    instrucao_meio VARCHAR(255) NOT NULL DEFAULT 'Apoiar a Defesa',
    pressao INT NOT NULL DEFAULT 50,
    estilo INT NOT NULL DEFAULT 50,
    tempo INT NOT NULL DEFAULT 50
);

CREATE TABLE Partida (
    id SERIAL PRIMARY KEY,
    time_1 INT REFERENCES Team(id),
    time_2 INT REFERENCES Team(id),
    gols_time_1 INT NOT NULL DEFAULT 0,
    gols_time_2 INT NOT NULL DEFAULT 0,
    local VARCHAR(255) NOT NULL,
    data_partida VARCHAR(255) NOT NULL,
    hora_partida VARCHAR(255) NOT NULL,
    empate BOOLEAN NOT NULL DEFAULT FALSE,
    gols_time_1_penaltis INT NOT NULL DEFAULT 0,
    gols_time_2_penaltis INT NOT NULL DEFAULT 0,
    vencedor INT REFERENCES Team(id),
    campeonato INT REFERENCES Campeonato(id)
);