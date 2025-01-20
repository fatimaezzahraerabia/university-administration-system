package service;
import model.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VieScolaireService {

    // Méthode pour compter le nombre d'enseignants
    public int getTotalEnseignants() throws SQLException {
        String query = "SELECT COUNT(*) FROM utilisateur where role = 'ENSEIGNANT'"; // Remplacez par votre table d'enseignants
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1); // Récupérer le nombre d'enseignants
            }
        }
        return 0;
    }

    // Méthode pour compter le nombre d'élèves
    public int getTotalEleves() throws SQLException {
        String query = "SELECT COUNT(*) FROM utilisateur where role = 'ELEVE'"; // Remplacez par votre table d'élèves
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1); // Récupérer le nombre d'élèves
            }
        }
        return 0;
    }

    // Méthode pour compter le nombre de classes
    public int getTotalClasses() throws SQLException {
        String query = "SELECT COUNT(*) FROM classe"; // Remplacez par votre table de classes
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1); // Récupérer le nombre de classes
            }
        }
        return 0;
    }
}
