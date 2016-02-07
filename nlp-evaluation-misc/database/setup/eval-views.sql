-- phpMyAdmin SQL Dump
-- version 4.4.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 07. Feb 2016 um 20:41
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
-- Stellvertreter-Struktur des Views `v_all_evals`
--
CREATE TABLE IF NOT EXISTS `v_all_evals` (
`session_id` int(11)
,`start` timestamp
,`end` timestamp
,`evaluate_as` varchar(2)
,`questionset_id` int(11)
,`questionset_name` varchar(255)
,`questionset_type` enum('IB','DB/ES')
,`question_id` int(11)
,`questiontext` text
,`correct_answer_id` int(11)
,`answertext` varchar(255)
,`chosen_answer_id` int(11)
,`correct` int(1)
,`needed_time` int(11)
);

-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `v_db`
--
CREATE TABLE IF NOT EXISTS `v_db` (
`questionset_id` int(11)
,`correct` decimal(14,4)
);

-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `v_diff`
--
CREATE TABLE IF NOT EXISTS `v_diff` (
`session_id` int(11)
,`questionset_id` int(11)
,`correct_ib` decimal(14,4)
,`correct_db` decimal(14,4)
,`correct_es` decimal(14,4)
,`exp` decimal(19,8)
,`diff` decimal(20,8)
);

-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `v_es`
--
CREATE TABLE IF NOT EXISTS `v_es` (
`session_id` int(11)
,`questionset_id` int(11)
,`correct` decimal(14,4)
);

-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `v_ib`
--
CREATE TABLE IF NOT EXISTS `v_ib` (
`session_id` int(11)
,`correct` decimal(14,4)
);

-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `v_quest_corr`
--
CREATE TABLE IF NOT EXISTS `v_quest_corr` (
`question_id` int(11)
,`questionset_id` int(11)
,`evaluate_as` varchar(2)
,`correct` decimal(14,4)
);

-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `v_quest_exp`
--
CREATE TABLE IF NOT EXISTS `v_quest_exp` (
`session_id` int(11)
,`question_id` int(11)
,`questionset_id` int(11)
,`correct_ib` decimal(14,4)
,`correct_db` decimal(18,8)
,`evaluated_as` varchar(2)
,`correct_exp` decimal(23,12)
,`correct` int(1)
);

-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `v_quest_perf`
--
CREATE TABLE IF NOT EXISTS `v_quest_perf` (
`question_id` int(11)
,`questionset_id` int(11)
,`correct_db` decimal(22,12)
,`correct_es` decimal(14,4)
,`correct_diff` decimal(23,12)
,`correct_es_exp` decimal(27,16)
,`es_perf` decimal(34,8)
);

-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `v_quest_perf_helper`
--
CREATE TABLE IF NOT EXISTS `v_quest_perf_helper` (
`question_id` int(11)
,`questionset_id` int(11)
,`correct_db` decimal(22,12)
,`correct_es_exp` decimal(27,16)
);

-- --------------------------------------------------------

--
-- Struktur des Views `v_all_evals`
--
DROP TABLE IF EXISTS `v_all_evals`;

CREATE ALGORITHM=UNDEFINED DEFINER=`nlpweb`@`localhost` SQL SECURITY DEFINER VIEW `v_all_evals` AS select `es`.`id` AS `session_id`,`es`.`start` AS `start`,`es`.`end` AS `end`,coalesce(`ess`.`evaluate_as`,'IB') AS `evaluate_as`,`qs`.`id` AS `questionset_id`,`qs`.`name` AS `questionset_name`,`qs`.`type` AS `questionset_type`,`q`.`id` AS `question_id`,`q`.`questiontext` AS `questiontext`,`a`.`id` AS `correct_answer_id`,`a`.`answertext` AS `answertext`,`qe`.`chosen_answer_id` AS `chosen_answer_id`,(`a`.`id` = `qe`.`chosen_answer_id`) AS `correct`,`qe`.`needed_time` AS `needed_time` from (((((`eval_session` `es` join `eval_session_sets` `ess` on((`ess`.`eval_session_id` = `es`.`id`))) join `questionset` `qs` on((`qs`.`id` = `ess`.`questionset_id`))) join `question` `q` on((`q`.`questionset_id` = `qs`.`id`))) join `answer` `a` on(((`a`.`question_id` = `q`.`id`) and ((`a`.`type` = `ess`.`evaluate_as`) or ((`a`.`type` = 'IB') and isnull(`ess`.`evaluate_as`))) and (`a`.`correct` = '1')))) join `question_evaluation` `qe` on(((`qe`.`eval_session_id` = `es`.`id`) and (`qe`.`question_id` = `q`.`id`)))) where (`es`.`end` is not null);

