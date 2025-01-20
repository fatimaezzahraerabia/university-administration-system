// CoursService.java
package service;

import model.DatabaseConnection;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnseignantService {

    public List<Object[]> getTeacherSchedule(int teacherId) {
        String query = """
        SELECT jour, heureDebut, heureFin, nomCours, nomScolaire, nomSalle, typecours
        FROM affectationsalles
        JOIN cours ON cours.coursID = affectationsalles.cours_id
        JOIN classe ON affectationsalles.id_classe = classe.id
        JOIN salle ON affectationsalles.salle_id = salle.id
        WHERE cours.id_utilisateur = ?
    """;
        List<Object[]> scheduleData = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, teacherId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Map<String, Object[]> dailySchedule = new LinkedHashMap<>();

                // Initialize rows for each day
                for (String day : new String[]{"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"}) {
                    dailySchedule.put(day, new Object[]{day, "", "", "", "", ""});
                }

                while (resultSet.next()) {
                    // Log all retrieved data
                    System.out.println("Row Data: " +
                            "Jour=" + resultSet.getString("jour") +
                            ", HeureDebut=" + resultSet.getString("heureDebut") +
                            ", NomCours=" + resultSet.getString("nomCours") +
                            ", NomScolaire=" + resultSet.getString("nomScolaire") +
                            ", TypeCours=" + resultSet.getString("typecours") +
                            ", NomSalle=" + resultSet.getString("nomSalle")
                    );

                    // Normalize and process data
                    String jour = resultSet.getString("jour");
                    jour = jour.substring(0, 1).toUpperCase() + jour.substring(1).toLowerCase();

                    String heureDebut = resultSet.getString("heureDebut");
                    String nomCours = resultSet.getString("nomCours");
                    String nomScolaire = resultSet.getString("nomScolaire");
                    String typecours = resultSet.getString("typecours");
                    String nomSalle = resultSet.getString("nomSalle");

                    int columnIndex = switch (heureDebut) {
                        case "08:00" -> 1;
                        case "10:00" -> 2;
                        case "12:00" -> 3;
                        case "14:00" -> 4;
                        case "16:00" -> 5;
                        default -> -1;
                    };

                    if (columnIndex != -1 && dailySchedule.containsKey(jour)) {
                        Object[] row = dailySchedule.get(jour);
                        String existingEntry = (String) row[columnIndex];
                        String newEntry = nomCours + " " + typecours + "\n" + nomScolaire + "\n" + nomSalle;
                        row[columnIndex] = existingEntry.isEmpty() ? newEntry : existingEntry + "\n\n" + newEntry;
                    }
                }

                scheduleData.addAll(dailySchedule.values());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleData;
    }

    public int getTeacherId(String username) {
        String query = "SELECT id FROM  cours , utilisateur WHERE role = 'ENSEIGNANT' AND username = ?  ";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    System.out.println("Retrieved teacher ID: " + id);
                    return id;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("No teacher ID found for username: " + username);
        return -1;
    }

}


