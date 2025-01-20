package controller;

import model.Classe;
import model.Utilisateur;
import service.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Stack;

public class AppController {
    private VieScolaireController vieScolaireController;
    private AjouterClasseView ajouterclasse;
    private VieScolaireService vieScolaireService;
    private GestionMatiereView gestionMatiereView;
    private ClasseService classeService;
    private LoginView loginView;
    private GestionClasseView gestionClasseView;
    private GestionSalleView gestionSalleView;
    private VieScolaireView vieScolaireView;
    private AuthService authService;
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private AuthController authController;
    private GestionCoursView gestionCoursView;
    private GestionEnseignantView gestionEnseignantView;
    private ClassCourView classCourView;
    private Stack<JPanel> viewStack;
    private JPanel mainPanel;
    private EnseignantService enseignantService; // Add this line

    public AppController() {
        initialize();
        this.classeService = new ClasseService();
        this.authService = new AuthService();
        this.viewStack = new Stack<>(); // Initialize the viewStack
        this.mainPanel = new JPanel(new BorderLayout());
        this.enseignantService = new EnseignantService(); // Initialize the EnseignantService

        authController = new AuthController(loginView, authService, this);
        loginView.addLoginListener(e -> authController.handleLogin());
        vieScolaireController = new VieScolaireController(vieScolaireView, gestionClasseView, gestionEnseignantView, gestionCoursView);
    }

    private void initialize() {
        frame = new JFrame("Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginView = new LoginView(this);
        vieScolaireView = new VieScolaireView(this);
        gestionClasseView = new GestionClasseView(this);
        gestionSalleView = new GestionSalleView(this);
        gestionEnseignantView = new GestionEnseignantView(this);
        gestionCoursView = new GestionCoursView(this);
        gestionMatiereView = new GestionMatiereView(this);
        cardPanel.add(loginView, "Login");
        cardPanel.add(vieScolaireView, "VieScolaire");
        cardPanel.add(gestionClasseView, "GestionClasse");
        cardPanel.add(gestionSalleView, "GestionSalle");
        cardPanel.add(gestionEnseignantView, "GestionEnseignant");
        cardPanel.add(gestionCoursView, "GestionAbsences");
        cardPanel.add(gestionMatiereView, "GestionMatiere");
        frame.add(cardPanel);
        frame.setVisible(true);

        showLoginView();
    }

    public void showGestionMatiereView() {
        vieScolaireController.loadCourses();
        cardLayout.show(cardPanel, "GestionMatiere");
    }

    public void showClassCourView(int courseId) {
        vieScolaireController.showClassCourView(courseId, this);
    }

    public void showLoginView() {
        cardLayout.show(cardPanel, "Login");
    }

    public void showGestionClasseView() {
        List<Classe> classes = classeService.getAllClasses();
        gestionClasseView.updateClasseTable(classes);
        cardLayout.show(cardPanel, "GestionClasse");
    }

    public void showGestionSalleView() {
        cardLayout.show(cardPanel, "GestionSalle");
    }

    public void showGestionCoursView() {
        vieScolaireController.loadCourses();
        cardLayout.show(cardPanel, "GestionAbsences");
    }

    public void showGestionEnseignantView() {
        try {
            vieScolaireController.loadEnseignants();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la récupération des enseignants.");
        }
        cardLayout.show(cardPanel, "GestionEnseignant");
    }

    public void showVieScolaireView() {
        try {
            vieScolaireController.updateStats();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la mise à jour des statistiques.");
        }
        cardLayout.show(cardPanel, "VieScolaire");
    }

    public void showScheduleView(int teacherId) {
        String scheduleViewKey = "ScheduleView"; // Unique key for ScheduleView
        if (!isCardPresent(scheduleViewKey)) {
            ScheduleView scheduleView = new ScheduleView(teacherId, this); // Create the view
            cardPanel.add(scheduleView, scheduleViewKey); // Add to cardPanel
        }
        cardLayout.show(cardPanel, scheduleViewKey); // Switch to ScheduleView
        cardPanel.revalidate();
        cardPanel.repaint();

    }

    public void ShowEleveCompte(int teacherId) {
        String EleveViewKey = "cmptEleve"; // Unique key for ScheduleView
        if (!isCardPresent(EleveViewKey)) {
            cmptEleve cmptEleve = new cmptEleve(teacherId, this); // Create the view
            cardPanel.add(cmptEleve, EleveViewKey); // Add to cardPanel
        }
        cardLayout.show(cardPanel, EleveViewKey); // Switch to ScheduleView
        cardPanel.revalidate();
        cardPanel.repaint();

    }


    // Helper method to check if a card already exists in the CardLayout
    private boolean isCardPresent(String cardName) {
        for (Component comp : cardPanel.getComponents()) {
            if (cardName.equals(cardPanel.getLayout().toString())) {
                return true;
            }
        }
        return false;
    }

    public void showAbsenceView(int teacherId) {
        AbsenceView absenceView = new AbsenceView(teacherId, this); // Pass 'this' (AppController instance)
        AbsenceService absenceService = new AbsenceService(); // Create the absence service
        AbsenceController absenceController = new AbsenceController(absenceView, absenceService); // Create the absence controller

        absenceController.loadAbsencesForTeacher(teacherId); // Load absences for the teacher
        absenceView.setVisible(true);
        // Show the absence view
        cardPanel.revalidate();
        cardPanel.repaint();

    }

    public void redirectToRoleBasedView(Utilisateur utilisateur) {
        switch (utilisateur.getRole()) {
            case ENSEIGNANT:
                int teacherId = enseignantService.getTeacherId(utilisateur.getNom()); // Get teacher ID
                showScheduleView(teacherId); // Call the refactored method
                break;
            case VIESCOLAIRE:
                showDashboardView();
                vieScolaireController.updateStats();
                break;
            case ELEVE:
                int eleveId = EleveService.getEleveId(utilisateur.getNom()); // Get teacher ID
                ShowEleveCompte(eleveId);
                break;
            default:
                showLoginView();
                break;
        }
    }


    private void showDashboardView() {
        loginView.setVisible(false);
        vieScolaireView.setVisible(true);
    }

    public VieScolaireController getVieScolaireController() {
        return vieScolaireController;
    }

    private void switchView(JPanel newView) {
        if (!viewStack.isEmpty()) {
            mainPanel.remove(viewStack.peek());
        }
        viewStack.push(newView);
        mainPanel.add(newView, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void goBack() {
        if (viewStack.size() > 1) {
            mainPanel.remove(viewStack.pop());
            mainPanel.add(viewStack.peek(), BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }
}