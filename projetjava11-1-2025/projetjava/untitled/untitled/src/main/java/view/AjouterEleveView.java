package view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AjouterEleveView extends JPanel {
    // Déclaration des variables d'instance
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField gmailField;
    private JTextField adresseField;
    private JComboBox<String> genreDropdown;
    private JTextField dateNaissanceField;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel messageLabel , nomClasseLabel;

     public AjouterEleveView(String nomClasse) {
        // Initialiser les composants et le layout
        nomClasseLabel = new JLabel("Classe : " + nomClasse);
        this.add(nomClasseLabel);

        // Configuration de l'apparence avec FlatLaf
        FlatLightLaf.setup();
        setLayout(new BorderLayout());
        initializeComponents();
        configureLayout();
    }

    // Méthode pour initialiser les composants
    private void initializeComponents() {
        nomField = new JTextField(20);
        prenomField = new JTextField(20);
        gmailField = new JTextField(20);
        adresseField = new JTextField(20);
        dateNaissanceField = new JTextField(20);

        genreDropdown = new JComboBox<>(new String[]{"Masculin", "Féminin"});

        saveButton = createStyledButton("Sauvegarder", new Color(76, 175, 80));
        cancelButton = createStyledButton("Annuler", new Color(231, 76, 60));

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
    }

    // Méthode pour organiser les composants
    private void configureLayout() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(createFieldPanel("Nom:", nomField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFieldPanel("Prénom:", prenomField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFieldPanel("Gmail:", gmailField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFieldPanel("Adresse:", adresseField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFieldPanel("Genre:", genreDropdown));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFieldPanel("Date de Naissance:", dateNaissanceField));
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

    // Méthode pour créer un champ avec un label
    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    // Méthode pour styliser les boutons
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

    // Méthodes pour ajouter des écouteurs
    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void addCancelListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

    // Méthodes pour obtenir les valeurs des champs
    public String getNom() {
        return nomField.getText();
    }

    public String getPrenom() {
        return prenomField.getText();
    }

    public String getGmail() {
        return gmailField.getText();
    }

    public String getAdresse() {
        return adresseField.getText();
    }

    public String getGenre() {
        return (String) genreDropdown.getSelectedItem();
    }

    public String getDateNaissance() {
        return dateNaissanceField.getText();
    }
    public String getNomClasse() {
        return nomClasseLabel.getText().replace("Classe : ", ""); // Extraire le nom de la classe à partir du JLabel
    }

    public void setMessage(String message, boolean isSuccess) {
        messageLabel.setText(message);
        if (isSuccess) {
            messageLabel.setForeground(new Color(39, 174, 96)); // Vert pour succès
        } else {
            messageLabel.setForeground(new Color(192, 57, 43)); // Rouge pour erreur
        }
    }


}
