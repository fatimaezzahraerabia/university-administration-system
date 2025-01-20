// AffecterSalleDialog.java
package view;

import com.formdev.flatlaf.FlatLightLaf;
import controller.AppController;
import controller.SalleController;
import model.Classe;
import model.Cours;
import service.ClasseService;
import service.CoursService;
import service.SalleService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class AffecterSalleDialog extends JDialog {
    private JComboBox<Classe> classeComboBox;
    private JComboBox<Cours> coursComboBox;
    private JTextField jourField;
    private JTextField heureDebutField;
    private JTextField heureFinField;
    private JButton affecterButton;
    private SalleService salleService;
    private ClasseService classeService;
    private CoursService coursService;
    private SalleController salleController;
    private GestionSalleView gestionSalleView;

    public AffecterSalleDialog(Frame parent, AppController appController, GestionSalleView gestionSalleView) {
        super(parent, "Affecter une nouvelle salle", true);

        FlatLightLaf.setup();
        this.salleService = new SalleService();
        this.classeService = new ClasseService();
        this.coursService = new CoursService();
        this.salleController = new SalleController();
        this.gestionSalleView = gestionSalleView;
        initializeUI();
        setResizable(true);
        setPreferredSize(new Dimension(600, 500));
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        classeComboBox = new JComboBox<>(classeService.getAllClasses().toArray(new Classe[0]));
        coursComboBox = new JComboBox<>(coursService.getAllCours().toArray(new Cours[0]));
        jourField = new JTextField();
        heureDebutField = new JTextField();
        heureFinField = new JTextField();
        affecterButton = new JButton("Affecter");

        classeComboBox.setToolTipText("Sélectionnez une classe");
        coursComboBox.setToolTipText("Sélectionnez un cours");

        // Add placeholders to text fields
        addPlaceholder(jourField, "Entrez le jour (ex: Lundi)");
        addPlaceholder(heureDebutField, "Entrez l'heure de début (ex: 08:00)");
        addPlaceholder(heureFinField, "Entrez l'heure de fin (ex: 10:00)");

        affecterButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        affecterButton.setBackground(new Color(52, 152, 219));
        affecterButton.setForeground(Color.WHITE);
        affecterButton.setFocusPainted(false);

        formPanel.add(new JLabel("Classe:"));
        formPanel.add(classeComboBox);
        formPanel.add(new JLabel("Cours:"));
        formPanel.add(coursComboBox);
        formPanel.add(new JLabel("Jour:"));
        formPanel.add(jourField);
        formPanel.add(new JLabel("Heure Début:"));
        formPanel.add(heureDebutField);
        formPanel.add(new JLabel("Heure Fin:"));
        formPanel.add(heureFinField);
        formPanel.add(new JLabel());
        formPanel.add(affecterButton);

        add(formPanel, BorderLayout.CENTER);

        affecterButton.addActionListener(e -> affecterSalle());

        setLocationRelativeTo(getParent());
    }

    private void addPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }

    private void affecterSalle() {
        Classe classe = (Classe) classeComboBox.getSelectedItem();
        Cours cours = (Cours) coursComboBox.getSelectedItem();
        String jour = jourField.getText();
        String heureDebut = heureDebutField.getText();
        String heureFin = heureFinField.getText();

        boolean success = salleController.affecterCours(cours, classe, jour, heureDebut, heureFin);

        if (success) {
            JOptionPane.showMessageDialog(this, "Salle affectée avec succès !");
            gestionSalleView.loadAffectations(); // Refresh the table
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Échec de l'affectation de la salle.");
        }
    }
}