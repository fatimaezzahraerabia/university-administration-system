package view;

import com.formdev.flatlaf.FlatLightLaf;
import controller.AppController;
import controller.VieScolaireController;
import model.Classe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GestionClasseView extends JPanel {
private VieScolaireController vieScolaireController;
    private final AppController appController;
    private JTable table; // Table pour afficher les classes

    // Constructeur avec AppController
    public GestionClasseView(AppController appController) {
        this.appController = appController;

        // Appliquer le thème FlatLaf
        FlatLightLaf.setup();

        setLayout(new BorderLayout());

        // Sidebar (menu)
        JPanel sidebar = MenuPanel.createMenu(appController);

        // Tableau pour afficher les classes
        JPanel mainContent = new JPanel(new BorderLayout());

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Classes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainContent.add(titleLabel, BorderLayout.NORTH);

        table = createTable(); // Initialisation de la table

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainContent.add(scrollPane, BorderLayout.CENTER);
        // Bouton "Ajouter" en bas du tableau
        JButton addButton = new JButton("Ajouter");
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addButton.setBackground(new Color(52, 152, 219)); // Bleu
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.addActionListener(e ->new VieScolaireController(new VieScolaireView(appController),this,new GestionEnseignantView(appController),new GestionCoursView(appController)).openAddClassDialog());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);

        mainContent.add(buttonPanel, BorderLayout.SOUTH);

        // Ajouter les panneaux au layout principal
        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
    }

    private JTable createTable() {
        // Définir les colonnes
        String[] columnNames = {"ID", "Nom Classe", "Niveau Scolaire", "Année Scolaire", "Action"};

        // Initialiser le modèle du tableau avec 0 lignes
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Ne permet à aucune cellule d'être éditée
                return false;
            }
        };

        JTable table = new JTable(model);
table.setDefaultEditor(Object.class,null);
        // Design FlatLaf pour le tableau
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);

        table.getColumn("Action").setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

                // Create the "Edit" button
                JButton editButton = new JButton("Modifier");
                editButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
                editButton.setBackground(new Color(46, 204, 113)); // Green
                editButton.setForeground(Color.WHITE);
                editButton.setFocusPainted(false);
                GestionClasseView gestionClasseView = new GestionClasseView(appController) ;
                // Adding the action listener to the button
                editButton.addActionListener(e -> {
                    VieScolaireController controller = new VieScolaireController(
                            new VieScolaireView(appController),
                            gestionClasseView,
                            new GestionEnseignantView(appController),
                            new GestionCoursView(appController)
                    );
                    controller.openEditClassDialog(row);

                    // Fermer la vue gestionClasseView


                    // Rouvrir la vue gestionClasseView après un court délai
                   refreshView();
                });
                // Create the "Delete" button
                JButton deleteButton = new JButton("Supprimer");
                deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
                deleteButton.setBackground(new Color(231, 76, 60)); // Red
                deleteButton.setForeground(Color.WHITE);
                deleteButton.setFocusPainted(false);
                // Adding the action listener to the button
                deleteButton.addActionListener(e -> {
                    System.out.println("Supprimer button clicked");  // Debugging message
                    try {
                        deleteRow(row);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                // Add buttons to the panel
                panel.add(editButton);

                return panel;  // Return the panel with buttons
            }
        });

        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                if (column == table.getColumn("Nom Classe").getModelIndex()) {
                    table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    table.setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                // Vérifiez si la colonne cliquée est celle du nom de la classe
                if (column == table.getColumn("Nom Classe").getModelIndex()) {
                    String className = table.getValueAt(row, column).toString();
                    int classId = (int) table.getValueAt(row, 0); // Supposons que l'ID de la classe est dans la colonne 0
                    VieScolaireController vieScolaireController = appController.getVieScolaireController();
                    if (vieScolaireController != null) {
                        vieScolaireController.openGestionEleveView(classId, className);
                    } else {
                        System.err.println("VieScolaireController no está inicializado.");
                        // Manejar el caso de error, por ejemplo, mostrar un mensaje al usuario
                    }


                }
            }
        });

