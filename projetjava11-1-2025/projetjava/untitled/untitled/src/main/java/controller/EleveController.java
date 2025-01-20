package controller;

import model.Eleve;
import service.ClasseService;
import service.EleveService;
import view.GestionEleveView;
import view.ModifierEleveView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;


public class EleveController {
    private ClasseService eleveService;
    private ModifierEleveView modifierEleveView;
    private Eleve eleve;
    private JFrame frame;
    private String nomClasse;
    private GestionEleveView gestionEleveView;
    private EleveService EleveService;

    public EleveController(ModifierEleveView modifierEleveView, GestionEleveView gestionEleveView, Eleve eleve, JFrame frame, String nomClasse) {
        this.eleveService = new ClasseService();
        this.modifierEleveView = modifierEleveView;
        this.gestionEleveView = gestionEleveView;
        this.eleve = eleve;
        this.frame = frame;
        this.nomClasse = nomClasse;
    }
    public EleveController() {
        this.EleveService = new EleveService();

    }
    public void openModifierEleveDialog(Eleve eleve, String nomClasse) {
        modifierEleveView.setNom(eleve.getNom());
        modifierEleveView.setPrenom(eleve.getPrenom());
        modifierEleveView.setGmail(eleve.getPassword());
        modifierEleveView.setAdresse(eleve.getAdresse());
        modifierEleveView.setSelectedGenre(eleve.getGenre());
        modifierEleveView.setDateNaissance(String.valueOf(eleve.getDatedenaissance()));
        modifierEleveView.setNomClasse(nomClasse);

        JFrame frame = new JFrame("Modifier Élève");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);
        frame.add(modifierEleveView);
        frame.setVisible(true);


        modifierEleveView.addUpdateListener(e -> {
            String updatedNomClasse = modifierEleveView.getNomClasse();
            handleUpdateEleve(eleve, updatedNomClasse);
        });
    }

    private void handleUpdateEleve(Eleve eleve, String nomClasse) {
        String updatedNom = modifierEleveView.getNom();
        String updatedPrenom = modifierEleveView.getPrenom();
        String updatedGmail = modifierEleveView.getGmail();
        String updatedAdresse = modifierEleveView.getAdresse();
        String updatedGenre = modifierEleveView.getSelectedGenre();
        String updatedDateNaissance = modifierEleveView.getDateNaissance();
        int updatedClasseId = eleveService.getClasseIdByName(nomClasse);

        if (updatedClasseId == -1) {
            JOptionPane.showMessageDialog(null, "Nom de classe invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("Updating student with ID: " + eleve.getEleveID());
        System.out.println("Updated details: ");
        System.out.println("Nom: " + updatedNom);
        System.out.println("Prenom: " + updatedPrenom);
        System.out.println("Gmail: " + updatedGmail);
        System.out.println("Adresse: " + updatedAdresse);
        System.out.println("Genre: " + updatedGenre);
        System.out.println("Date de Naissance: " + updatedDateNaissance);
        System.out.println("Classe ID: " + updatedClasseId);

        boolean success = eleveService.updateEleve(
                eleve.getEleveID(),
                updatedGmail,
                updatedNom,
                updatedPrenom,
                updatedAdresse,
                updatedGenre,
                java.sql.Date.valueOf(updatedDateNaissance),
                updatedClasseId
        );

        if (success) {

            JOptionPane.showMessageDialog(null, "Élève mis à jour avec succès !");
            List<Eleve> updatedEleves = eleveService.getElevesByClasseId(updatedClasseId);
            gestionEleveView.updateElevesList(updatedEleves);
        } else {

            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour de l'élève.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Object[]> getScheduleData(int userId) {
        return EleveService.getTeacherSchedule(userId);
    }
    public void populateScheduleTable(DefaultTableModel tableModel, int userId) {
        List<Object[]> scheduleData = EleveService.getTeacherSchedule(userId);

        // Clear existing data
        tableModel.setRowCount(0);

        // Add new data to the table model
        for (Object[] row : scheduleData) {
            tableModel.addRow(row);
        }

    }
}