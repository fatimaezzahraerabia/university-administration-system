package view;

import com.formdev.flatlaf.FlatLightLaf;
import controller.AppController;

import javax.swing.*;
import java.awt.*;

public class VieScolaireView extends JPanel {

    // Labels pour les statistiques
    private JLabel studentsLabel;
    private JLabel teachersLabel;
    private JLabel classesLabel;
    private final AppController appController;

    // Constructeur avec AppController
    public VieScolaireView(AppController appController) {
        this.appController = appController;

        // Appliquer le thème FlatLaf
        FlatLightLaf.setup();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Sidebar (menu)
        JPanel sidebar = MenuPanel.createMenu(appController);

        // Main content
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(new Color(245, 245, 245));

        JPanel welcomePanel = createWelcomePanel(sidebar.getPreferredSize().width);
        JPanel statsPanel = createStatsPanel();

        mainContent.add(welcomePanel, BorderLayout.NORTH);
        mainContent.add(statsPanel, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createWelcomePanel(int width) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(41, 128, 185)); // Bleu moderne
        panel.setPreferredSize(new Dimension(width, 88));

        JLabel welcomeLabel = new JLabel("Bienvenue, Admin");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.setLayout(new BorderLayout());
        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245)); // Gris clair moderne
        panel.setLayout(new BorderLayout());

        JPanel statsInfoPanel = new JPanel();
        statsInfoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        statsInfoPanel.setBackground(new Color(245, 245, 245));

        // Initialisation des labels pour les statistiques
        studentsLabel = new JLabel("Total Étudiants: 0");
        teachersLabel = new JLabel("Total Enseignants: 0");
        classesLabel = new JLabel("Total Classes: 0");

        // Ajouter les panneaux avec les labels au panneau principal
        statsInfoPanel.add(createStatPanel(studentsLabel, new Color(52, 152, 219)));
        statsInfoPanel.add(createStatPanel(teachersLabel, new Color(39, 174, 96)));
        statsInfoPanel.add(createStatPanel(classesLabel, new Color(231, 76, 60)));

        panel.add(statsInfoPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatPanel(JLabel label, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 150));
        panel.setBorder(BorderFactory.createLineBorder(color.darker(), 2));

        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 28));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    // Méthode pour mettre à jour les valeurs des statistiques
    public void updateStatsPanel(int students, int teachers, int classes) {
        System.out.println("Mise à jour des statistiques :");
        System.out.println("Étudiants : " + students);
        System.out.println("Enseignants : " + teachers);
        System.out.println("Classes : " + classes);

        studentsLabel.setText("Total Étudiants: " + students);
        teachersLabel.setText("Total Enseignants: " + teachers);
        classesLabel.setText("Total Classes: " + classes);
    }


}