-- --------------------------------------------------------

--
-- Struktur des Views `v_db`
--
DROP TABLE IF EXISTS `v_db`;

CREATE ALGORITHM=UNDEFINED DEFINER=`nlpweb`@`localhost` SQL SECURITY DEFINER VIEW `v_db` AS select `v_all_evals`.`questionset_id` AS `questionset_id`,avg(`v_all_evals`.`correct`) AS `correct` from `v_all_evals` where (`v_all_evals`.`evaluate_as` = 'DB') group by `v_all_evals`.`questionset_id`;

-- --------------------------------------------------------

--
-- Struktur des Views `v_diff`
--
DROP TABLE IF EXISTS `v_diff`;

CREATE ALGORITHM=UNDEFINED DEFINER=`nlpweb`@`localhost` SQL SECURITY DEFINER VIEW `v_diff` AS select `v_es`.`session_id` AS `session_id`,`v_es`.`questionset_id` AS `questionset_id`,`v_ib`.`correct` AS `correct_ib`,`v_db`.`correct` AS `correct_db`,`v_es`.`correct` AS `correct_es`,((`v_ib`.`correct` + `v_db`.`correct`) / 2) AS `exp`,(`v_es`.`correct` - ((`v_ib`.`correct` + `v_db`.`correct`) / 2)) AS `diff` from ((`v_es` join `v_ib` on((`v_ib`.`session_id` = `v_es`.`session_id`))) join `v_db` on((`v_db`.`questionset_id` = `v_es`.`questionset_id`))) order by (`v_es`.`correct` - ((`v_ib`.`correct` + `v_db`.`correct`) / 2));

-- --------------------------------------------------------

--
-- Struktur des Views `v_es`
--
DROP TABLE IF EXISTS `v_es`;

CREATE ALGORITHM=UNDEFINED DEFINER=`nlpweb`@`localhost` SQL SECURITY DEFINER VIEW `v_es` AS select `v_all_evals`.`session_id` AS `session_id`,`v_all_evals`.`questionset_id` AS `questionset_id`,avg(`v_all_evals`.`correct`) AS `correct` from `v_all_evals` where (`v_all_evals`.`evaluate_as` = 'ES') group by `v_all_evals`.`session_id`,`v_all_evals`.`questionset_id`;

-- --------------------------------------------------------

--
-- Struktur des Views `v_ib`
--
DROP TABLE IF EXISTS `v_ib`;

CREATE ALGORITHM=UNDEFINED DEFINER=`nlpweb`@`localhost` SQL SECURITY DEFINER VIEW `v_ib` AS select `v_all_evals`.`session_id` AS `session_id`,avg(`v_all_evals`.`correct`) AS `correct` from `v_all_evals` where (`v_all_evals`.`evaluate_as` = 'IB') group by `v_all_evals`.`session_id`;

-- --------------------------------------------------------

--
-- Struktur des Views `v_quest_corr`
--
DROP TABLE IF EXISTS `v_quest_corr`;

CREATE ALGORITHM=UNDEFINED DEFINER=`nlpweb`@`localhost` SQL SECURITY DEFINER VIEW `v_quest_corr` AS select `v_all_evals`.`question_id` AS `question_id`,`v_all_evals`.`questionset_id` AS `questionset_id`,`v_all_evals`.`evaluate_as` AS `evaluate_as`,avg(`v_all_evals`.`correct`) AS `correct` from `v_all_evals` group by `v_all_evals`.`question_id`,`v_all_evals`.`questionset_id`,`v_all_evals`.`evaluate_as`;

