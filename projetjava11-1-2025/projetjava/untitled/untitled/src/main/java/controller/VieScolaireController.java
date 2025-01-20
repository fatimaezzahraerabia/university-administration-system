package controller;

import model.Classe;
import model.Cours;
import model.Eleve;
import model.Enseignant;
import service.ClasseService;
import service.CoursService;
import service.GestionEnseignantService;
import service.VieScolaireService;
import view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static model.Role.ELEVE;

public class VieScolaireController {

    private VieScolaireView view;
    private AjouterClasseView ajouterClasseView;
    private VieScolaireService service;
    private ClasseService classeService;
    private ModifierClasseView modifierClasseView;
    private AjouterEleveView ajouterEleveView;
    private GestionClasseView gestionClasseView;
    private GestionEleveView gestionEleveView;
    private ModifierEleveView modifierEleveView;
    private GestionCoursView gestionCoursView;
    private ClassCourView classCourView;
    private CoursService coursService;
    private AppController appController;
    int row;
    private GestionEnseignantService gestionEnseignantService;
    private GestionEnseignantView gestionEnseignantView;
    public VieScolaireController(VieScolaireView view, GestionClasseView gestionClasseView, GestionEnseignantView gestionEnseignantView, GestionCoursView gestionCoursView) {



        this.gestionEnseignantService = new GestionEnseignantService();
        this.gestionEnseignantView = gestionEnseignantView; // Assurez-vous que cette vue est bien initialisée avant utilisation.
        this.gestionCoursView = gestionCoursView;
        this.coursService = new CoursService();



        this.view = view;
        this.gestionClasseView = gestionClasseView;
        this.service = new VieScolaireService();
        this.classeService = new ClasseService();
        updateStats();
        updateClasses();  // Call to update classes on initialization
        this.ajouterClasseView = new AjouterClasseView(); // Si es necesario
        this.ajouterClasseView.addSaveListener(e -> openAddClassDialog());
        this.modifierClasseView = new ModifierClasseView(); // Si es necesario
        String nomclasse = gestionClasseView.getSelectedClasseName();
        this.ajouterEleveView = new AjouterEleveView(nomclasse); // Si es necesario

        this.ajouterEleveView.addSaveListener(e -> openAddEleveDialog(nomclasse));
        List<Eleve> updatedEleves = classeService.getElevesByClasseId(classeService.getClasseIdByName(nomclasse));


        this.gestionEleveView = new GestionEleveView(nomclasse ,updatedEleves);
        this.gestionCoursView = gestionCoursView;

        this.coursService = new CoursService();
    }

    public VieScolaireController(VieScolaireView view,GestionEnseignantView gestionEnseignantView) {



        this.gestionEnseignantView = gestionEnseignantView;
        this.gestionEnseignantService = new GestionEnseignantService();

    }
    // AppController.java
    public void showClassCourView(int courseId , AppController appController) {
        ClassCourView classCourView = new ClassCourView(appController, courseId);
        List<Classe> classes = coursService.getClassesByCourseId(courseId);
        classCourView.updateClasseTable(classes);


    }

