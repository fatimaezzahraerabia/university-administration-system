// AffectationSalleCours.java
package service;

import model.*;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class AffectationSalleCours {
    private List<AffectationSalle> affectations;
    private SalleService salleService;
    private Connection connection;

    public AffectationSalleCours() {
        this.affectations = new ArrayList<>();
        this.salleService = new SalleService();
        try {
            this.connection = DatabaseConnection.getConnection();
            loadAffectationsFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // AffectationSalleCours.java

public void loadAffectationsFromDatabase() {
    affectations.clear(); // Clear the existing list to avoid duplicates
    String query = "SELECT * FROM affectationsalles";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    try (PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            int id = rs.getInt("affectationID");
            int idSalle = rs.getInt("salle_id");
            int idClasse = rs.getInt("id_classe");
            int idCours = rs.getInt("cours_id");
            String jour = rs.getString("Jour");
            String heureDebut = LocalTime.parse(rs.getString("heureDebut"), formatter).format(formatter);
            String heureFin = LocalTime.parse(rs.getString("heureFin"), formatter).format(formatter);

            Salle salle = salleService.getSalleById(idSalle);
            Classe classe = new ClasseService().getClasseById(idClasse);
            Cours cours = new CoursService().getCoursById(idCours);

            AffectationSalle affectation = new AffectationSalle(id, cours, salle, classe, jour, heureDebut, heureFin);
            affectations.add(affectation);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

  public boolean affecterCours(Cours cours, Classe classe, String jour, String heureDebut, String heureFin) {
    // Vérifier les équipements de la salle avant d'affecter le cours
    List<Salle> sallesDisponibles = obtenirSallesDisponibles(cours, jour, heureDebut, heureFin);
    if (sallesDisponibles.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Aucune salle disponible ne dispose des équipements nécessaires pour ce cours.");
        return false; // Aucune salle disponible
    }

    // Sélectionner automatiquement la première salle disponible
    Salle selectedSalle = sallesDisponibles.get(0);

    // Vérifier que l'enseignant n'a pas de cours simultanément pour une autre classe
    for (AffectationSalle affectation : affectations) {
        if (heureDebut.isEmpty() || heureFin.isEmpty()) {
            JOptionPane.showMessageDialog(null, "L'heure de début ou de fin ne peut pas être vide.");
            return false; // Invalid input
        }
        LocalTime affectationHeureDebut = LocalTime.parse(affectation.getHeureDebut(), DateTimeFormatter.ofPattern("H:mm"));
        LocalTime affectationHeureFin = LocalTime.parse(affectation.getHeureFin(), DateTimeFormatter.ofPattern("H:mm"));
        LocalTime inputHeureDebut = LocalTime.parse(heureDebut, DateTimeFormatter.ofPattern("H:mm"));
        LocalTime inputHeureFin = LocalTime.parse(heureFin, DateTimeFormatter.ofPattern("H:mm"));

        if (affectation.getCours().getCoursID() == cours.getCoursID() && affectation.getJour().equals(jour) &&
            affectationHeureDebut.equals(inputHeureDebut) && affectationHeureFin.equals(inputHeureFin)) {
            JOptionPane.showMessageDialog(null, "L'enseignant a déjà un cours à cet horaire.");
            return false; // L'enseignant a déjà un cours à cet horaire
        }
    }

    // Vérifier que la classe n'a pas déjà un cours dans une salle à cet horaire
    for (AffectationSalle affectation : affectations) {
        if (affectation.getClasse().getId() == classe.getId() && affectation.getJour().equals(jour) &&
            affectation.getHeureDebut().equals(heureDebut) && affectation.getHeureFin().equals(heureFin)) {
            JOptionPane.showMessageDialog(null, "La classe a déjà un cours dans une salle à cet horaire.");
            return false; // La classe a déjà un cours dans une salle à cet horaire
        }
    }

    // Vérifier que la salle sélectionnée n'est pas déjà affectée à une autre classe à cet horaire
    for (AffectationSalle affectation : affectations) {
        if (affectation.getSalle().getId() == selectedSalle.getId() && affectation.getJour().equals(jour) &&
            affectation.getHeureDebut().equals(heureDebut) && affectation.getHeureFin().equals(heureFin)) {
            JOptionPane.showMessageDialog(null, "La salle est déjà affectée à une autre classe à cet horaire.");
            return false; // La salle est déjà affectée à une autre classe à cet horaire
        }
    }

    // Affecter le cours à la salle sélectionnée
    AffectationSalle nouvelleAffectation = new AffectationSalle(affectations.size() + 1, cours, selectedSalle, classe, jour, heureDebut, heureFin);
    affectations.add(nouvelleAffectation);

    // Enregistrer l'affectation dans la base de données
    return saveAffectationToDatabase(nouvelleAffectation);
}
    public boolean saveAffectationToDatabase(AffectationSalle affectation) {
        String query = "INSERT INTO affectationsalles ( salle_id,cours_id,  heureDebut, heureFin,jour, id_classe) VALUES (?, ?, ?, ?, ?, ?)";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        try (PreparedStatement statement = connection.prepareStatement(query)) {
System.out.println(affectation.getSalle().getId());
            statement.setInt(1, affectation.getSalle().getId());
            statement.setInt(2, affectation.getCours().getCoursID());
            statement.setString(3, LocalTime.parse(affectation.getHeureDebut(), formatter).format(formatter));
            statement.setString(4, LocalTime.parse(affectation.getHeureFin(), formatter).format(formatter));
            statement.setString(5, affectation.getJour());
            statement.setInt(6, affectation.getClasse().getId());
            statement.executeUpdate();
            return true;
        }  catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   public List<Salle> obtenirSallesDisponibles(Cours cours, String jour, String heureDebut, String heureFin) {
    List<Salle> sallesDisponibles = salleService.getAllSalles();
    List<Salle> sallesNonDisponibles = new ArrayList<>();

    // Vérifier les salles déjà affectées à cet horaire
    for (AffectationSalle affectation : affectations) {
        if (affectation.getJour().equals(jour)) {
            LocalTime affectationHeureDebut = LocalTime.parse(affectation.getHeureDebut(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime affectationHeureFin = LocalTime.parse(affectation.getHeureFin(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime inputHeureDebut = LocalTime.parse(heureDebut, DateTimeFormatter.ofPattern("H:mm"));
            LocalTime inputHeureFin = LocalTime.parse(heureFin, DateTimeFormatter.ofPattern("H:mm"));

            // Check if the times overlap
            boolean overlap = (inputHeureDebut.isBefore(affectationHeureFin) && inputHeureFin.isAfter(affectationHeureDebut));
            if (overlap) {
                sallesNonDisponibles.add(affectation.getSalle());
            }
        }
    }

    // Retirer les salles non disponibles de la liste des salles disponibles
    sallesDisponibles.removeAll(sallesNonDisponibles);

    // Filtrer les salles en fonction des équipements nécessaires pour le cours
    List<Salle> sallesFiltrees = new ArrayList<>();
    for (Salle salle : sallesDisponibles) {
        if (cours.getTypeCours().equals("Chimie") && salle.getType().equals("Laboratoire")) {
            sallesFiltrees.add(salle);
        } else if (cours.getTypeCours().equals("Sport") && salle.getType().equals("Salle de sports")) {
            sallesFiltrees.add(salle);
        } else if (cours.getTypeCours().equals("Vidéo") && salle.getEquipements().contains(Equipements.TRIPLE_TABLEAU)) {
            sallesFiltrees.add(salle);
        } else if (!cours.getTypeCours().equals("Chimie") && !cours.getTypeCours().equals("Sport") && !cours.getTypeCours().equals("Vidéo")) {
            sallesFiltrees.add(salle);
        }
    }

    return sallesFiltrees;
}
    public List<AffectationSalle> getAllAffectations() {
        return affectations;
    }
}