-- --------------------------------------------------------

--
-- Struktur des Views `v_quest_exp`
--
DROP TABLE IF EXISTS `v_quest_exp`;

CREATE ALGORITHM=UNDEFINED DEFINER=`nlpweb`@`localhost` SQL SECURITY DEFINER VIEW `v_quest_exp` AS select `eval_session_sets`.`eval_session_id` AS `session_id`,`v_quest_corr`.`question_id` AS `question_id`,`v_quest_corr`.`questionset_id` AS `questionset_id`,`v_ib`.`correct` AS `correct_ib`,avg(`v_quest_corr`.`correct`) AS `correct_db`,`v_all_evals`.`evaluate_as` AS `evaluated_as`,((`v_ib`.`correct` + avg(`v_quest_corr`.`correct`)) / 2) AS `correct_exp`,`v_all_evals`.`correct` AS `correct` from (((`v_quest_corr` join `eval_session_sets` on(((`eval_session_sets`.`evaluate_as` = `v_quest_corr`.`evaluate_as`) and (`eval_session_sets`.`questionset_id` = `v_quest_corr`.`questionset_id`)))) join `v_ib` on((`v_ib`.`session_id` = `eval_session_sets`.`eval_session_id`))) join `v_all_evals` on(((`v_all_evals`.`session_id` = `eval_session_sets`.`eval_session_id`) and (`v_all_evals`.`question_id` = `v_quest_corr`.`question_id`)))) where (`v_quest_corr`.`evaluate_as` <> 'IB') group by `eval_session_sets`.`eval_session_id`,`v_quest_corr`.`question_id`,`v_quest_corr`.`questionset_id`,`v_ib`.`correct`,`v_all_evals`.`evaluate_as`,`v_all_evals`.`correct`;

-- --------------------------------------------------------

--
-- Struktur des Views `v_quest_perf`
--
DROP TABLE IF EXISTS `v_quest_perf`;

CREATE ALGORITHM=UNDEFINED DEFINER=`nlpweb`@`localhost` SQL SECURITY DEFINER VIEW `v_quest_perf` AS select `v_quest_perf_helper`.`question_id` AS `question_id`,`v_quest_perf_helper`.`questionset_id` AS `questionset_id`,`v_quest_perf_helper`.`correct_db` AS `correct_db`,avg(`v_quest_exp`.`correct`) AS `correct_es`,(`v_quest_perf_helper`.`correct_db` - avg(`v_quest_exp`.`correct`)) AS `correct_diff`,`v_quest_perf_helper`.`correct_es_exp` AS `correct_es_exp`,(avg(`v_quest_exp`.`correct`) / `v_quest_perf_helper`.`correct_es_exp`) AS `es_perf` from (`v_quest_perf_helper` join `v_quest_exp` on(((`v_quest_exp`.`evaluated_as` = 'ES') and (`v_quest_exp`.`question_id` = `v_quest_perf_helper`.`question_id`)))) group by `v_quest_perf_helper`.`question_id`,`v_quest_perf_helper`.`questionset_id`,`v_quest_perf_helper`.`correct_db`,`v_quest_perf_helper`.`correct_es_exp`;

-- --------------------------------------------------------

--
-- Struktur des Views `v_quest_perf_helper`
--
DROP TABLE IF EXISTS `v_quest_perf_helper`;

CREATE ALGORITHM=UNDEFINED DEFINER=`nlpweb`@`localhost` SQL SECURITY DEFINER VIEW `v_quest_perf_helper` AS select `a`.`question_id` AS `question_id`,`a`.`questionset_id` AS `questionset_id`,avg(`a`.`correct_db`) AS `correct_db`,avg(`a`.`correct_exp`) AS `correct_es_exp` from `v_quest_exp` `a` where (`a`.`evaluated_as` = 'DB') group by `a`.`question_id`,`a`.`questionset_id`;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
