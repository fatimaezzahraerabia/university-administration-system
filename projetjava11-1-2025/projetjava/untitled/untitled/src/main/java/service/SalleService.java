package service;

import model.DatabaseConnection;
import model.Equipements;
import model.Salle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SalleService {

    public List<Salle> getAllSalles() {
        List<Salle> salles = new ArrayList<>();
        String query = "SELECT * FROM salle";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Salle salle = new Salle();
                salle.setId(resultSet.getInt("id"));
                salle.setNomSalle(resultSet.getString("nomSalle"));
                salle.setCapacite(resultSet.getInt("capacite"));
                salle.setType(resultSet.getString("type"));

                String equipementsStr = resultSet.getString("equipements");
                List<Equipements> equipements = convertToEquipementsList(equipementsStr);
                salle.setEquipements(equipements);
                salles.add(salle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return salles;
    }

    private List<Equipements> convertToEquipementsList(String equipementsStr) {
        if (equipementsStr == null || equipementsStr.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return List.of(equipementsStr.split(",")).stream()
                    .map(String::trim)
                    .map(Equipements::valueOf)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            // Log the error and handle it appropriately
            System.err.println("Invalid equipment value: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void addSalle(Salle salle) {
        String query = "INSERT INTO salle (nom, capacite, type, equipements, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, salle.getNomSalle());
            statement.setInt(2, salle.getCapacite());
            statement.setString(3, salle.getType());
            statement.setString(4, convertToString(salle.getEquipements()));

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSalle(Salle salle) {
        String query = "UPDATE salle SET nom = ?, capacite = ?, type = ?, equipements = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, salle.getNomSalle());
            statement.setInt(2, salle.getCapacite());
            statement.setString(3, salle.getType());
            statement.setString(4, convertToString(salle.getEquipements()));

            statement.setInt(5, salle.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSalle(int id) {
        String query = "DELETE FROM salle WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private String convertToString(List<Equipements> equipements) {
        return equipements.stream()
                .map(Equipements::name)
                .collect(Collectors.joining(","));
    }

    public Salle getSalleById(int id) {
        String query = "SELECT * FROM salle WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List<Equipements> equipements = convertToEquipementsList(rs.getString("equipements"));

                    return new Salle(
                            rs.getInt("id"),
                            rs.getInt("capacite"),
                            rs.getString("type"),
                            equipements,
                            rs.getString("nomSalle")

                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}