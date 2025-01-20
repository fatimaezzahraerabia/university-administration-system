// ModifierEleveView.java
package view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ModifierEleveView extends JPanel {

    private JTextField idField;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField gmailField;
    private JTextField adresseField;
    private JComboBox<String> genreDropdown;
    private JTextField dateNaissanceField;
    private JTextField nomClasseField; // Remplacer le JComboBox par JTextField pour le nom de la classe
    private JButton updateButton;
    private JLabel messageLabel;

    public ModifierEleveView() {
        // Appliquer le thème FlatLaf
        FlatLightLaf.setup();

        setLayout(new BorderLayout());
        initializeComponents();
        configureLayout();
    }

    private void initializeComponents() {
        idField = new JTextField(20);
        nomField = new JTextField(20);
        prenomField = new JTextField(20);
        gmailField = new JTextField(20);
        adresseField = new JTextField(20);

        genreDropdown = new JComboBox<>(new String[]{"Masculin", "Féminin"});
        dateNaissanceField = new JTextField(20);

        // Initialisation du JTextField pour le nom de la classe
        nomClasseField = new JTextField(20); // Utilisation d'un JTextField pour entrer le nom de la classe

        updateButton = createStyledButton("Mettre à jour", new Color(76, 175, 80)); // Vert pour mise à jour

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
    }

    private void configureLayout() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Ajout des champs de formulaire
        addField(formPanel, gbc, "Nom:", nomField, 0);
        addField(formPanel, gbc, "Prénom:", prenomField, 1);
        addField(formPanel, gbc, "Gmail:", gmailField, 2);
        addField(formPanel, gbc, "Adresse:", adresseField, 3);
        addField(formPanel, gbc, "Genre:", genreDropdown, 4);
        addField(formPanel, gbc, "Date de Naissance:", dateNaissanceField, 5);

        // Ajout du champ "Nom de classe"
        addField(formPanel, gbc, "Nom de classe:", nomClasseField, 6); // Champ pour entrer le nom de la classe

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(updateButton);
        formPanel.add(buttonPanel, gbc);

        gbc.gridy = 8;
        formPanel.add(messageLabel, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
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

    // Méthodes pour définir et obtenir le nom de la classe

    public String getNomClasse() {
        return nomClasseField.getText(); // Récupère le texte saisi dans le champ "Nom de classe"
    }

    public void addUpdateListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }

    public String getId() {
        return idField.getText();
    }

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

    public String getSelectedGenre() {
        return (String) genreDropdown.getSelectedItem();
    }

    public String getDateNaissance() {
        return dateNaissanceField.getText();
    }

    public void setId(String id) {
        idField.setText(id);
    }

    public void setNom(String nom) {
        nomField.setText(nom);
    }

    public void setPrenom(String prenom) {
        prenomField.setText(prenom);
    }

    public void setGmail(String gmail) {
        gmailField.setText(gmail);
    }

    public void setAdresse(String adresse) {
        adresseField.setText(adresse);
    }

    public void setSelectedGenre(String genre) {
        genreDropdown.setSelectedItem(genre);
    }

    public void setDateNaissance(String dateNaissance) {
        dateNaissanceField.setText(dateNaissance);
    }

    public void setNomClasse(String nomClasse) {
        // Si vous utilisez un JTextField pour afficher le nom de la classe
        nomClasseField.setText(nomClasse);
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