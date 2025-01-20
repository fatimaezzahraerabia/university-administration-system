package service;

import model.Enseignant;
import model.DatabaseConnection;
import model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionEnseignantService {

    // Récupérer tous les enseignants depuis la base de données
    public List<Enseignant> getAllEnseignants() {
        List<Enseignant> enseignants = new ArrayList<>();
        String query = "SELECT * FROM utilisateur WHERE role = ? AND is_deleted = 0";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, "ENSEIGNANT"); // Ensure this is correct

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Debugging: Log the ID and username
                System.out.println("Enseignant ID: " + resultSet.getInt("id"));

                Enseignant enseignant = new Enseignant(
                        resultSet.getInt("id"),  // Ensure you're fetching the ID correctly
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        Role.ENSEIGNANT,  // Assuming role is always 'ENSEIGNANT'
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("adresse"),
                        resultSet.getString("genre"),
                        resultSet.getString("telephone")
                );
                enseignants.add(enseignant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enseignants;
    }


    // Ajouter un nouvel enseignant
    public boolean addEnseignant(Enseignant enseignant) {
        String query = "INSERT INTO utilisateur (username, password, role, prenom, email, adresse, genre, telephone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, enseignant.getUsername());
            String password = enseignant.getUsername() + enseignant.getPrenom();
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, "ENSEIGNANT");
            preparedStatement.setString(4, enseignant.getPrenom());
            preparedStatement.setString(5, enseignant.getGmail());
            preparedStatement.setString(6, enseignant.getAdresse());
            preparedStatement.setString(7, enseignant.getGenre());
            preparedStatement.setString(8, enseignant.getTelephone());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mettre à jour les informations d'un enseignant
    public boolean updateEnseignant(Enseignant enseignant) {
        String query = "UPDATE utilisateur SET username = ?, password = ?, prenom = ?, email = ?, adresse = ?, genre = ?, telephone = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, enseignant.getUsername());
            preparedStatement.setString(2, enseignant.getPassword());
            preparedStatement.setString(3, enseignant.getPrenom());
            preparedStatement.setString(4, enseignant.getGmail());
            preparedStatement.setString(5, enseignant.getAdresse());
            preparedStatement.setString(6, enseignant.getGenre());
            preparedStatement.setString(7, enseignant.getTelephone());
            preparedStatement.setInt(8, enseignant.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Supprimer un enseignant
    public boolean deleteEnseignant(int enseignantId) {
        String query = "UPDATE utilisateur SET is_deleted = 1 WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, enseignantId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
