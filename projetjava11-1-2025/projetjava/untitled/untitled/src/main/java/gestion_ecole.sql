-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mar. 14 jan. 2025 à 10:24
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestion_ecole`
--

-- --------------------------------------------------------

--
-- Structure de la table `absence`
--

CREATE TABLE `absence` (
  `id` int(11) NOT NULL,
  `dateAbsence` date DEFAULT NULL,
  `justification` text DEFAULT NULL,
  `id_cours` int(11) DEFAULT NULL,
  `id_viescolaire` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `ideleve` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `absence`
--

INSERT INTO `absence` (`id`, `dateAbsence`, `justification`, `id_cours`, `id_viescolaire`, `status`, `ideleve`) VALUES
(9403, '2024-12-31', '', 2, 1, 'Présent', 25),
(9404, '2024-12-31', '', 2, 1, 'Présent', 27),
(9405, '2024-12-31', NULL, 2, 1, 'Absence', 28),
(9406, '2024-12-31', '', 2, 1, 'Absence', 30);

-- --------------------------------------------------------

--
-- Structure de la table `affectationsalles`
--

CREATE TABLE `affectationsalles` (
  `affectationID` int(11) NOT NULL,
  `salle_id` int(11) DEFAULT NULL,
  `cours_id` int(11) DEFAULT NULL,
  `heureDebut` varchar(5) DEFAULT NULL,
  `heureFin` varchar(5) DEFAULT NULL,
  `Jour` varchar(10) DEFAULT NULL,
  `id_classe` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `affectationsalles`
--

INSERT INTO `affectationsalles` (`affectationID`, `salle_id`, `cours_id`, `heureDebut`, `heureFin`, `Jour`, `id_classe`) VALUES
(40, 1, 1, '08:00', '09:00', 'lundi', 1),
(41, 1, 1, '09:00', '10:00', 'lundi', 4),
(42, 1, 2, '11:00', '11:30', 'lundi', 1),
(43, 1, 2, '11:00', '12:00', 'lundi', 4),
(44, 1, 3, '08:00', '09:00', 'mardi', 13),
(45, 1, 2, '10:00', '13:00', 'samedi', 6),
(46, 1, 1, '20:00', '22:00', 'mercredi', 1),
(47, 1, 3, '20:00', '20:00', 'samedi', 6),
(48, 1, 1, '08:00', '11:00', 'samedi', 1);

-- --------------------------------------------------------

--
-- Structure de la table `classe`
--

CREATE TABLE `classe` (
  `id` int(11) NOT NULL,
  `niveau_scolaire` varchar(255) DEFAULT NULL,
  `anneeScolaire` varchar(255) DEFAULT NULL,
  `nomScolaire` varchar(255) DEFAULT NULL,
  `viescolaire_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `classe`
--

INSERT INTO `classe` (`id`, `niveau_scolaire`, `anneeScolaire`, `nomScolaire`, `viescolaire_id`) VALUES
(1, 'Primaire', '2023', 'Classe BB', NULL),
(2, 'Classe VB', 'Primaire', '2023', NULL),
(3, 'classeG', 'Primaire', '2019', NULL),
(4, 'Collège', '2008', 'classF', 1),
(5, 'Collège', '2035', 'calsen', 1),
(6, 'Primaire', '2019', 'lm', 1),
(8, 'Primaire', '2019', 'hj', 1),
(9, 'Primaire', '2019', 'lk', 1),
(10, 'Primaire', '2019', 'lk', 1),
(11, 'Primaire', '2012', 'ml', 1),
(12, 'Primaire', '2019', 'ml', 1),
(13, 'Primaire', '2019', 'nh', 1),
(14, 'ln', '2019', 'Primaire', 1),
(15, 'class', '2025', 'Collège', 1),
(16, 'Lycée', '2026', 'class2', 1);

-- --------------------------------------------------------

--
-- Structure de la table `cours`
--

