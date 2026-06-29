-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 06/06/2025 às 05:24
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `db_sistema`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `administrador`
--

CREATE TABLE `administrador` (
  `ID_Admin` int(11) NOT NULL,
  `Nome` varchar(100) NOT NULL,
  `RE` varchar(50) NOT NULL,
  `Senha` varchar(255) NOT NULL,
  `Cargo` varchar(50) DEFAULT 'Administrador'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `administrador`
--

INSERT INTO `administrador` (`ID_Admin`, `Nome`, `RE`, `Senha`, `Cargo`) VALUES
(1, 'Vitor', '0258', 'adm123', 'Administrador'),
(2, 'Paulo', '7894', 'Paulo2020', 'Administrador');

-- --------------------------------------------------------

--
-- Estrutura para tabela `emprestimo`
--

CREATE TABLE `emprestimo` (
  `ID_Emprestimo` int(11) NOT NULL,
  `ID_Funcionario` int(11) NOT NULL,
  `ID_Admin` int(11) NOT NULL,
  `Data_Retirada` date NOT NULL,
  `Data_Devolucao` date DEFAULT NULL,
  `Data_Prevista` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `emprestimo`
--

INSERT INTO `emprestimo` (`ID_Emprestimo`, `ID_Funcionario`, `ID_Admin`, `Data_Retirada`, `Data_Devolucao`, `Data_Prevista`) VALUES
(2, 1, 1, '2025-05-30', '2025-05-30', '2025-05-31'),
(3, 1, 1, '2025-05-30', '2025-05-30', '2025-05-30'),
(5, 1, 1, '2025-05-31', '2025-05-31', '2025-06-11'),
(6, 1, 1, '2025-05-31', '2025-05-31', '2025-05-31'),
(7, 11, 1, '2025-05-31', '2025-05-31', '2025-05-31'),
(8, 1, 1, '2025-05-31', '2025-05-31', '2025-05-31'),
(9, 1, 1, '2025-05-31', '2025-05-31', '2025-05-31'),
(10, 11, 1, '2025-05-31', '2025-05-31', '2025-06-01'),
(11, 11, 1, '2025-05-31', '2025-05-31', '2025-05-31'),
(12, 1, 1, '2025-05-31', '2025-05-31', '2025-05-31'),
(13, 17, 1, '2025-06-01', '2025-06-01', '2025-06-06'),
(14, 1, 2, '2025-06-01', '2025-06-01', '2025-06-02'),
(15, 1, 2, '2025-06-01', '2025-06-01', '2025-06-27'),
(16, 1, 1, '2025-06-04', '2025-06-04', '2025-06-04'),
(17, 1, 1, '2025-06-04', '2025-06-04', '2025-06-13'),
(18, 1, 1, '2025-06-04', '2025-06-04', '2025-06-02'),
(19, 1, 1, '2025-06-04', '2025-06-04', '2025-06-09'),
(20, 1, 1, '2025-06-04', '2025-06-04', '2025-06-01'),
(22, 1, 1, '2025-06-05', '2025-06-05', '2025-06-30'),
(24, 1, 1, '2025-06-06', '2025-06-06', '2025-06-06'),
(25, 1, 1, '2025-06-06', '2025-06-06', '2025-06-13'),
(26, 1, 1, '2025-06-06', NULL, '2025-06-26');

-- --------------------------------------------------------

--
-- Estrutura para tabela `emprestimo_item`
--

CREATE TABLE `emprestimo_item` (
  `ID_Emprestimo_Item` int(11) NOT NULL,
  `ID_Emprestimo` int(11) NOT NULL,
  `ID_Item` int(11) NOT NULL,
  `Status_Devolucao` varchar(20) NOT NULL DEFAULT 'pendente',
  `Observacoes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `emprestimo_item`
--

INSERT INTO `emprestimo_item` (`ID_Emprestimo_Item`, `ID_Emprestimo`, `ID_Item`, `Status_Devolucao`, `Observacoes`) VALUES
(1, 2, 1, 'devolvido', ''),
(2, 3, 12, 'devolvido', ''),
(3, 5, 1, 'devolvido', ''),
(4, 6, 12, 'devolvido', ''),
(5, 7, 2, 'devolvido', ''),
(6, 8, 2, 'devolvido', ''),
(7, 9, 1, 'devolvido', ''),
(8, 10, 1, 'devolvido', ''),
(9, 11, 1, 'devolvido', ''),
(10, 12, 1, 'devolvido', ''),
(11, 13, 1, 'devolvido', ''),
(12, 14, 2, 'devolvido', 'Devolveu quebrado\n'),
(13, 15, 34, 'devolvido', ''),
(14, 16, 1, 'devolvido', ''),
(15, 17, 1, 'devolvido', ''),
(16, 18, 1, 'devolvido', ''),
(17, 19, 1, 'devolvido', ''),
(18, 20, 1, 'devolvido', ''),
(20, 22, 1, 'devolvido', ''),
(22, 24, 1, 'devolvido', ''),
(24, 26, 34, 'pendente', '');

-- --------------------------------------------------------

--
-- Estrutura para tabela `funcionario`
--

CREATE TABLE `funcionario` (
  `ID_Funcionario` int(11) NOT NULL,
  `Nome` varchar(100) NOT NULL,
  `RE` varchar(50) NOT NULL,
  `Status_Func` varchar(20) NOT NULL,
  `Cargo` varchar(50) DEFAULT NULL,
  `Senha` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `funcionario`
--

INSERT INTO `funcionario` (`ID_Funcionario`, `Nome`, `RE`, `Status_Func`, `Cargo`, `Senha`) VALUES
(1, 'a', '1234', 'positivo', 'a', 'legal'),
(11, 'Joao', '4567', 'negativo', 'Professor', '123456'),
(17, 'Maria Paula', '96398', 'positivo', 'Professor', 'maria456');

-- --------------------------------------------------------

--
-- Estrutura para tabela `item`
--

CREATE TABLE `item` (
  `ID_Item` int(11) NOT NULL,
  `Nome` varchar(100) NOT NULL,
  `Categoria` varchar(50) NOT NULL,
  `Status_Item` varchar(20) NOT NULL DEFAULT 'ativo'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `item`
--

INSERT INTO `item` (`ID_Item`, `Nome`, `Categoria`, `Status_Item`) VALUES
(1, 'aa', 'Informática', 'ativo'),
(2, 'bb', 'categoria1', 'ativo'),
(12, 'asf', 'Escritório', 'ativo'),
(15, 'aaaaaaaa', 'Informática', 'ativo'),
(25, 'bobca', 'categoria2', 'ativo'),
(34, 'sexo', 'Audio e Vídeo', 'em manutenção'),
(39, 'aaaaaaaaaaaaaaaaaa', 'Chaves', 'ativo');

-- --------------------------------------------------------

--
-- Estrutura para tabela `manutencao`
--

CREATE TABLE `manutencao` (
  `ID_Manutencao` int(11) NOT NULL,
  `ID_Item` int(11) DEFAULT NULL,
  `Data_Entrada` date NOT NULL,
  `Status_Item` varchar(50) NOT NULL DEFAULT 'em manutenção',
  `Observacoes` text DEFAULT NULL,
  `Data_Saida` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `manutencao`
--

INSERT INTO `manutencao` (`ID_Manutencao`, `ID_Item`, `Data_Entrada`, `Status_Item`, `Observacoes`, `Data_Saida`) VALUES
(24, 34, '2025-06-06', 'em manutenção', '', NULL);

-- --------------------------------------------------------

--
-- Estrutura para tabela `reserva`
--

CREATE TABLE `reserva` (
  `ID_Reserva` int(11) NOT NULL,
  `ID_Funcionario` int(11) DEFAULT NULL,
  `ID_Item` int(11) DEFAULT NULL,
  `Data_Reserva` date NOT NULL,
  `Status_Reserva` varchar(20) DEFAULT NULL CHECK (`Status_Reserva` in ('pendente','aceita','recusado','fechada','cancelada')),
  `Observacoes` text DEFAULT NULL,
  `Data_Retirada_Solicitada` date DEFAULT NULL,
  `Cancelado` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `reserva`
--

INSERT INTO `reserva` (`ID_Reserva`, `ID_Funcionario`, `ID_Item`, `Data_Reserva`, `Status_Reserva`, `Observacoes`, `Data_Retirada_Solicitada`, `Cancelado`) VALUES
(12, 1, 12, '2025-05-18', 'recusado', NULL, NULL, 0),
(18, 1, 2, '2025-05-18', 'recusado', NULL, NULL, 0),
(22, 1, 15, '2025-05-25', 'recusado', 'Vc é bobo cara de melao', NULL, 0),
(23, 1, 15, '2025-05-25', 'recusado', NULL, NULL, 0),
(30, 1, 2, '2025-05-25', 'recusado', 'a', NULL, 0),
(34, 1, 12, '2025-05-27', 'fechada', NULL, NULL, 1),
(35, 1, 12, '2025-05-28', 'cancelada', NULL, NULL, 0),
(36, 1, 12, '2025-05-28', 'cancelada', NULL, NULL, 0),
(37, 1, 12, '2025-05-28', 'fechada', NULL, NULL, 0),
(38, 1, 15, '2025-05-28', 'fechada', NULL, NULL, 0),
(39, 1, 12, '2025-05-28', 'fechada', NULL, NULL, 0),
(40, 1, 15, '2025-05-28', 'fechada', NULL, NULL, 0),
(41, 1, 25, '2025-05-28', 'fechada', NULL, NULL, 0),
(42, 1, 25, '2025-05-28', 'fechada', NULL, NULL, 0),
(43, 1, 1, '2025-05-28', 'fechada', NULL, NULL, 0),
(44, 1, 2, '2025-05-31', 'recusado', '', NULL, 0),
(45, 1, 12, '2025-05-31', 'recusado', '', NULL, 0),
(46, 1, 2, '2025-05-31', 'recusado', '', NULL, 0),
(47, 11, 15, '2025-05-31', 'recusado', '', NULL, 0),
(48, 11, 12, '2025-05-31', 'recusado', '', NULL, 0),
(49, 11, 2, '2025-05-31', 'recusado', '', NULL, 0),
(50, 11, 25, '2025-05-31', 'recusado', '', NULL, 0),
(51, 11, 34, '2025-05-31', 'recusado', '', NULL, 0),
(52, 11, 2, '2025-05-31', 'cancelada', NULL, NULL, 0),
(53, 11, 2, '2025-05-31', 'cancelada', NULL, NULL, 0),
(54, 11, 12, '2025-05-31', 'recusado', '', NULL, 0),
(55, 11, 12, '2025-05-31', 'recusado', '', NULL, 0),
(56, 11, 12, '2025-05-31', 'recusado', '', NULL, 0),
(57, 11, 12, '2025-05-31', 'recusado', '', NULL, 0),
(58, 11, 12, '2025-05-31', 'recusado', '', NULL, 0),
(59, 11, 25, '2025-05-31', 'recusado', 'as', NULL, 0),
(60, 11, 12, '2025-05-31', 'recusado', '', NULL, 0),
(61, 11, 15, '2025-05-31', 'cancelada', NULL, NULL, 0),
(62, 11, 2, '2025-05-31', 'recusado', '', NULL, 0),
(63, 11, 2, '2025-05-31', 'cancelada', NULL, NULL, 0),
(64, 11, 12, '2025-05-31', 'cancelada', NULL, NULL, 0),
(65, 1, 12, '2025-06-01', 'fechada', NULL, NULL, 0),
(66, 1, 12, '2025-06-01', 'fechada', NULL, NULL, 0),
(67, 1, 12, '2025-06-05', 'recusado', '', NULL, 0),
(68, 1, 12, '2025-06-05', 'recusado', '', NULL, 0),
(69, 1, 12, '2025-06-05', 'recusado', '', NULL, 0),
(70, 1, 12, '2025-06-05', 'recusado', '', NULL, 0),
(71, 1, 12, '2025-06-05', 'fechada', NULL, NULL, 0),
(74, 17, 1, '2025-06-06', 'fechada', NULL, NULL, 0),
(75, 17, 1, '2025-06-06', 'fechada', NULL, NULL, 0),
(76, 17, 1, '2025-06-06', 'fechada', NULL, NULL, 0),
(77, 17, 2, '2025-06-06', 'pendente', NULL, NULL, 0),
(78, 17, 39, '2025-06-06', 'aceita', NULL, NULL, 0);

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `administrador`
--
ALTER TABLE `administrador`
  ADD PRIMARY KEY (`ID_Admin`);

--
-- Índices de tabela `emprestimo`
--
ALTER TABLE `emprestimo`
  ADD PRIMARY KEY (`ID_Emprestimo`),
  ADD KEY `ID_Funcionario` (`ID_Funcionario`),
  ADD KEY `ID_Admin` (`ID_Admin`);

--
-- Índices de tabela `emprestimo_item`
--
ALTER TABLE `emprestimo_item`
  ADD PRIMARY KEY (`ID_Emprestimo_Item`),
  ADD KEY `ID_Emprestimo` (`ID_Emprestimo`),
  ADD KEY `ID_Item` (`ID_Item`);

--
-- Índices de tabela `funcionario`
--
ALTER TABLE `funcionario`
  ADD PRIMARY KEY (`ID_Funcionario`),
  ADD UNIQUE KEY `RE` (`RE`);

--
-- Índices de tabela `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`ID_Item`);

--
-- Índices de tabela `manutencao`
--
ALTER TABLE `manutencao`
  ADD PRIMARY KEY (`ID_Manutencao`),
  ADD KEY `ID_Item` (`ID_Item`);

--
-- Índices de tabela `reserva`
--
ALTER TABLE `reserva`
  ADD PRIMARY KEY (`ID_Reserva`),
  ADD KEY `ID_Funcionario` (`ID_Funcionario`),
  ADD KEY `ID_Item` (`ID_Item`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `administrador`
--
ALTER TABLE `administrador`
  MODIFY `ID_Admin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de tabela `emprestimo`
--
ALTER TABLE `emprestimo`
  MODIFY `ID_Emprestimo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT de tabela `emprestimo_item`
--
ALTER TABLE `emprestimo_item`
  MODIFY `ID_Emprestimo_Item` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT de tabela `funcionario`
--
ALTER TABLE `funcionario`
  MODIFY `ID_Funcionario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT de tabela `item`
--
ALTER TABLE `item`
  MODIFY `ID_Item` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT de tabela `manutencao`
--
ALTER TABLE `manutencao`
  MODIFY `ID_Manutencao` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT de tabela `reserva`
--
ALTER TABLE `reserva`
  MODIFY `ID_Reserva` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=79;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `emprestimo`
--
ALTER TABLE `emprestimo`
  ADD CONSTRAINT `emprestimo_ibfk_1` FOREIGN KEY (`ID_Funcionario`) REFERENCES `funcionario` (`ID_Funcionario`),
  ADD CONSTRAINT `emprestimo_ibfk_2` FOREIGN KEY (`ID_Admin`) REFERENCES `administrador` (`ID_Admin`);

--
-- Restrições para tabelas `emprestimo_item`
--
ALTER TABLE `emprestimo_item`
  ADD CONSTRAINT `emprestimo_item_ibfk_1` FOREIGN KEY (`ID_Emprestimo`) REFERENCES `emprestimo` (`ID_Emprestimo`),
  ADD CONSTRAINT `emprestimo_item_ibfk_2` FOREIGN KEY (`ID_Item`) REFERENCES `item` (`ID_Item`);

--
-- Restrições para tabelas `manutencao`
--
ALTER TABLE `manutencao`
  ADD CONSTRAINT `manutencao_ibfk_1` FOREIGN KEY (`ID_Item`) REFERENCES `item` (`ID_Item`);

--
-- Restrições para tabelas `reserva`
--
ALTER TABLE `reserva`
  ADD CONSTRAINT `reserva_ibfk_1` FOREIGN KEY (`ID_Funcionario`) REFERENCES `funcionario` (`ID_Funcionario`),
  ADD CONSTRAINT `reserva_ibfk_2` FOREIGN KEY (`ID_Item`) REFERENCES `item` (`ID_Item`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
