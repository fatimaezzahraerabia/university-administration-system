package service;

import model.Absence;
import model.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AbsenceService {

    public void saveAbsence(Absence absence) {
        String query = "INSERT INTO absence (dateAbsence, justification, id_cours, id_viescolaire, status, ideleve) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the current date
            statement.setDate(1, new java.sql.Date(System.currentTimeMillis()));
            statement.setString(2, absence.getJustification());
            statement.setInt(3, absence.getIdCours());
            statement.setInt(4, 1);
            statement.setString(5, absence.getStatus());
            statement.setInt(6, absence.getIdEleve());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Absence> getAbsencesByCourseId(int courseId) {
        List<Absence> absences = new ArrayList<>();
        String query = "SELECT * FROM absence WHERE id_cours = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Absence absence = new Absence();
                absence.setId(resultSet.getInt("id"));
                absence.setDateAbsence(resultSet.getDate("dateAbsence"));
                absence.setJustification(resultSet.getString("justification"));
                absence.setIdCours(resultSet.getInt("id_cours"));
                absence.setIdVieScolaire(resultSet.getInt("id_viescolaire"));
                absence.setStatus(resultSet.getString("status"));
                absence.setIdEleve(resultSet.getInt("ideleve"));
                absences.add(absence);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return absences;
    }

    public Absence getAbsenceByEleveAndCourse(int eleveId, int courseId) {
        String query = "SELECT * FROM absence WHERE ideleve = ? AND id_cours = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, eleveId);
            statement.setInt(2, courseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Absence absence = new Absence();
                absence.setId(resultSet.getInt("id"));
                absence.setDateAbsence(resultSet.getDate("dateAbsence"));
                absence.setJustification(resultSet.getString("justification"));
                absence.setIdCours(resultSet.getInt("id_cours"));
                absence.setIdVieScolaire(resultSet.getInt("id_viescolaire"));
                absence.setStatus(resultSet.getString("status"));
                absence.setIdEleve(resultSet.getInt("ideleve"));
                return absence;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAbsence(Absence absence) {
        String query = "UPDATE absence SET dateAbsence = ?, justification = ?, id_cours = ?, id_viescolaire = ?, status = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, absence.getDateAbsence());
            statement.setString(2, absence.getJustification());
            statement.setInt(3, absence.getIdCours());
            statement.setInt(4, absence.getIdVieScolaire());
            statement.setString(5, absence.getStatus());
            statement.setInt(6, absence.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Absence> getAbsencesForWeekWithoutJustification() {
        List<Absence> absences = new ArrayList<>();
        String query = "SELECT * FROM absence WHERE (justification IS NULL OR justification = '') AND status != ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "Présent");
            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("Query executed: " + query);

                while (resultSet.next()) {
                    Absence absence = new Absence();
                    absence.setIdEleve(resultSet.getInt("ideleve"));
                    absence.setIdCours(resultSet.getInt("id_cours"));
                    absence.setStatus(resultSet.getString("status"));
                    absence.setJustification(resultSet.getString("justification"));
                    absences.add(absence);

                    // Print the absence details to the console
                    System.out.println("Absence found: Eleve ID = " + absence.getIdEleve() +
                            ", Cours ID = " + absence.getIdCours() +
                            ", Status = " + absence.getStatus() +
                            ", Justification = " + absence.getJustification());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return absences;
    }
    public String getParentEmailByEleveId(int eleveId) {
        String parentEmail = null;
        String query = "SELECT email FROM parent WHERE lien_parente = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, eleveId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                parentEmail = resultSet.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parentEmail;
    }

    // New method to get absences by course ID and date
    public List<Absence> getAbsencesByCourseIdAndDate(int courseId, LocalDate date) {
        List<Absence> absences = new ArrayList<>();
        String query = "SELECT * FROM absence WHERE id_cours = ? AND dateAbsence = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            statement.setDate(2, Date.valueOf(date));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Absence absence = new Absence();
                absence.setId(resultSet.getInt("id"));
                absence.setDateAbsence(resultSet.getDate("dateAbsence"));
                absence.setJustification(resultSet.getString("justification"));
                absence.setIdCours(resultSet.getInt("id_cours"));
                absence.setIdVieScolaire(resultSet.getInt("id_viescolaire"));
                absence.setStatus(resultSet.getString("status"));
                absence.setIdEleve(resultSet.getInt("ideleve"));
                absences.add(absence);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return absences;
    }

    public List<Object[]> getAbsencesByTeacherId(int teacherId) {
        List<Object[]> absences = new ArrayList<>();
        String query = """
            SELECT c.nomCours, u.username, a.dateAbsence, a.justification
            FROM absence a
            JOIN cours c ON a.id_cours = c.coursID
            JOIN utilisateur u ON a.id_viescolaire = u.id
            WHERE c.id_utilisateur = ?
            ORDER BY c.nomCours, a.dateAbsence
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, teacherId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String courseName = resultSet.getString("nomCours");
                String studentName = resultSet.getString("username");
                String date = resultSet.getString("dateAbsence");
                String justification = resultSet.getString("justification");

                absences.add(new Object[]{courseName, studentName, date, justification});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return absences;
    }
    public Absence getAbsenceByEleveAndCourseAndDate(int eleveId, int courseId, Date date) {
        String query = "SELECT * FROM absence WHERE ideleve = ? AND id_cours = ? AND dateAbsence = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, eleveId);
            statement.setInt(2, courseId);
            statement.setDate(3, date);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Absence absence = new Absence();
                absence.setId(resultSet.getInt("id"));
                absence.setDateAbsence(resultSet.getDate("dateAbsence"));
                absence.setJustification(resultSet.getString("justification"));
                absence.setIdCours(resultSet.getInt("id_cours"));
                absence.setIdVieScolaire(resultSet.getInt("id_viescolaire"));
                absence.setStatus(resultSet.getString("status"));
                absence.setIdEleve(resultSet.getInt("ideleve"));
                return absence;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Absence> getAllAbsences() {
        List<Absence> absences = new ArrayList<>();
        String query = "SELECT * FROM absence";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Absence absence = new Absence();
                absence.setId(resultSet.getInt("id"));
                absence.setDateAbsence(resultSet.getDate("dateAbsence"));
                absence.setJustification(resultSet.getString("justification"));
                absence.setIdCours(resultSet.getInt("id_cours"));
                absence.setIdVieScolaire(resultSet.getInt("id_viescolaire"));
                absence.setStatus(resultSet.getString("status"));
                absence.setIdEleve(resultSet.getInt("ideleve"));
                absences.add(absence);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return absences;
    }
}