# Système de Gestion d'Administration Universitaire

Ce projet est une application de bureau (desktop) développée en Java Swing pour simplifier les tâches administratives au sein d'une université, notamment l'attribution des salles et le suivi des absences des étudiants.

## 🚀 À propos du projet

L'objectif de cette application est de fournir une interface graphique simple et centralisée pour les administrateurs universitaires. Elle permet de gérer efficacement des tâches quotidiennes qui sont souvent manuelles, réduisant ainsi les risques d'erreurs et faisant gagner du temps.


## 🛠️ Technologies utilisées

*   **Langage :** Java
*   **Framework d'Interface Graphique :** Java Swing
*   **IDE :** * IntelliJ IDEA*
*   **Base de données  :** *( MySQL, gestion via fichiers)*


---

## ⚙️ Comment Lancer l'Application

Pour exécuter ce projet sur votre machine, suivez ces étapes :

1.  **Clonez le dépôt :**
    ```bash
    git clone https://github.com/fatimaezzahraerabia/gestion-administrative-.git
    ```

2.  **Ouvrez le projet dans un IDE Java :**
    *   Ouvrez votre IDE préféré (Eclipse, IntelliJ IDEA, NetBeans).
    *   Importez le projet cloné en tant que projet Maven ou projet Java standard.

3.  **Configurez la base de données  :**
    *   Si le projet utilise une base de données, assurez-vous qu'elle est configurée et que le script de création des tables a été exécuté.
    *   Vérifiez les informations de connexion dans les fichiers de configuration.

4.  **Exécutez l'application :**
    *   Trouvez la classe principale (celle qui contient la méthode `public static void main(String[] args)`).
    *   Faites un clic droit sur ce fichier et choisissez "Run As" > "Java Application".

---

## ✨ Fonctionnalités

*   **Gestion de l'Attribution des Salles :**
    *   Interface pour visualiser l'emploi du temps des salles.
    *   Fonctionnalité pour réserver une salle pour un cours ou un événement.
    *   Détection des conflits pour éviter les doubles réservations.

*   **Suivi des Absences des Étudiants :**
    *   Module pour enregistrer les absences des étudiants pour chaque cours.
    *   Possibilité de consulter l'historique des absences d'un étudiant.
    *   Génération de rapports simples sur l'assiduité.

*   **Interface de Bureau Intuitive :**
    *   Une application 100% desktop construite avec le framework natif Java Swing.
    *   Navigation simple entre les différents modules de gestion.
