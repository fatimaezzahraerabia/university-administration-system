package view;

import com.formdev.flatlaf.FlatLightLaf;
import controller.AppController;
import controller.VieScolaireController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AjouterClasseView extends JPanel {

    private JTextField classNameField;
    private JComboBox<String> levelDropdown;
    private JTextField schoolYearField;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel messageLabel;
    private VieScolaireController vieScolaireController;
    private AppController appController;

    public AjouterClasseView() {
        // Appel du constructeur par défaut pour initialiser les composants
        FlatLightLaf.setup();
        setLayout(new BorderLayout());
        initializeComponents();
        configureLayout();
    }

    private void initializeComponents() {
        classNameField = new JTextField(20);
        classNameField.setPreferredSize(new Dimension(300, 10));

        levelDropdown = new JComboBox<>(new String[]{"Primaire", "Collège", "Lycée"});
        levelDropdown.setPreferredSize(new Dimension(300, 10));

        schoolYearField = new JTextField(20);
        schoolYearField.setPreferredSize(new Dimension(300, 10));

        saveButton = createStyledButton("Sauvegarder", new Color(76, 175, 80)); // Vert pour sauvegarder
        saveButton.setPreferredSize(new Dimension(150, 40));

        cancelButton = createStyledButton("Annuler", new Color(231, 76, 60)); // Rouge pour annuler
        cancelButton.setPreferredSize(new Dimension(150, 40));

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
    }

    private void configureLayout() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(createFieldPanel("Nom de la Classe:", classNameField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFieldPanel("Niveau Scolaire:", levelDropdown));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFieldPanel("Année Scolaire:", schoolYearField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        formPanel.add(buttonPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(messageLabel);

        add(formPanel, BorderLayout.CENTER);
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Méthodes pour ajouter des écouteurs d'événements
    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void addCancelListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

    // Méthodes pour obtenir les valeurs des champs
    public String getClassName() {
        return classNameField.getText();
    }

    public String getSelectedLevel() {
        return (String) levelDropdown.getSelectedItem();
    }

    public String getSchoolYear() {
        return schoolYearField.getText();
    }

    // Méthode pour afficher des messages à l'utilisateur
    public void setMessage(String message, boolean isSuccess) {
        messageLabel.setText(message);
        if (isSuccess) {
            messageLabel.setForeground(new Color(39, 174, 96)); // Vert pour succès
        } else {
            messageLabel.setForeground(new Color(192, 57, 43)); // Rouge pour erreur
        }
    }
}