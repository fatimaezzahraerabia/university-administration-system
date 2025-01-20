package view;

import com.formdev.flatlaf.FlatLightLaf;
import controller.AppController;
import controller.EleveController;
import controller.VieScolaireController;
import model.Eleve;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GestionEleveView extends JFrame {

    private DefaultTableModel tableModel;
    private JLabel titleLabel;
    private AppController appController;
    private JTable table;

    private JButton addButton;
    private JButton backButton;

    public GestionEleveView(String nomClasse, List<Eleve> eleves) {
        this.appController = appController;

        // Appliquer le thème FlatLaf
        FlatLightLaf.setup();

        // Initialisation des composants
        initializeComponents(nomClasse, eleves);

        // Création du tableau
        table = createTable(eleves, nomClasse);

        // Configuration de l'interface
        configureLayout(nomClasse, eleves);

        // Ajouter des écouteurs d'événements
        this.addButton.addActionListener(e -> {
            new VieScolaireController(
                    new VieScolaireView(appController),
                    new GestionClasseView(appController), new GestionEnseignantView(appController), new GestionCoursView(appController)
            ).openAddEleveDialog(nomClasse);
        });

        this.backButton.addActionListener(e -> {
            // Fermer la fenêtre actuelle
            dispose();
        });

        // Configurer le JFramee
        setTitle("Gestion des Élèves");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Agrandir la fenêtre pour qu'elle occupe tout l'écran
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initializeComponents(String nomClasse, List<Eleve> eleves) {

        // Titre
        titleLabel = new JLabel("Gestion des Élèves - Classe : " + nomClasse, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Bouton Ajouter
        addButton = new JButton("Ajouter un élève");
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addButton.setBackground(new Color(52, 152, 219)); // Bleu
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Bouton Retour
        backButton = new JButton("Retour");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setBackground(new Color(231, 76, 60)); // Rouge
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void configureLayout(String nomClasse, List<Eleve> eleves) {
        setLayout(new BorderLayout());

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.add(titleLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContent.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);
        mainContent.add(buttonPanel, BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
    }

    private JTable createTable(List<Eleve> eleves, String nomClasse) {
        // Ajout des nouvelles colonnes dans l'en-tête, y compris l'ID utilisateur
        String[] columnNames = {"Nom", "Prénom", "Gmail", "Adresse", "Genre", "Date de Naissance", "ID Utilisateur", "Actions"};

        // Mise à jour des données avec l'ajout de l'ID utilisateur
        Object[][] data = eleves.stream()
                .map(eleve -> new Object[]{
                        eleve.getNom(),
                        eleve.getPrenom(),
                        eleve.getPassword(), // Gmail de l'élève
                        eleve.getAdresse(), // Adresse de l'élève
                        eleve.getGenre(), // Genre de l'élève
                        eleve.getDatedenaissance(),
                        eleve.getId(), // ID de l'élève
                        null // Colonne pour les actions
                })
                .toArray(Object[][]::new);

        // Création du modèle de la table
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Seule la colonne "Actions" est éditable
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Ajout du renderer et de l'éditeur pour la colonne "Actions"
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), eleves, nomClasse, appController, this));

        // Appliquer le renderer personnalisé pour les en-têtes des colonnes
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(52, 152, 219)); // Bleu
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("SansSerif", Font.BOLD, 14));
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        return table;
    }

    public JTable getTable() {
        return table;
    }

    public void updateElevesList(List<Eleve> updatedEleves) {
        tableModel.setRowCount(0); // Réinitialiser le modèle de la table

        for (Eleve eleve : updatedEleves) {
            tableModel.addRow(new Object[]{
                    eleve.getNom(),              // Nom de l'élève
                    eleve.getPrenom(),           // Prénom de l'élève
                    eleve.getPassword(),         // Gmail de l'élève
                    eleve.getAdresse(),          // Adresse de l'élève
                    eleve.getGenre(),            // Genre de l'élève
                    eleve.getDatedenaissance(),  // Date de naissance de l'élève
                    eleve.getEleveID(),          // ID de l'élève
                    null                         // Colonne pour les actions
            });
        }

        tableModel.fireTableDataChanged(); // Rafraîchir la table
    }
}

// Renderer pour afficher les boutons dans la colonne "Actions"
class ButtonRenderer extends JPanel implements TableCellRenderer {
    private final JButton editButton;

    public ButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        editButton = new JButton("Modifier");
        editButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        editButton.setBackground(new Color(46, 204, 113));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(39, 174, 96), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(editButton);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

// Editor pour gérer les actions des boutons dans la colonne "Actions"
class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final JPanel panel;
    private final JButton editButton;

    private List<Eleve> eleves;
    private int currentRow;
    private String nomClasse;
    private AppController appController;

    public ButtonEditor(JCheckBox checkBox, List<Eleve> eleves, String nomClasse, AppController appController, GestionEleveView gestionEleveView) {
        this.eleves = eleves;
        this.nomClasse = nomClasse;
        this.appController = appController;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        editButton = new JButton("Modifier");
        editButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        editButton.setBackground(new Color(46, 204, 113));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentRow >= 0 && currentRow < eleves.size()) {
                    Eleve selectedEleve = eleves.get(currentRow);
                    if (selectedEleve != null) {
                        EleveController eleveController = new EleveController(
                                new ModifierEleveView(),
                                gestionEleveView,
                                selectedEleve,
                                new JFrame(),
                                nomClasse
                        );
                        eleveController.openModifierEleveDialog(selectedEleve, nomClasse);
                    }
                }
            }
        });

        panel.add(editButton);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentRow = row;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null; // Pas de valeur spécifique à retourner puisque c'est juste un bouton
    }
}