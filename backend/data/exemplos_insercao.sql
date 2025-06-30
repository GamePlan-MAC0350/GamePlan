-- Exemplo de inserção de dados para testes do sistema GamePlan (PostgreSQL)
-- Apenas times e jogadores famosos: Real Madrid, Barcelona, Liverpool, Chelsea

-- Limpar tabelas e resetar sequências (opcional, para ambiente de testes)
TRUNCATE TABLE Jogador RESTART IDENTITY CASCADE;
TRUNCATE TABLE Team RESTART IDENTITY CASCADE;
TRUNCATE TABLE Tatica RESTART IDENTITY CASCADE;
TRUNCATE TABLE Tecnico RESTART IDENTITY CASCADE;

-- Técnicos (emails e senhas fáceis)
INSERT INTO Tecnico (nome, nacionalidade, data_nascimento, email, senha) VALUES
  ('Carlo Ancelotti', 'Itália', '1959-06-10', 'anc@gmail.com', '1234'),
  ('Xavi Hernández', 'Espanha', '1980-01-25', 'xav@gmail.com', '1234'),
  ('Jürgen Klopp', 'Alemanha', '1967-06-16', 'klo@gmail.com', '1234'),
  ('Mauricio Pochettino', 'Argentina', '1972-03-02', 'poc@gmail.com', '1234');

-- Táticas realistas
INSERT INTO Tatica (plano_jogo, conduta, instrucao_ataque, instrucao_defesa, instrucao_meio, pressao, estilo, tempo) VALUES
  ('4-3-3', 'Ofensiva', 'Ataque rápido', 'Pressão alta', 'Troca de passes', 8, 8, 90), -- Real Madrid
  ('4-3-3', 'Posse', 'Posse de bola', 'Linha alta', 'Construção curta', 7, 9, 90),    -- Barcelona
  ('4-3-3', 'Transição', 'Transição rápida', 'Pressão intensa', 'Lançamentos', 9, 7, 90), -- Liverpool
  ('3-4-3', 'Equilibrada', 'Ataque pelos lados', 'Defesa compacta', 'Troca de passes', 7, 7, 90); -- Chelsea

-- Times
INSERT INTO Team (nome, nacionalidade, data_fundacao, tecnico, tatica) VALUES
  ('Real Madrid', 'Espanha', '1902-03-06', 1, 1),
  ('Barcelona', 'Espanha', '1899-11-29', 2, 2),
  ('Liverpool', 'Inglaterra', '1892-06-03', 3, 3),
  ('Chelsea', 'Inglaterra', '1905-03-10', 4, 4);

-- Jogadores Real Madrid (id time = 1)
INSERT INTO Jogador (nome, altura, nacionalidade, data_nascimento, numero_camisa, posicao, pe_dominante, gols_totais, assistencias_totais, cartoes_amarelos, cartoes_vermelhos, clube) VALUES
  ('Thibaut Courtois', 199, 'Bélgica', '1992-05-11', 1, 'Goleiro', 'Esquerdo', 0, 0, 5, 0, 1),
  ('Dani Carvajal', 173, 'Espanha', '1992-01-11', 2, 'Lateral', 'Direito', 7, 35, 40, 1, 1),
  ('Éder Militão', 186, 'Brasil', '1998-01-18', 3, 'Zagueiro', 'Direito', 12, 5, 20, 0, 1),
  ('David Alaba', 180, 'Áustria', '1992-06-24', 4, 'Zagueiro', 'Esquerdo', 35, 30, 25, 0, 1),
  ('Ferland Mendy', 180, 'França', '1995-06-08', 23, 'Lateral', 'Esquerdo', 6, 12, 15, 0, 1),
  ('Luka Modric', 172, 'Croácia', '1985-09-09', 10, 'Meia', 'Direito', 38, 90, 40, 0, 1),
  ('Toni Kroos', 183, 'Alemanha', '1990-01-04', 8, 'Meia', 'Destro', 27, 95, 30, 0, 1),
  ('Federico Valverde', 182, 'Uruguai', '1998-07-22', 15, 'Meia', 'Direito', 18, 25, 18, 0, 1),
  ('Vinicius Jr', 176, 'Brasil', '2000-07-12', 7, 'Atacante', 'Direito', 60, 45, 10, 0, 1),
  ('Rodrygo', 174, 'Brasil', '2001-01-09', 11, 'Atacante', 'Direito', 35, 25, 7, 0, 1),
  ('Kylian Mbappé', 178, 'França', '1998-12-20', 9, 'Atacante', 'Direito', 250, 110, 20, 0, 1);