CREATE TABLE `cours` (
  `coursID` int(11) NOT NULL,
  `typecours` varchar(255) DEFAULT NULL,
  `nomCours` varchar(255) DEFAULT NULL,
  `id_utilisateur` int(11) DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `cours`
--

INSERT INTO `cours` (`coursID`, `typecours`, `nomCours`, `id_utilisateur`, `is_deleted`) VALUES
(1, 'Mathematics', 'Algebra 101', NULL, 0),
(2, 'Science', 'Physics 101', NULL, 0),
(3, 'tp', 'Chimie', 1, 0),
(4, 'jfhz', 'bfuz', 1, 0);

-- --------------------------------------------------------

--
-- Structure de la table `parent`
--

CREATE TABLE `parent` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telephone` varchar(15) DEFAULT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `lien_parente` varchar(50) DEFAULT NULL,
  `id_viescolaire` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `parent`
--

INSERT INTO `parent` (`id`, `nom`, `prenom`, `email`, `telephone`, `adresse`, `lien_parente`, `id_viescolaire`) VALUES
(1, 'ikram', 'ghk', 'afakhariikram@gmail.com', NULL, NULL, '28', 1);

-- --------------------------------------------------------

--
-- Structure de la table `salle`
--

CREATE TABLE `salle` (
  `id` int(11) NOT NULL,
  `capacite` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `nomSalle` varchar(255) DEFAULT NULL,
  `equipements` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `salle`
--

INSERT INTO `salle` (`id`, `capacite`, `type`, `nomSalle`, `equipements`) VALUES
(1, 30, 'laboratoire ', 'Salle 101', NULL),
(17, 30, 'Classroom', 'Room 101', 'PROJECTEURS_VIDEO, TRIPLE_TABLEAU'),
(19, 30, 'laboratoire', 'salle4', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id_classe` int(11) DEFAULT NULL,
  `id` int(11) NOT NULL,
  `role` enum('VIESCOLAIRE','ELEVE','ENSEIGNANT') NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `genre` varchar(25) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `telephone` varchar(25) DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id_classe`, `id`, `role`, `password`, `username`, `prenom`, `email`, `adresse`, `genre`, `date_naissance`, `telephone`, `is_deleted`) VALUES
(NULL, 1, 'VIESCOLAIRE', 'motdepasse', 'motdepasse', NULL, '', NULL, NULL, NULL, NULL, 0),
(NULL, 2, 'ELEVE', 'bn', 'hh', 'kk', NULL, 'g', 'Masculin', '0030-05-13', NULL, 0),
(NULL, 3, 'ELEVE', 'VV', 'V', 'V', NULL, 'G', 'Masculin', '0027-02-10', NULL, 0),
(NULL, 4, 'ELEVE', 'c', 'fx', 'c', NULL, 'c', 'Masculin', '0027-02-10', NULL, 0),
(NULL, 5, 'ELEVE', 'j,', 'v', 'b', NULL, 'h', 'Masculin', '0027-02-10', NULL, 0),
(NULL, 6, 'ELEVE', 'r', 'hr', 'r', NULL, 'fff', 'Masculin', '0027-02-10', NULL, 0),
(NULL, 7, 'ELEVE', 'r', 'hr', 'r', NULL, 'fff', 'Masculin', '0027-09-10', NULL, 0),
(NULL, 8, 'ELEVE', 'motdepasse', 'rabia', 'fatima', NULL, 'rabia@gmail', 'Féminin', '0027-02-10', NULL, 0),
(NULL, 9, 'ELEVE', 'dz', 'ng', 'lk', NULL, 'a', 'Masculin', '0027-02-10', NULL, 0),
(NULL, 10, 'ELEVE', 'k', 'y', 'l', NULL, 'g', 'Masculin', '0027-02-09', NULL, 0),
(NULL, 11, 'ELEVE', 'j', 'b', 'j', NULL, 'j', 'Masculin', '0027-02-10', NULL, 0),
(NULL, 12, 'ELEVE', 'D', 'D', 'DD', NULL, 'HDD', 'Masculin', '0027-02-10', NULL, 0),
(NULL, 13, 'ELEVE', 'h', 'b', 'h', NULL, 'h', 'Masculin', '0027-02-10', NULL, 0),
(NULL, 14, 'ELEVE', 'b', 'b', 'b', NULL, 'b', 'Masculin', '2224-11-14', NULL, 0),
(NULL, 15, 'ELEVE', 'h', 'k', 'h', NULL, 'h', 'Masculin', '2348-10-26', NULL, 0),
(1, 24, 'ELEVE', 'password_example', 'nom_de_lutilisateur', 'prenom_de_lutilisateur', NULL, 'adresse_example', 'M', '2005-03-12', NULL, 0),
(6, 25, 'ELEVE', 'ga', 'fatima', 'fa', NULL, 'jh', 'Masculin', '2021-08-20', NULL, 0),
(1, 26, 'ELEVE', ' j', 'v hv', ' j', NULL, 'b', 'Masculin', '0027-02-10', NULL, 0),
(6, 27, 'ELEVE', 'h', 'h', 'h', NULL, 'h', 'Masculin', '0031-05-29', NULL, 0),
(6, 28, 'ELEVE', 'f', 'gf', 'ff', NULL, 'f', 'Masculin', '0027-02-10', NULL, 0),
(2, 29, 'ELEVE', 'k', 'kalimaH12347', 'r', NULL, 'j', 'Masculin', '0027-02-10', NULL, 0),
(6, 30, 'ELEVE', 'e', 'gf', 'r', NULL, 'a', 'Masculin', '0027-02-10', NULL, 0),
(NULL, 31, 'ENSEIGNANT', 'password456', 'Smith', 'Jane', 'jane.smith@example.com', 'gfhh', NULL, NULL, '0634567898', 0),
(5, 32, 'ELEVE', 'LK', '^p', 'KL', NULL, 'OP', 'Masculin', '0022-11-15', NULL, 0),
(11, 33, 'ELEVE', 'lks', 'kl', 'rabia', NULL, 'bnx', 'Masculin', '0027-02-10', NULL, 0),
(8, 34, 'ELEVE', 'c', 'hj', 'm', NULL, 'tt', 'Masculin', '0027-02-10', NULL, 0),
(11, 35, 'ELEVE', 'n', 'g', 'b', NULL, 'b', 'Masculin', '0027-02-10', NULL, 0),
(9, 36, 'ELEVE', ' hk', 'biug', 'jb', NULL, ' b', 'Masculin', '0027-02-10', NULL, 0),
(9, 37, 'ELEVE', 'b', 'hj', 'k,', NULL, 't', 'Masculin', '0027-02-14', NULL, 0),
(9, 38, 'ELEVE', 'N', 'H', 'H', NULL, 'N', 'Masculin', '0029-06-17', NULL, 0),
(5, 39, 'ELEVE', 'n', 'hfj', 'n', NULL, 'n', 'Masculin', '2021-06-30', NULL, 0),
(9, 40, 'ELEVE', 'c', 'egdf', 'bcv', NULL, 'c', 'Masculin', '0021-08-20', NULL, 0),
(14, 41, 'ELEVE', 'u', 'n', 'l', NULL, 'r', 'Masculin', '2020-03-09', NULL, 0),
(14, 42, 'ELEVE', 'y', 'mlk', 'g', NULL, 'bbbb ', 'Masculin', '0027-02-10', NULL, 0);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `absence`
--
ALTER TABLE `absence`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_cours` (`id_cours`),
  ADD KEY `fk_utilisateur_id` (`id_viescolaire`),
  ADD KEY `fk_ideleve` (`ideleve`);

--
-- Index pour la table `affectationsalles`
--
ALTER TABLE `affectationsalles`
  ADD PRIMARY KEY (`affectationID`),
  ADD KEY `salle_id` (`salle_id`),
  ADD KEY `cours_id` (`cours_id`),
  ADD KEY `fk_id_classe` (`id_classe`);

--
-- Index pour la table `classe`
--
ALTER TABLE `classe`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user` (`viescolaire_id`);

--
-- Index pour la table `cours`
--
ALTER TABLE `cours`
  ADD PRIMARY KEY (`coursID`),
  ADD KEY `fk_id_utilisateur` (`id_utilisateur`);

--
-- Index pour la table `parent`
--
ALTER TABLE `parent`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fkg_utilisateur_id` (`id_viescolaire`);

--
-- Index pour la table `salle`
--
ALTER TABLE `salle`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_utilisateur_classe` (`id_classe`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `absence`
--
ALTER TABLE `absence`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9407;

--
-- AUTO_INCREMENT pour la table `affectationsalles`
--
ALTER TABLE `affectationsalles`
  MODIFY `affectationID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT pour la table `classe`
--
ALTER TABLE `classe`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT pour la table `cours`
--
ALTER TABLE `cours`
  MODIFY `coursID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `parent`
--
ALTER TABLE `parent`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `salle`
--
ALTER TABLE `salle`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `absence`
--
ALTER TABLE `absence`
  ADD CONSTRAINT `absence_ibfk_1` FOREIGN KEY (`id_cours`) REFERENCES `cours` (`coursID`),
  ADD CONSTRAINT `absence_ibfk_2` FOREIGN KEY (`id_viescolaire`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `fk_ideleve` FOREIGN KEY (`ideleve`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `fk_utilisateur_id` FOREIGN KEY (`id_viescolaire`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `affectationsalles`
--
ALTER TABLE `affectationsalles`
  ADD CONSTRAINT `affectationsalles_ibfk_1` FOREIGN KEY (`salle_id`) REFERENCES `salle` (`id`),
  ADD CONSTRAINT `affectationsalles_ibfk_2` FOREIGN KEY (`cours_id`) REFERENCES `cours` (`coursID`),
  ADD CONSTRAINT `fk_id_classe` FOREIGN KEY (`id_classe`) REFERENCES `classe` (`id`);

--
-- Contraintes pour la table `classe`
--
ALTER TABLE `classe`
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`viescolaire_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `cours`
--
ALTER TABLE `cours`
  ADD CONSTRAINT `fk_id_utilisateur` FOREIGN KEY (`id_utilisateur`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `parent`
--
ALTER TABLE `parent`
  ADD CONSTRAINT `fkg_utilisateur_id` FOREIGN KEY (`id_viescolaire`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD CONSTRAINT `fk_utilisateur_classe` FOREIGN KEY (`id_classe`) REFERENCES `classe` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