    // Méthode pour ouvrir le formulaire d'ajout de classe
    // Méthode pour ouvrir le formulaire d'ajout de classe
    public void openAddClassDialog() {
        // Créer une nouvelle fenêtre modale pour l'ajout de classe
        JDialog dialog = new JDialog((Frame) null, "Ajouter une Classe", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(view); // Centrer par rapport à la vue principale

        // Utiliser l'instance déjà existante de AjouterClasseView
        dialog.setContentPane(ajouterClasseView);

        // Ajouter les écouteurs à l'instance existante de AjouterClasseView
        ajouterClasseView.addSaveListener(e -> {
            System.out.println("Save button clicked");
            handleSave(ajouterClasseView, dialog);
        });

        ajouterClasseView.addCancelListener(e -> dialog.dispose());

        dialog.setVisible(true); // Afficher la fenêtre
    }

    // Méthode pour gérer l'action de sauvegarde
    private void handleSave(AjouterClasseView ajouterClasseView, JDialog dialog) {
        // Récupérer les données depuis la vue
        String className = ajouterClasseView.getClassName();
        String level = ajouterClasseView.getSelectedLevel();
        String schoolYear = ajouterClasseView.getSchoolYear();
        System.out.println("Classe name: " + className);
        System.out.println("Level: " + level);
        System.out.println("School year: " + schoolYear);

        // Validation des champs
        if (className.isEmpty() || level.isEmpty() || schoolYear.isEmpty()) {
            ajouterClasseView.setMessage("Tous les champs doivent être remplis.", false);
            return;
        }

        // Créer un objet Classe
        Classe newClasse = new Classe(level, schoolYear, className);

        // Appeler le service pour ajouter la classe
        try {
            // Appel à la méthode de service pour ajouter la classe
            classeService.addClass(newClasse);
            Timer timer = new Timer(1500, ev -> dialog.dispose());
            timer.setRepeats(false);
            timer.start();
            // Afficher un message de succès dans la vue
            ajouterClasseView.setMessage("Classe ajoutée avec succès!", true);

            // Mettre à jour les classes dans la vue GestionClasseView
            updateClasses();

            // Mettre à jour les statistiques
            updateStats();

            // Fermer la fenêtre après un court délai pour permettre à l'utilisateur de voir le message

        } catch (Exception e) {
            // En cas d'erreur lors de l'ajout de la classe
            ajouterClasseView.setMessage("Erreur lors de l'ajout de la classe.", false);
            e.printStackTrace();
        }
    }
    // Met à jour les statistiques dans la vue
    public void updateStats() {
        try {
            // Récupérer les données du service
            int totalEnseignants = service.getTotalEnseignants();
            int totalEleves = service.getTotalEleves();
            int totalClasses = service.getTotalClasses();
            System.out.println("Total enseignants récupérés : " + totalEnseignants);
            System.out.println("Total élèves récupérés : " + totalEleves);
            System.out.println("Total classes récupérées : " + totalClasses);

            // Mettre à jour l'affichage
            view.updateStatsPanel(totalEleves, totalEnseignants, totalClasses);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur lors de la récupération des données.");
        }
    }

    // Met à jour les données des classes dans la vue
    public void updateClasses() {
        try {
            // Récupérer la liste des classes à partir du service
            List<Classe> classes = classeService.getAllClasses();

            // Mettre à jour le tableau dans GestionClasseView
            gestionClasseView.updateClasseTable(classes);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gestionClasseView, "Erreur lors de la récupération des classes.");
        }
    }

