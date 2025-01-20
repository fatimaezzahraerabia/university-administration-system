package view;

import controller.AppController;
import controller.SalleController;
import model.AffectationSalle;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class GestionSalleView extends JPanel {
    private SalleController salleController;
    private JTable table;
    private AffectationTableModel tableModel;
    private int loadAffectationsCounter = 0;

    public GestionSalleView(AppController appController) {
        salleController = new SalleController();
        initializeUI(appController);
    }

    private void initializeUI(AppController appController) {
        setLayout(new BorderLayout());

        // Add MenuPanel to the left of the GestionSalleView
        JPanel menuPanel = MenuPanel.createMenu(appController);
        menuPanel.setPreferredSize(new Dimension(300, 900)); // Set a specific width and height
        add(menuPanel, BorderLayout.WEST);

        // Add title
        JLabel titleLabel = new JLabel("Gestion des Salles", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new AffectationTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(40);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        // Add sorting functionality
        TableRowSorter<AffectationTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);

        // Create topPanel containing searchPanel and affecterSalleButton
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a panel for the search field and icon
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        searchPanel.setPreferredSize(new Dimension(600, 40)); // Increase the preferred size

        // Create and configure the search field
        JTextField searchField = new JTextField();
        searchField.setToolTipText("Rechercher...");
        searchField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchField.setForeground(Color.GRAY);
        searchField.setText("Rechercher...");

        // Add focus listener to handle placeholder text
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Rechercher...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Rechercher...");
                    tableModel.filterAffectations(""); // Clear filter when placeholder is shown
                }
            }
        });

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }

            private void applyFilter() {
                if (!searchField.getText().equals("Rechercher...")) {
                    tableModel.filterAffectations(searchField.getText());
                }
            }
        });
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Add search icon to the right
        JLabel searchIcon = new JLabel();
        java.net.URL searchIconURL = getClass().getResource("/icons/search.png");
        if (searchIconURL != null) {
            try {
                BufferedImage searchImage = ImageIO.read(searchIconURL);
                Image scaledImage = searchImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Enlarge the icon
                searchIcon.setIcon(new ImageIcon(scaledImage));
            } catch (IOException e) {
                System.err.println("Error loading search icon: " + e.getMessage());
            }
        } else {
            System.err.println("Search icon not found");
        }
        searchPanel.add(searchIcon, BorderLayout.EAST);

        topPanel.add(searchPanel, BorderLayout.CENTER);

        JButton affecterSalleButton = new JButton("Affecter une nouvelle salle");
        affecterSalleButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        affecterSalleButton.setBackground(new Color(52, 152, 219));
        affecterSalleButton.setForeground(Color.WHITE);
        affecterSalleButton.setFocusPainted(false);
        affecterSalleButton.setToolTipText("Cliquez pour affecter une nouvelle salle");

        affecterSalleButton.addActionListener(e -> {
            AffecterSalleDialog dialog = new AffecterSalleDialog((Frame) SwingUtilities.getWindowAncestor(this), appController, this);
            dialog.setVisible(true);
        });
        topPanel.add(affecterSalleButton, BorderLayout.EAST);

        // Create a new panel to hold both topPanel and scrollPane
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add mainPanel to the center of the main panel
        add(mainPanel, BorderLayout.CENTER);

        // Initialize buttonPanel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(buttonPanel, BorderLayout.SOUTH);

        loadAffectations();
    }

    public void loadAffectations() {
        loadAffectationsCounter++; // Increment the counter
        System.out.println("loadAffectations called " + loadAffectationsCounter + " times"); // Print the counter value

        // Clear existing data in the table model
        tableModel.setAffectations(List.of());

        // Load new data
        List<AffectationSalle> affectations = salleController.getAllAffectations();
        tableModel.setAffectations(affectations);
    }
}