package view;

import controller.AppController;
import controller.VieScolaireController;
import model.Cours;
import model.Enseignant;
import model.Role;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class  GestionEnseignantView extends JPanel {
    private AppController appController;
    private JTable table;
    private DefaultTableModel tableModel;
    private VieScolaireController vieScolaireController;

    public GestionEnseignantView(AppController appController) {
        this.appController = appController;

        setLayout(new BorderLayout());

        // Barre latérale
        JPanel sidebar = MenuPanel.createMenu(appController);

        // Contenu principal
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Enseignants", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Ajout de padding
        mainContent.add(titleLabel, BorderLayout.NORTH);

        // Set up the button panel for the "Ajouter Cours" button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Ajouter Engeignant");
        addButton.setFont(new Font("Roboto", Font.PLAIN, 14));
        addButton.setBackground(new Color(52, 152, 219));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setPreferredSize(new Dimension(150, 40));

        addButton.addActionListener(e -> addEnseignant());

        buttonPanel.add(addButton);

        mainContent.add(buttonPanel, BorderLayout.SOUTH);

        // Configuration de la table dynamique
        String[] columnNames = {"Nom","Prenom", "Email", "Adresse", "Genre", "Téléphone","Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;// Rendre les cellules non modifiables
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);

        table.getColumn("Actions").setCellRenderer(new GestionEnseignantView.ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new GestionEnseignantView.ButtonEditor());
        refreshTable();

        // Ajout de la table dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        // Ajout des panneaux à la vue
        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
    }


    // Refresh JTable data from the controller
    private void refreshTable() {
        vieScolaireController = new VieScolaireController(new VieScolaireView(appController), this);

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        // Clear existing rows
        List<Enseignant> courses = vieScolaireController.loadEnseignants();
        if (courses != null) {
            courses.forEach(cours -> tableModel.addRow(new Object[]{
                    cours.getPrenom(),
                    cours.getGmail(),
                    cours.getAdresse(),
                    cours.getGenre(),
                    cours.getTelephone(),
                    "Delete", // Action buttons
                    "Update"
            }));
        }
    }

    // Add a new course
    private void addEnseignant() {
        JTextField prenomField = new JTextField();
        JTextField nomField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField adresseField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField teleField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Prenom:"));
        formPanel.add(prenomField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Adresse:"));
        formPanel.add(adresseField);
        formPanel.add(new JLabel("Genre:"));
        formPanel.add(genreField);
        formPanel.add(new JLabel("Téléphone:"));
        formPanel.add(teleField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Ajouter Enseignant", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String email = emailField.getText().trim();
            String adresse = adresseField.getText().trim();
            String genre = genreField.getText().trim();
            String tele = teleField.getText().trim();

            if (!nom.isEmpty() && !prenom.isEmpty() && !email.isEmpty() && !adresse.isEmpty() && !genre.isEmpty() && !tele.isEmpty()) {
                Enseignant newEnseignant = new Enseignant(0, nom, "", null, prenom, email, adresse, genre, tele);
                vieScolaireController.createEnseignant(newEnseignant);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Enseignant ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Custom renderer for the action buttons
    // Custom renderer for the action buttons
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton updateButton;
        private JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Increased spacing
            setOpaque(true);

            updateButton = createStyledButton("Mod", new Color(46, 204, 113), Color.WHITE);
            deleteButton = createStyledButton("Sup", new Color(231, 76, 60), Color.WHITE);

            add(updateButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(Color.WHITE);
            }
            return this;
        }
    }

    // Custom editor for the action buttons
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton updateButton;
        private JButton deleteButton;

        public ButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Increased spacing
            panel.setOpaque(true);

            updateButton = createStyledButton("Mod", new Color(46, 204, 113), Color.WHITE);
            deleteButton = createStyledButton("Sup", new Color(231, 76, 60), Color.WHITE);

            // Action listeners for buttons
            updateButton.addActionListener(e -> {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                if (row < 0 || row >= tableModel.getRowCount()) {
                    fireEditingStopped();
                    return;
                }
                updateEnseignant(row);
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                if (row < 0 || row >= tableModel.getRowCount()) {
                    fireEditingStopped();
                    return;
                }
                deleteEnseignant(row);
                fireEditingStopped();
            });


            panel.add(updateButton);
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Padding for the button
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }


    //update cours

    private void updateEnseignant(int row) {
        vieScolaireController = new VieScolaireController(new VieScolaireView(appController), this);

        // Fetch selected Enseignant
        Enseignant selectedEnseignant = vieScolaireController.GetEnseignant().get(row);

        // Debugging: log the selected enseignant data
        System.out.println("Selected Enseignant ID: " + selectedEnseignant.getId());
        System.out.println("Selected Enseignant Name: " + selectedEnseignant.getNom());
        System.out.println("Selected Enseignant Password: " + selectedEnseignant.getPassword());

        JTextField nomField = new JTextField(selectedEnseignant.getNom());
        JTextField prenomField = new JTextField(selectedEnseignant.getPrenom());
        JTextField emailField = new JTextField(selectedEnseignant.getGmail());
        JTextField adresseField = new JTextField(selectedEnseignant.getAdresse());
        JTextField genreField = new JTextField(selectedEnseignant.getGenre());
        JTextField teleField = new JTextField(selectedEnseignant.getTelephone());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Prenom:"));
        formPanel.add(prenomField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Adresse:"));
        formPanel.add(adresseField);
        formPanel.add(new JLabel("Genre:"));
        formPanel.add(genreField);
        formPanel.add(new JLabel("Téléphone:"));
        formPanel.add(teleField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Modifier Enseignant", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String email = emailField.getText().trim();
            String adresse = adresseField.getText().trim();
            String genre = genreField.getText().trim();
            String tele = teleField.getText().trim();

            if (!nom.isEmpty() && !prenom.isEmpty() && !email.isEmpty() && !adresse.isEmpty() && !genre.isEmpty() && !tele.isEmpty()) {
                // Debugging: log the updated values before passing them to the controller
                System.out.println("Updating Enseignant with ID: " + selectedEnseignant.getId());
                System.out.println("Updated Name: " + nom);
                System.out.println("Updated Email: " + email);

                // Use existing password and role
                String password = selectedEnseignant.getPassword(); // Existing password
                Role role = selectedEnseignant.getRole(); // Existing role

                // Create the updated Enseignant object
                Enseignant updatedEnseignant = new Enseignant(selectedEnseignant.getId(), nom, password, role, prenom, email, adresse, genre, tele);

                // Call the update method in the controller
                vieScolaireController.updateEnseignant(row, updatedEnseignant);

                // Refresh the table and show a success message
                refreshTable();
                JOptionPane.showMessageDialog(this, "Enseignant modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }




    // Delete an existing course
    private void deleteEnseignant(int row) {
        vieScolaireController = new VieScolaireController(new VieScolaireView(appController), this);
        Enseignant selectedEnseignant = vieScolaireController.GetEnseignant().get(row);
        if (row < 0 || row >= vieScolaireController.GetEnseignant().size()) {
            JOptionPane.showMessageDialog(this, "Row index is invalid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce cours ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            vieScolaireController.deleteEnseignant(selectedEnseignant.getId());

            // Ensure the data model is refreshed after deletion
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }

            refreshTable();

            JOptionPane.showMessageDialog(this, "Enseignant supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void updateEnseignantTable(List<Enseignant> enseignants) {
        // Efface les anciennes données de la table
        tableModel.setRowCount(0);

        // Vérifie si la liste d'enseignants est null ou vide
        if (enseignants == null || enseignants.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun enseignant à afficher.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Ajoute les données des enseignants dans la table
        for (Enseignant enseignant : enseignants) {
            tableModel.addRow(new Object[]{
                    enseignant.getNom(),
                    enseignant.getPrenom(),
                    enseignant.getGmail(),
                    enseignant.getAdresse(),
                    enseignant.getGenre(),
                    enseignant.getTelephone()
            });
        }
    }

    public JTable getTable() {
        return table;
    }
}