    // Ajouter une nouvelle classe
    public void openEditClassDialog(int row) {
        // Récupérer les données de la table
        DefaultTableModel model = (DefaultTableModel) gestionClasseView.getTable().getModel();
        String classId = model.getValueAt(row, 0).toString();
        String nomClasse = (String) model.getValueAt(row, 1);
        String niveauScolaire = (String) model.getValueAt(row, 2);
        String anneeScolaire = model.getValueAt(row, 3).toString();

        // Créer la vue pour modifier la classe
        ModifierClasseView modifierClasseView = new ModifierClasseView();
        modifierClasseView.setIdClasse(classId);
        modifierClasseView.setNomClasse(nomClasse);
        modifierClasseView.setNiveauScolaire(niveauScolaire);
        modifierClasseView.setAnneeScolaire(anneeScolaire);

        // Créer une boîte de dialogue modale pour la modification
        JDialog dialog = new JDialog((Frame) null, "Modifier la Classe", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(gestionClasseView); // Centrer par rapport à la vue principale
        dialog.setContentPane(modifierClasseView);

        // Ajouter des écouteurs pour les actions de sauvegarde et d'annulation
        modifierClasseView.addSaveListener(e -> {
            String updatedNomClasse = modifierClasseView.getNomClasse();
            String updatedNiveauScolaire = modifierClasseView.getNiveauScolaire();
            String updatedAnneeScolaire = modifierClasseView.getAnneeScolaire();

            // Appeler le service pour enregistrer les modifications
            boolean success = classeService.updateClasse(Integer.parseInt(classId), updatedNiveauScolaire, updatedAnneeScolaire, updatedNomClasse);

            if (success) {
                // Mise à jour du modèle si la sauvegarde est réussie
                model.setValueAt(updatedNomClasse, row, 1);
                model.setValueAt(updatedNiveauScolaire, row, 2);
                model.setValueAt(updatedAnneeScolaire, row, 3);

                dialog.dispose();
                JOptionPane.showMessageDialog(dialog, "Classe mise à jour avec succès !");

                // Rafraîchir la vue après la mise à jour
                gestionClasseView.refreshView();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erreur lors de la mise à jour de la classe.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        modifierClasseView.addCancelListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    // Supprimer une classe
    public void deleteClass(int id) {
        try {
            classeService.deleteClass(id);
            updateClasses();  // Recharger les données après la suppression
            JOptionPane.showMessageDialog(view, "Classe supprimée avec succès !");

            // Rafraîchir la vue après la suppression
            gestionClasseView.refreshView();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur lors de la suppression de la classe.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void openGestionEleveView(int idClasse, String nomClasse) {
        // Exemple de données pour les élèves d'une classe (vous pouvez récupérer ces données à partir d'une base de données, etc.)
        List<Eleve> eleves = classeService.getElevesByClasseId(idClasse);

        // Créer la vue des élèves avec les données pour cette classe
        GestionEleveView gestionEleveView = new GestionEleveView(nomClasse, eleves);

        // Cette méthode peut être utilisée pour récupérer les élèves via l'id de la classe.
        gestionEleveView.updateElevesList(eleves);

        // Afficher la fenêtre
        gestionEleveView.setVisible(true);
    }
    public void openAddEleveDialog(String nomClasse) {
        // Créer une nouvelle fenêtre modale pour l'ajout d'élève
        JDialog dialog = new JDialog((Frame) null, "Ajouter un élève", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(view); // Centrer par rapport à la vue principale

        // Vérifier si ajouterEleveView est bien initialisée
        if (ajouterEleveView == null) {
            ajouterEleveView = new AjouterEleveView(nomClasse); // Initialisation si null
        }

        // Utiliser l'instance déjà existante de AjouterEleveView
        dialog.setContentPane(ajouterEleveView);

        // Ajouter les écouteurs à l'instance existante de AjouterEleveView
        ajouterEleveView.addSaveListener(e -> {
            System.out.println("Save button clicked");
            System.out.println(nomClasse);
            int id_classe = classeService.getClasseIdByName(nomClasse);
            System.out.println(id_classe);
            handleSaveEleve(ajouterEleveView, dialog, id_classe);
            Timer timer = new Timer(5, ev -> {
                gestionEleveView.dispose();

                List<Eleve> updatedEleves = classeService.getElevesByClasseId(id_classe);
              gestionEleveView.updateElevesList(updatedEleves);

                gestionEleveView.setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();// Appel à la méthode de sauvegarde
        });

        ajouterEleveView.addCancelListener(e -> dialog.dispose()); // Fermeture du dialogue

        dialog.setVisible(true); // Afficher la fenêtre
    }
    public List<Cours> GetCourses() {
        try {
            // Fetching all courses

            return coursService.getAllCours();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gestionCoursView, "Erreur lors de la récupération des cours.");
            return new ArrayList<>(); // Return an empty list in case of error
        }
    }
    public static List<String> fetchEnseignants() {
        return CoursService.getEnseignants();
    }
    // Méthode pour gérer l'action de sauvegarde
    private void handleSaveEleve(AjouterEleveView ajouterEleveView, JDialog dialog, int id_classe) {
        // Récupérer les données depuis la vue
        String nom = ajouterEleveView.getNom();
        String prenom = ajouterEleveView.getPrenom();
        String gmail = ajouterEleveView.getGmail();
        String adresse = ajouterEleveView.getAdresse();
        String genre = ajouterEleveView.getGenre();
        String dateNaissance = ajouterEleveView.getDateNaissance();

        // Validation des champs
        if (nom.isEmpty() || prenom.isEmpty() || genre.isEmpty() || adresse.isEmpty() || dateNaissance.isEmpty()) {
            ajouterEleveView.setMessage("Tous les champs doivent être remplis.", false); // Afficher message dans le bon composant
            return;
        }

        Date dateNaissanceda = null;
        try {
            // Convertir la chaîne en objet Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format attendu : année-mois-jour
            dateNaissanceda = dateFormat.parse(dateNaissance);
        } catch (ParseException e) {
            ajouterEleveView.setMessage("Format de date invalide. Utilisez le format yyyy-MM-dd.", false); // Affichage du message d'erreur
            return;
        }

        // Créer un objet Eleve
        Eleve newEleve = new Eleve(prenom, nom, gmail, ELEVE, genre, adresse, dateNaissanceda);

        // Appeler le service pour ajouter l'élève
        try {

            // Appel à la méthode de service pour ajouter l'élève
            classeService.ajouterEleve(newEleve, id_classe);
            List<Eleve> updatedEleves = classeService.getElevesByClasseId(id_classe);

            this.gestionEleveView.updateElevesList(updatedEleves);


            // Afficher un message de succès dans la vue
            ajouterEleveView.setMessage("Élève ajouté avec succès!", true);


            // Fermer la fenêtre après un court délai pour permettre à l'utilisateur de voir le message

        } catch (Exception e) {
            // En cas d'erreur lors de l'ajout de l'élève
            ajouterEleveView.setMessage("Erreur lors de l'ajout de l'élève.", false);
            e.printStackTrace();
        }
    }
    public List<Enseignant> loadEnseignants() {
        if (gestionEnseignantView == null) {
            JOptionPane.showMessageDialog(null, "La vue GestionEnseignantView n'a pas été initialisée.");
            return null;
        }

        try {
            List<Enseignant> enseignants = gestionEnseignantService.getAllEnseignants(); // Récupérer les enseignants via le service
            gestionEnseignantView.updateEnseignantTable(enseignants); // Mettre à jour la vue avec ces données
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des données des enseignants.");
        }
        return null;
    }
    public void loadCourses() {
        try {
            List<Cours> coursList = coursService.getAllCours();
            updateCourseTable(coursList);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gestionCoursView, "Erreur lors de la récupération des cours.");
        }
    }
    private void updateCourseTable(List<Cours> coursList) {
        DefaultTableModel model = (DefaultTableModel) gestionCoursView.getTable().getModel();
        model.setRowCount(0); // Réinitialise les données existantes
        for (Cours cours : coursList) {
            model.addRow(new Object[]{
                    cours.getCoursID(), cours.getNomCours(), cours.getTypeCours()
            });
        }
    }
    public void createCours(Cours cours) {
        coursService.addCours(cours);
    }
    public void deleteCours(int index) {
        if (index >= 0) {
            coursService.deleteCours(index);
        } else {
            System.err.println("Invalid course index.");
        }
    }
    public void updateCours(int index, Cours cours) {
        if (cours != null && cours.getCoursID() > 0) {
            coursService.updateCours(index, cours);
        } else {
            System.err.println("Invalid Cours data.");
        }
    }


    // gestion des enseignants

    public List<Enseignant> GetEnseignant() {
        try {
            // Fetching all courses

            return gestionEnseignantService.getAllEnseignants();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gestionEnseignantView, "Erreur lors de la récupération des Enseignants.");
            return new ArrayList<>(); // Return an empty list in case of error
        }
    }
    public void createEnseignant(Enseignant newEnseignant) {
        gestionEnseignantService.addEnseignant(newEnseignant);
    }

    public void updateEnseignant(int row, Enseignant updatedEnseignant) {
        // Fetch the enseignant ID from the selected row or directly from the updatedEnseignant object
        int enseignantId = updatedEnseignant.getId();

        // Update the enseignant details in the database
        boolean isUpdated = gestionEnseignantService.updateEnseignant(updatedEnseignant);

        if (isUpdated) {
            // Log the success and refresh the enseignant list
            System.out.println("Enseignant with ID " + enseignantId + " updated successfully.");
        } else {
            System.out.println("Failed to update enseignant with ID " + enseignantId + ".");
        }
    }

    public void deleteEnseignant(int index) {
        if (index >= 0) {
            gestionEnseignantService.deleteEnseignant(index);
        } else {
            System.err.println("Invalid course index.");
        }
    }
}