-- Jogadores Barcelona (id time = 2)
INSERT INTO Jogador (nome, altura, nacionalidade, data_nascimento, numero_camisa, posicao, pe_dominante, gols_totais, assistencias_totais, cartoes_amarelos, cartoes_vermelhos, clube) VALUES
  ('Marc-André ter Stegen', 187, 'Alemanha', '1992-04-30', 1, 'Goleiro', 'Destro', 0, 0, 4, 0, 2),
  ('Jules Koundé', 180, 'França', '1998-11-12', 23, 'Zagueiro', 'Direito', 7, 10, 15, 1, 2),
  ('Ronald Araújo', 188, 'Uruguai', '1999-03-07', 4, 'Zagueiro', 'Direito', 10, 5, 12, 1, 2),
  ('Alejandro Balde', 175, 'Espanha', '2003-10-18', 3, 'Lateral', 'Esquerdo', 2, 8, 6, 0, 2),
  ('João Cancelo', 182, 'Portugal', '1994-05-27', 2, 'Lateral', 'Direito', 15, 30, 20, 0, 2),
  ('Frenkie de Jong', 180, 'Holanda', '1997-05-12', 21, 'Meia', 'Destro', 25, 40, 12, 0, 2),
  ('Pedri', 174, 'Espanha', '2002-11-25', 8, 'Meia', 'Direito', 18, 25, 8, 0, 2),
  ('Gavi', 173, 'Espanha', '2004-08-05', 6, 'Meia', 'Destro', 7, 15, 10, 1, 2),
  ('Raphinha', 176, 'Brasil', '1996-12-14', 11, 'Atacante', 'Esquerdo', 40, 30, 8, 0, 2),
  ('Robert Lewandowski', 185, 'Polônia', '1988-08-21', 9, 'Atacante', 'Direito', 350, 80, 20, 0, 2),
  ('Lamine Yamal', 180, 'Espanha', '2007-07-13', 27, 'Atacante', 'Esquerdo', 5, 7, 2, 0, 2);

-- Jogadores Liverpool (id time = 3)
INSERT INTO Jogador (nome, altura, nacionalidade, data_nascimento, numero_camisa, posicao, pe_dominante, gols_totais, assistencias_totais, cartoes_amarelos, cartoes_vermelhos, clube) VALUES
  ('Alisson Becker', 191, 'Brasil', '1992-10-02', 1, 'Goleiro', 'Destro', 1, 1, 6, 0, 3),
  ('Trent Alexander-Arnold', 180, 'Inglaterra', '1998-10-07', 66, 'Lateral', 'Direito', 16, 70, 18, 0, 3),
  ('Virgil van Dijk', 193, 'Holanda', '1991-07-08', 4, 'Zagueiro', 'Direito', 35, 10, 25, 1, 3),
  ('Andrew Robertson', 178, 'Escócia', '1994-03-11', 26, 'Lateral', 'Esquerdo', 12, 55, 15, 0, 3),
  ('Ibrahima Konaté', 194, 'França', '1999-05-25', 5, 'Zagueiro', 'Direito', 5, 3, 10, 0, 3),
  ('Alexis Mac Allister', 176, 'Argentina', '1998-12-24', 10, 'Meia', 'Destro', 18, 20, 7, 0, 3),
  ('Dominik Szoboszlai', 186, 'Hungria', '2000-10-25', 8, 'Meia', 'Direito', 22, 25, 6, 0, 3),
  ('Curtis Jones', 185, 'Inglaterra', '2001-01-30', 17, 'Meia', 'Direito', 10, 12, 5, 0, 3),
  ('Mohamed Salah', 175, 'Egito', '1992-06-15', 11, 'Atacante', 'Esquerdo', 210, 110, 12, 0, 3),
  ('Darwin Núñez', 187, 'Uruguai', '1999-06-24', 9, 'Atacante', 'Destro', 60, 20, 8, 1, 3),
  ('Luis Díaz', 180, 'Colômbia', '1997-01-13', 7, 'Atacante', 'Direito', 40, 25, 7, 0, 3);

-- Jogadores Chelsea (id time = 4)
INSERT INTO Jogador (nome, altura, nacionalidade, data_nascimento, numero_camisa, posicao, pe_dominante, gols_totais, assistencias_totais, cartoes_amarelos, cartoes_vermelhos, clube) VALUES
  ('Robert Sánchez', 197, 'Espanha', '1997-11-18', 1, 'Goleiro', 'Destro', 0, 0, 3, 0, 4),
  ('Reece James', 182, 'Inglaterra', '1999-12-08', 24, 'Lateral', 'Direito', 11, 25, 15, 1, 4),
  ('Thiago Silva', 183, 'Brasil', '1984-09-22', 6, 'Zagueiro', 'Direito', 30, 10, 35, 1, 4),
  ('Ben Chilwell', 178, 'Inglaterra', '1996-12-21', 21, 'Lateral', 'Esquerdo', 10, 18, 10, 0, 4),
  ('Axel Disasi', 190, 'França', '1998-03-11', 2, 'Zagueiro', 'Direito', 7, 5, 8, 0, 4),
  ('Enzo Fernández', 178, 'Argentina', '2001-01-17', 8, 'Meia', 'Destro', 12, 20, 7, 0, 4),
  ('Moises Caicedo', 178, 'Equador', '2001-11-02', 25, 'Meia', 'Direito', 6, 10, 5, 0, 4),
  ('Conor Gallagher', 182, 'Inglaterra', '2000-02-06', 23, 'Meia', 'Direito', 9, 12, 8, 0, 4),
  ('Raheem Sterling', 170, 'Inglaterra', '1994-12-08', 17, 'Atacante', 'Direito', 130, 80, 18, 0, 4),
  ('Christopher Nkunku', 175, 'França', '1997-11-14', 18, 'Atacante', 'Direito', 60, 35, 10, 0, 4),
  ('Nicolas Jackson', 186, 'Senegal', '2001-06-20', 15, 'Atacante', 'Direito', 25, 10, 6, 0, 4);

-- Pronto para testes! Técnicos: anc@gmail.com/1234, xav@gmail.com/1234, klo@gmail.com/1234, poc@gmail.com/1234