// Gestion des clics


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                if (column == table.getColumn("Action").getModelIndex()) {
                    if (e.getButton() == MouseEvent.BUTTON1) { // Left click
                        JButton editButton = (JButton) ((JPanel) table.getCellRenderer(row, column).getTableCellRendererComponent(table, null, false, false, row, column)).getComponent(0);
                        editButton.doClick(); // Simulate button click
                    }
                }
            }
        });
        return table;
    }

    public JTable getTable() {
        return table;
    }


    private void openEleveView(int classId, String className) {
        // Créer une nouvelle JFrame
        JFrame eleveFrame = new JFrame("Élèves de la classe : " + className);
        eleveFrame.setSize(600, 400);
        eleveFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());

        // Titre
        JLabel titleLabel = new JLabel("Élèves de la classe : " + className, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Créer une table pour afficher les élèves
       // Méthode pour créer la table



        // Bouton pour fermer
        JButton closeButton = new JButton("Fermer");
        closeButton.addActionListener(e -> eleveFrame.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Ajouter le panel à la fenêtre
        eleveFrame.add(panel);
        eleveFrame.setLocationRelativeTo(null); // Centrer la fenêtre
        eleveFrame.setVisible(true);
    }



    // Méthode pour mettre à jour la table avec une liste de classes
    public void updateClasseTable(List<Classe> classes) {
        if (classes.isEmpty()) {
            System.out.println("La liste des classes est vide.");
        } else {
            System.out.println("Nombre de classes : " + classes.size());
        }
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Effacer les lignes existantes

        for (Classe classe : classes) {
            model.addRow(new Object[]{
                    classe.getId(),
                    classe.getNomClasse(),
                    classe.getNiveauScolaire(),
                    classe.getAnneeScolaire(),
                    "Action"
            });
        }

    }
    public String getSelectedClasseName() {
        // Récupérer la ligne sélectionnée dans la JTable
        int selectedRow = getTable().getSelectedRow();

        if (selectedRow == -1) {
            // Aucune ligne n'est sélectionnée
            return null;
        }

        // Supposons que la première colonne de la table contient le nom de la classe
        String nomClasse = (String) getTable().getValueAt(selectedRow, 1);

        return nomClasse;
    }

    // Fonction pour supprimer la ligne
    private void deleteRow(int row) {
        // Supprimer la ligne sélectionnée
        ((DefaultTableModel) table.getModel()).removeRow(row);
    }
    private void onEditButtonClick(int row) {
        // Action pour le bouton "Modifier"
        JOptionPane.showMessageDialog(this, "Modification de l'élève à la ligne " + (row + 1));
    }

        // ...existing code...

        public void refreshView() {
            removeAll();
            revalidate();
            repaint();
            initializeUI(appController,vieScolaireController);

        }

        private void initializeUI(AppController appController,VieScolaireController vieScolaireController) {
            // ...existing code...
            // Sidebar (menu)
            JPanel sidebar = MenuPanel.createMenu(appController);

            // Tableau pour afficher les classes
            JPanel mainContent = new JPanel(new BorderLayout());
vieScolaireController = new VieScolaireController(new VieScolaireView(appController),this,new GestionEnseignantView(appController),new GestionCoursView(appController));
            // Titre
            JLabel titleLabel = new JLabel("Gestion des Classes", SwingConstants.CENTER);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            mainContent.add(titleLabel, BorderLayout.NORTH);

            table = createTable(); // Initialisation de la table

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            mainContent.add(scrollPane, BorderLayout.CENTER);
            // Bouton "Ajouter" en bas du tableau
            JButton addButton = new JButton("Ajouter");
            addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            addButton.setBackground(new Color(52, 152, 219)); // Bleu
            addButton.setForeground(Color.WHITE);
            addButton.setFocusPainted(false);
            addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            buttonPanel.add(addButton);
            addButton.addActionListener(e ->new VieScolaireController(new VieScolaireView(appController),this,new GestionEnseignantView(appController),new GestionCoursView(appController)).openAddClassDialog());

            mainContent.add(buttonPanel, BorderLayout.SOUTH);

            add(sidebar, BorderLayout.WEST);
            add(mainContent, BorderLayout.CENTER);
            vieScolaireController.updateClasses();
        }

        // ...existing code..
}
