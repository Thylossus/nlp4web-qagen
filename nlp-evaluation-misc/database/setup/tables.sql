-- phpMyAdmin SQL Dump
-- version 4.4.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 07. Feb 2016 um 20:42
-- Server-Version: 5.5.44-MariaDB
-- PHP-Version: 5.5.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `nlpweb`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `answer`
--

CREATE TABLE IF NOT EXISTS `answer` (
  `id` int(11) NOT NULL,
  `type` enum('IB','DB','ES') NOT NULL,
  `answertext` varchar(255) NOT NULL,
  `question_id` int(11) NOT NULL,
  `correct` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `eval_session`
--

CREATE TABLE IF NOT EXISTS `eval_session` (
  `id` int(11) NOT NULL,
  `start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end` timestamp NULL DEFAULT NULL,
  `resume_key` char(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `eval_session_sets`
--

CREATE TABLE IF NOT EXISTS `eval_session_sets` (
  `questionset_id` int(11) NOT NULL,
  `eval_session_id` int(11) NOT NULL,
  `evaluate_as` enum('DB','ES') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `question`
--

CREATE TABLE IF NOT EXISTS `question` (
  `id` int(11) NOT NULL,
  `questiontext` text NOT NULL,
  `comment` text NOT NULL,
  `questionset_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `questionset`
--

CREATE TABLE IF NOT EXISTS `questionset` (
  `id` int(11) NOT NULL,
  `type` enum('IB','DB/ES') NOT NULL,
  `name` varchar(255) NOT NULL,
  `comment` text NOT NULL,
  `locked` set('IB','DB','ES') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `question_evaluation`
--

CREATE TABLE IF NOT EXISTS `question_evaluation` (
  `eval_session_id` int(11) NOT NULL,
  `question_id` int(11) NOT NULL,
  `chosen_answer_id` int(11) NOT NULL,
  `needed_time` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `settings`
--

CREATE TABLE IF NOT EXISTS `settings` (
  `param` varchar(128) NOT NULL,
  `value` varchar(255) NOT NULL,
  `description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `answer`
--
ALTER TABLE `answer`
  ADD PRIMARY KEY (`id`),
  ADD KEY `question_id` (`question_id`);

--
-- Indizes für die Tabelle `eval_session`
--
ALTER TABLE `eval_session`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `resume_key` (`resume_key`);

--
-- Indizes für die Tabelle `eval_session_sets`
--
ALTER TABLE `eval_session_sets`
  ADD PRIMARY KEY (`questionset_id`,`eval_session_id`),
  ADD KEY `eval_session_id` (`eval_session_id`);

--
-- Indizes für die Tabelle `question`
--
ALTER TABLE `question`
  ADD PRIMARY KEY (`id`),
  ADD KEY `questionset_id` (`questionset_id`);

--
-- Indizes für die Tabelle `questionset`
--
ALTER TABLE `questionset`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `question_evaluation`
--
ALTER TABLE `question_evaluation`
  ADD PRIMARY KEY (`eval_session_id`,`question_id`),
  ADD KEY `chosen_answer_id` (`chosen_answer_id`),
  ADD KEY `question_id` (`question_id`);

--
-- Indizes für die Tabelle `settings`
--
ALTER TABLE `settings`
  ADD PRIMARY KEY (`param`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `answer`
--
ALTER TABLE `answer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `eval_session`
--
ALTER TABLE `eval_session`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `question`
--
ALTER TABLE `question`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `questionset`
--
ALTER TABLE `questionset`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `answer`
--
ALTER TABLE `answer`
  ADD CONSTRAINT `answer_ibfk_1` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `eval_session_sets`
--
ALTER TABLE `eval_session_sets`
  ADD CONSTRAINT `eval_session_sets_ibfk_1` FOREIGN KEY (`questionset_id`) REFERENCES `questionset` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `eval_session_sets_ibfk_2` FOREIGN KEY (`eval_session_id`) REFERENCES `eval_session` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `question`
--
ALTER TABLE `question`
  ADD CONSTRAINT `question_ibfk_1` FOREIGN KEY (`questionset_id`) REFERENCES `questionset` (`id`) ON UPDATE CASCADE;

--
-- Constraints der Tabelle `question_evaluation`
--
ALTER TABLE `question_evaluation`
  ADD CONSTRAINT `question_evaluation_ibfk_1` FOREIGN KEY (`eval_session_id`) REFERENCES `eval_session` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `question_evaluation_ibfk_2` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `question_evaluation_ibfk_3` FOREIGN KEY (`chosen_answer_id`) REFERENCES `answer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Daten für Tabelle `settings`
--

INSERT INTO `settings` (`param`, `value`, `description`) VALUES
('EVALUATION_EVALS_PER_DB', '6', 'Amount of evaluations that every "Difficulty Baseline" question set should be measured before the phase of evaluating the real evaluation sets starts.'),
('EVALUATION_SESSION_SETS_AMOUNT', '3', 'Defines the amount of question sets that is used during every evaluation session, INCLUDING the individual baseline set.'),
('EVALUATION_TIMEOUT_LIMIT', '7200', 'Time before a evaluation session is regarded as timeout session, in seconds');

  
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
