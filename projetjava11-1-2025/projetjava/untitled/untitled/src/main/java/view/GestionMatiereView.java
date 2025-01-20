package view;


import controller.AppController;
import controller.VieScolaireController;
import model.Cours;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Enseignant;

import service.VieScolaireService;

public class GestionMatiereView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private AppController appController;
    private VieScolaireController vieScolaireController;

    public GestionMatiereView(AppController appController) {
        this.appController = appController;

        setLayout(new BorderLayout());

        JPanel sidebar = MenuPanel.createMenu(appController);
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Gestion des Cours", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContent.add(titleLabel, BorderLayout.NORTH);

        // Set up the button panel for the "Ajouter Cours" button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Ajouter Cours");
        addButton.setFont(new Font("Roboto", Font.PLAIN, 14));
        addButton.setBackground(new Color(52, 152, 219));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setPreferredSize(new Dimension(150, 40));

        addButton.addActionListener(e -> addCourse());

        buttonPanel.add(addButton);

        mainContent.add(buttonPanel, BorderLayout.SOUTH);

        // Add sidebar and main content to the panel
        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);


        String[] columnNames = {"IDCours", "Cours", "Type" , "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only make the "Actions" column editable
            }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        int courseId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    }
                }
            }
        });

        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor());
        refreshTable();
        JScrollPane scrollPane = new JScrollPane(table);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
    }

    private JButton createButton(String text, String iconName) {
        JButton button = new JButton(text);
        // Assuming you have icon images in your resources/icons directory
        // Example: add.png, edit.png, delete.png
        // You can use FlatLaf icons or any other icon library
        Icon icon = UIManager.getIcon("FileView.fileIcon"); // Placeholder icon
        button.setIcon(icon);
        button.setFocusPainted(false);
        button.setFont(new Font("Roboto", Font.PLAIN, 14));
        return button;
    }

    // Refresh JTable data from the controller
    private void refreshTable() {
        vieScolaireController = new VieScolaireController(new VieScolaireView(appController), new GestionClasseView(appController),new GestionEnseignantView(appController),new GestionCoursView(appController));

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        // Clear existing rows
        List<Cours> courses = vieScolaireController.GetCourses();
        if (courses != null) {
            courses.forEach(cours -> tableModel.addRow(new Object[]{
                    cours.getCoursID(),
                    cours.getNomCours(),
                    cours.getTypeCours(),
                    "Delete", // Action buttons
                    "Update"
            }));
        }
    }


    // Add a new course
    private void addCourse() {
        JTextField typeField = new JTextField();
        JTextField nameField = new JTextField();

        // Fetch enseignants with their IDs
        List<Enseignant> enseignants = vieScolaireController.GetEnseignant();
        Map<String, Integer> enseignantMap = new HashMap<>();
        for (Enseignant enseignant : enseignants) {
            enseignantMap.put(enseignant.getPrenom(), enseignant.getId());
        }

        // Create a dropdown with enseignant names
        JComboBox<String> enseignantsDropdown = new JComboBox<>(enseignantMap.keySet().toArray(new String[0]));

        // Styling fields with padding and rounded borders
        typeField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        enseignantsDropdown.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));
        enseignantsDropdown.setFont(new Font("Roboto", Font.PLAIN, 14));
        enseignantsDropdown.setPreferredSize(new Dimension(200, 30));

        // Improved dialog layout
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Type Cours:"));
        formPanel.add(typeField);
        formPanel.add(new JLabel("Nom Cours:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Enseignant:"));
        formPanel.add(enseignantsDropdown);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Ajouter un cours", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String type = typeField.getText().trim();
            String name = nameField.getText().trim();
            String selectedEnseignantName = (String) enseignantsDropdown.getSelectedItem();
            int enseignantId = enseignantMap.get(selectedEnseignantName); // Get the ID from the map

            if (!type.isEmpty() && !name.isEmpty()) {
                Cours newCourse = new Cours(0, type, name, enseignantId);

                // Create VieScolaireController dynamically if not already created
                if (vieScolaireController == null) {
                    // Create GestionCoursView instance since that’s what you want to pass
                    GestionCoursView gestionCoursView = new GestionCoursView(appController);
                    vieScolaireController = new VieScolaireController(new VieScolaireView(appController), new GestionClasseView(appController),new GestionEnseignantView(appController),new GestionCoursView(appController));
                }

                // Now call createCours on vieScolaireController
                vieScolaireController.createCours(newCourse);
                refreshTable();
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

            updateButton = createStyledButton("Modifier", new Color(46, 204, 113), Color.WHITE);
            deleteButton = createStyledButton("Supprimer", new Color(231, 76, 60), Color.WHITE);

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

            updateButton = createStyledButton("Modifier", new Color(46, 204, 113), Color.WHITE);
            deleteButton = createStyledButton("Supprimer", new Color(231, 76, 60), Color.WHITE);

            // Action listeners for buttons
            updateButton.addActionListener(e -> {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                if (row < 0 || row >= tableModel.getRowCount()) {
                    fireEditingStopped();
                    return;
                }
                updateCourse(row);
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                if (row < 0 || row >= tableModel.getRowCount()) {
                    fireEditingStopped();
                    return;
                }
                deleteCourse(row);
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

    private void updateCourse(int row) {
        vieScolaireController = new VieScolaireController(new VieScolaireView(appController), new GestionClasseView(appController),new GestionEnseignantView(appController),new GestionCoursView(appController));
        Cours selectedCours = vieScolaireController.GetCourses().get(row);

        JTextField typeField = new JTextField(selectedCours.getTypeCours());
        JTextField nameField = new JTextField(selectedCours.getNomCours());
        JComboBox<String> enseignantsDropdown = new JComboBox<>(vieScolaireController.fetchEnseignants().toArray(new String[0]));
        enseignantsDropdown.setSelectedIndex(selectedCours.getIdUtilisateur() - 1);

        // Styling fields with padding and rounded borders
        typeField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        enseignantsDropdown.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));
        enseignantsDropdown.setFont(new Font("Roboto", Font.PLAIN, 14));
        enseignantsDropdown.setPreferredSize(new Dimension(200, 30));

        // Improved dialog layout
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Type Cours:"));
        formPanel.add(typeField);
        formPanel.add(new JLabel("Nom Cours:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Enseignant:"));
        formPanel.add(enseignantsDropdown);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Modifier un cours", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String type = typeField.getText().trim();
            String name = nameField.getText().trim();
            int enseignantId = enseignantsDropdown.getSelectedIndex() + 1;

            if (!type.isEmpty() && !name.isEmpty()) {
                Cours updatedCours = new Cours(selectedCours.getCoursID(), type, name, enseignantId);
                vieScolaireController.updateCours(row, updatedCours);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cours modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Delete an existing course
    private void deleteCourse(int row) {
        vieScolaireController = new VieScolaireController(new VieScolaireView(appController), new GestionClasseView(appController),new GestionEnseignantView(appController),new GestionCoursView(appController));

        if (row < 0 || row >= vieScolaireController.GetCourses().size()) {
            JOptionPane.showMessageDialog(this, "Row index is invalid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce cours ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            vieScolaireController.deleteCours(row);

            // Ensure the data model is refreshed after deletion
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }

            refreshTable();

            JOptionPane.showMessageDialog(this, "Cours supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }




    public JTable getTable() {
        return table;
    }
}