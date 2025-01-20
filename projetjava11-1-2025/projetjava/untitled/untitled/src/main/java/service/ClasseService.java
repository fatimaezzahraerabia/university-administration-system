package service;

import model.Classe;
import model.DatabaseConnection;
import model.Eleve;
import model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClasseService {

    // Fetch classes from the database
    public List<Classe> getAllClasses() {
        List<Classe> classes = new ArrayList<>();
        String query = "SELECT id, nomScolaire, niveau_scolaire, anneeScolaire FROM classe";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (conn == null) {
                System.out.println("Erreur de connexion à la base de données.");
                return new ArrayList<>(); // Retourne une liste vide si la connexion échoue
            }
            while (rs.next()) {
                int id = rs.getInt("id");
                String nomClasse = rs.getString("nomScolaire");
                String niveauScolaire = rs.getString("niveau_scolaire");
                String anneeScolaire = rs.getString("anneeScolaire");

                System.out.println("Classe trouvée: " + id + " - " + nomClasse + " - " + niveauScolaire + " - " + anneeScolaire);

                Classe classe = new Classe( id,  niveauScolaire, anneeScolaire,  nomClasse) ;

                classes.add(classe);
            }


            // Vérification si des résultats sont trouvés
            if (classes.isEmpty()) {
                System.out.println("Aucune classe trouvée dans la base de données.");
            } else {
                System.out.println("Classes récupérées : " + classes.size());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }


    // Add a new class to the database
    public void addClass(Classe classe) {
        String selectQuery = "SELECT id FROM utilisateur WHERE role = 'viescolaire'";
        String insertQuery = "INSERT INTO classe (niveau_scolaire, anneeScolaire, nomScolaire, viescolaire_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            // Récupérer l'ID viescolaire
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int viescolaireId = rs.getInt("id");

                // Assurez-vous que anneeScolaire est un nombre valide
                String anneeScolaire = classe.getAnneeScolaire();
                int annee = 0; // Initialiser à 0 ou une valeur par défaut
                try {
                    annee = Integer.parseInt(anneeScolaire); // Convertir en entier
                } catch (NumberFormatException e) {
                    // Si la conversion échoue, vous pouvez traiter l'erreur (par exemple, définir une valeur par défaut)
                    System.out.println("Erreur de format pour l'année scolaire : " + anneeScolaire);
                    return; // Arrêter l'insertion si l'année est invalide
                }

                // Insérer les données dans la table classe
                insertStmt.setString(1,classe.getNiveauScolaire() );
                insertStmt.setInt(2, annee); // Utiliser l'entier pour l'année scolaire
                insertStmt.setString(3,classe.getNomClasse() );
                insertStmt.setInt(4, viescolaireId);
                System.out.println("Classe: " + classe.getNomClasse());
                System.out.println("Niveau Scolaire: " + classe.getNiveauScolaire());
                System.out.println("Année Scolaire: " + annee);
                System.out.println("Vie scolaire ID: " + viescolaireId);

                insertStmt.executeUpdate();
            } else {
                System.out.println("Aucun utilisateur avec le rôle 'viescolaire' trouvé.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



        // Méthode pour mettre à jour une classe dans la base de données


            public boolean updateClasse(int classId,String niveauScolaire,String anneeScolaire, String nomClasse) {
            String query = "UPDATE classe SET niveau_scolaire = ?, anneeScolaire = ?, nomScolaire = ? WHERE id = ?";

            try ( Connection connection = DatabaseConnection.getConnection();
                   PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Remplir les paramètres de la requête
                int annee;
                try {
                    annee = Integer.parseInt(anneeScolaire);
                } catch (NumberFormatException e) {
                    System.err.println("Erreur : anneeScolaire doit être une année (par exemple 2024).");
                    return false; // Ou lever une exception personnalisée
                }
                preparedStatement.setString(1, niveauScolaire);
                preparedStatement.setInt(2,annee);
                preparedStatement.setString(3,nomClasse );
                preparedStatement.setInt(4, classId);

                // Exécuter la requête
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0; // Retourne true si au moins une ligne est affectée
            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Retourne false si une erreur se produit
            }
        }


    // Update an existing class
    public void updateClass(Classe classe) {
        String query = "UPDATE classe SET nom_classe = ?, niveau_scolaire = ?, annee_scolaire = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, classe.getNomClasse());
            pstmt.setString(2, classe.getNiveauScolaire());
            pstmt.setString(3, classe.getAnneeScolaire());
            pstmt.setInt(4, classe.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a class
    public void deleteClass(int id) {
        String query = "DELETE FROM classe WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getClasseIdByName(String nomClasse) {
    String query = "SELECT id FROM classe WHERE nomScolaire = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, nomClasse);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1; // Return -1 if class ID is not found
}


    public List<Eleve> getElevesByClasseId(int idClasse) {
        List<Eleve> eleves = new ArrayList<>();
        String query = "SELECT * FROM utilisateur WHERE id_classe = ? AND role = 'eleve'";


        try (Connection connection =DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idClasse);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Role role = Role.fromString(resultSet.getString("role"));
                    // Mappez les données de la base vers un objet Eleve
                    Eleve eleve = new Eleve(resultSet.getInt("id"),
                            resultSet.getString("prenom"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            role,
                            resultSet.getString("genre"),
                            resultSet.getString("adresse"),
                            resultSet.getDate("date_naissance")
                    );

                    // Ajout à la liste des élèves
                    eleves.add(eleve);

                    // Affichage des détails de l'élève dans la console
                    System.out.println("Élève ajouté : ");
                    System.out.println("Prénom : " + eleve.getPrenom());
                    System.out.println("Nom d'utilisateur : " + eleve.getNom());
                    System.out.println("Mot de passe : " + eleve.getPassword());
                    System.out.println("Rôle : " + eleve.getRole());
                    System.out.println("Genre : " + eleve.getGenre());
                    System.out.println("Adresse : " + eleve.getAdresse());
                    System.out.println("Date de naissance : " + eleve.getDatedenaissance());
                    System.out.println("----------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur (log ou throw une exception métier)
        }

        return eleves;
    }
    public boolean ajouterEleve(Eleve eleve, int id_classe) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Vérifier si l'ID de la classe existe dans la table classe
            String checkQuery = "SELECT COUNT(*) FROM classe WHERE id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, id_classe);
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    System.out.println("Erreur : La classe avec ID " + id_classe + " n'existe pas.");
                    return false; // Si la classe n'existe pas, retourner false
                }
            }

            // Requête pour insérer l'élève avec l'ID de la classe
            String query = "INSERT INTO utilisateur (role, password, username, prenom, adresse, genre, date_naissance, id_classe) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, eleve.getRole().toString());  // Rôle de l'élève
                preparedStatement.setString(2, eleve.getPassword());         // Mot de passe de l'élève
                preparedStatement.setString(3, eleve.getNom());              // Nom de l'élève
                preparedStatement.setString(4, eleve.getPrenom());           // Prénom de l'élève
                preparedStatement.setString(5, eleve.getAdresse());          // Adresse de l'élève
                preparedStatement.setString(6, eleve.getGenre());            // Genre de l'élève
                preparedStatement.setDate(7, new java.sql.Date(eleve.getDatedenaissance().getTime())); // Date de naissance de l'élève
                preparedStatement.setInt(8, id_classe);                      // ID de la classe (clé étrangère)

                // Exécuter la requête d'insertion
                int rowsInserted = preparedStatement.executeUpdate();

                // Retourner vrai si une ligne a été insérée
                return rowsInserted > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateEleve(int idUtilisateur, String password, String username, String prenom, String adresse, String genre, Date dateNaissance, int idClasse) {
        String queryUpdateEleve = "UPDATE utilisateur SET password = ?, username = ?, prenom = ?, adresse = ?, genre = ?, date_naissance = ?, id_classe = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmtUpdateEleve = connection.prepareStatement(queryUpdateEleve)) {

            stmtUpdateEleve.setString(1, password);
            stmtUpdateEleve.setString(2, username);
            stmtUpdateEleve.setString(3, prenom);
            stmtUpdateEleve.setString(4, adresse);
            stmtUpdateEleve.setString(5, genre);
            stmtUpdateEleve.setDate(6, new java.sql.Date(dateNaissance.getTime()));
            stmtUpdateEleve.setInt(7, idClasse);
            stmtUpdateEleve.setInt(8, idUtilisateur);

            int rowsUpdated = stmtUpdateEleve.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Classe getClasseById(int id) {
        String query = "SELECT * FROM classe WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Classe(
                            rs.getInt("id"),
                            rs.getString("niveau_scolaire"),
                            rs.getString("anneeScolaire"),
                            rs.getString("nomScolaire")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

