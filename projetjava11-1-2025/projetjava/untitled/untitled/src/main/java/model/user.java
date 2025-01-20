package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class user {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_ecole";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public String validate(String username, String password) {
        String query = "SELECT role FROM utilisateur WHERE username = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("role"); // Return the role
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if validation fails
    }

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

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, teacherId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Map<String, Object[]> dailySchedule = new LinkedHashMap<>();

                // Initialize rows for each day
                for (String day : new String[]{"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"}) {
                    dailySchedule.put(day, new Object[]{day, "", "", "", "", ""});
                }

                while (resultSet.next()) {
                    // Normalize the `jour` value
                    String jour = resultSet.getString("jour");
                    jour = jour.substring(0, 1).toUpperCase() + jour.substring(1).toLowerCase();

                    String heureDebut = resultSet.getString("heureDebut");
                    String nomCours = resultSet.getString("nomCours");
                    String nomScolaire = resultSet.getString("nomScolaire");
                    String typecours = resultSet.getString("typecours");
                    String nomSalle = resultSet.getString("nomSalle");

                    System.out.println("Retrieved: " + jour + ", " + heureDebut + ", " + nomCours + ", " + nomScolaire);

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

    public int getMappedTeacherId(int teacherId) {
        String query = "SELECT cours_id_utilisateur FROM user_mapping WHERE utilisateur_id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, teacherId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("cours_id_utilisateur"); // Return mapped ID
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no mapping is found
    }


    public int getTeacherId(String username) {
        String query = "SELECT id FROM  cours , utilisateur WHERE role = 'ENSEIGNANT' AND username = ?  ";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
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


