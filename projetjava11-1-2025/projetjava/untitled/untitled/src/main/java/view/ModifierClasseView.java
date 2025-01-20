package view;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ModifierClasseView extends JPanel {
    private JTextField idField;
    private JTextField nameField;
    private JComboBox<String> levelComboBox;
    private JTextField yearField;
    private JButton saveButton;
    private JButton cancelButton;

    public ModifierClasseView() {
        // Appliquer le thème FlatLaf
        FlatLightLaf.setup();

        // Contenu principal
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels et champs de saisie
        JLabel idLabel = new JLabel("ID Classe:");
        idField = new JTextField(20);
        idField.setPreferredSize(new Dimension(300, 40));
        idField.setEnabled(false); // Champ non modifiable pour l'ID

        JLabel nameLabel = new JLabel("Nom Classe:");
        nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(300, 40));

        JLabel levelLabel = new JLabel("Niveau Scolaire:");
        levelComboBox = new JComboBox<>(new String[]{"Primaire", "Collège", "Lycée"});
        levelComboBox.setPreferredSize(new Dimension(300, 40));

        JLabel yearLabel = new JLabel("Année Scolaire:");
        yearField = new JTextField(20);
        yearField.setPreferredSize(new Dimension(300, 40));

        // Ajouter les composants au panneau principal
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(levelLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(levelComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(yearLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(yearField, gbc);

        // Boutons de confirmation et d'annulation
        saveButton = new JButton("Enregistrer");
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.setBackground(new Color(46, 204, 113)); // Vert
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        cancelButton = new JButton("Annuler");
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setBackground(new Color(231, 76, 60)); // Rouge
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Ajouter les panneaux au panneau principal
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Méthodes pour récupérer les valeurs des champs
    public String getNomClasse() {
        return nameField.getText();
    }

    public String getNiveauScolaire() {
        return (String) levelComboBox.getSelectedItem();
    }

    public String getAnneeScolaire() {
        return yearField.getText();
    }

    public void setIdClasse(String id) {
        idField.setText(id);
    }

    public void setNomClasse(String nomClasse) {
        nameField.setText(nomClasse);
    }

    public void setNiveauScolaire(String niveauScolaire) {
        levelComboBox.setSelectedItem(niveauScolaire);
    }

    public void setAnneeScolaire(String anneeScolaire) {
        yearField.setText(anneeScolaire);
    }

    // Méthodes d'écouteurs pour les boutons
    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void addCancelListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }
}