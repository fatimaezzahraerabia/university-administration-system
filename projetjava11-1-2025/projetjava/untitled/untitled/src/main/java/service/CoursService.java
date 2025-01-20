// CoursService.java
package service;

import model.Classe;
import model.Cours;
import model.DatabaseConnection;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursService {

    public List<Cours> getAllCours() {
        List<Cours> coursList = new ArrayList<>();
        String query = "SELECT * FROM cours";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Cours cours = new Cours(
                        resultSet.getInt("coursID"),
                        resultSet.getString("typeCours"),
                        resultSet.getString("nomCours")
                );
                coursList.add(cours);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coursList;
    }
    public static List<String> getEnseignants() {
        List<String> enseignants = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT prenom FROM utilisateur WHERE role = 'enseignant'");
            while (resultSet.next()) {
                enseignants.add(resultSet.getString("prenom"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enseignants;
    }

public Cours getCoursById(int id) {
    String query = "SELECT * FROM cours WHERE coursID = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, id);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Cours(
                        rs.getInt("coursID"),
                        rs.getString("typeCours"),
                        rs.getString("nomCours")
                );
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    public List<Classe> getClassesByCourseId(int courseId) {
    List<Classe> classes = new ArrayList<>();
    String query = "SELECT c.id, c.nomScolaire, c.niveau_scolaire, c.anneeScolaire " +
                   "FROM classe c " +
                   "JOIN affectationsalles a ON c.id = a.id_classe " +
                   "JOIN cours co ON a.cours_id = co.coursID " +
                   "WHERE co.coursID = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, courseId);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Classe classe = new Classe(
                        resultSet.getInt("id"),
                        resultSet.getString("niveau_scolaire"),
                        resultSet.getString("anneeScolaire"),
                        resultSet.getString("nomScolaire")

                );
                classes.add(classe);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return classes;
}
    public void addCours(Cours cours) {
        Connection connection = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();

            // Check if a course with the same nomCours AND typeCours already exists
            String checkQuery = "SELECT COUNT(*) FROM cours WHERE nomCours = ? AND typecours = ?";
            checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, cours.getNomCours());
            checkStmt.setString(2, cours.getTypeCours());
            resultSet = checkStmt.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                // Course with the same name and type already exists, show a message
                JOptionPane.showMessageDialog(null, "A course with the same name and type already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Course with the same name and type does not exist, proceed with insertion
                String insertQuery = "INSERT INTO cours (typecours, nomCours, id_utilisateur) VALUES (?, ?, ?)";
                insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setString(1, cours.getTypeCours());
                insertStmt.setString(2, cours.getNomCours());
                insertStmt.setInt(3, cours.getIdUtilisateur());
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Course added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while adding the course.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (checkStmt != null) checkStmt.close();
                if (insertStmt != null) insertStmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void deleteCours(int index) {
        try {
            List<Cours> courses = getAllCours();
            if (index < 0 || index >= courses.size()) {
                throw new IndexOutOfBoundsException("Invalid course index.");
            }
            Connection connection = DatabaseConnection.getConnection();
            String query = "UPDATE cours SET is_deleted = 1 WHERE coursID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, courses.get(index).getCoursID());
            stmt.executeUpdate();
            System.out.println("Cours deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting cours: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void updateCours(int index, Cours cours) {
        try {Connection connection = DatabaseConnection.getConnection();
            String query = "UPDATE cours SET typecours = ?, nomCours = ?, id_utilisateur = ? WHERE coursID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, cours.getTypeCours());
            stmt.setString(2, cours.getNomCours());
            stmt.setInt(3, cours.getIdUtilisateur());
            stmt.setInt(4, cours.getCoursID());
            stmt.executeUpdate();
            System.out.println("Cours updated successfully.");
        } catch (Exception e) {
            System.err.println("Error updating cours: " + e.getMessage());
            e.printStackTrace();
        }
    }

}


