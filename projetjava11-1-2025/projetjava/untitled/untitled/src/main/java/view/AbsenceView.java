package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import controller.AppController;

public class AbsenceView extends JFrame {
    private JTable absenceTable;
    private DefaultTableModel tableModel;
    private AppController appController; // Reference to AppController
    private int teacherId; // Teacher ID

    public AbsenceView(int teacherId, AppController appController) {
        this.teacherId = teacherId; // Store the teacher ID
        this.appController = appController; // Store the AppController reference

        setTitle("Liste des Absences");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Maximize the window

        // Main Container with Split Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(2);
        splitPane.setDividerLocation(250); // Width of navigation bar

        // ----- Left Panel: Navigation -----
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(54, 79, 107)); // Dark blue background

        JLabel titleLabel = new JLabel("Menu");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        navPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        navPanel.add(titleLabel);

        // Menu Buttons
        String[] menuItems = {"Accueil", "Déconnexion"};
        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setOpaque(false); // Make the button background transparent
            button.setContentAreaFilled(false); // Remove default content background
            button.setBorderPainted(false); // Remove the button border
            button.setForeground(Color.WHITE); // Set text color to white
            button.setFocusPainted(false);
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            navPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            navPanel.add(button);

            // Add Action Listener for Navigation
            button.addActionListener(new NavigationListener(item, this, teacherId));
        }

        // ----- Right Panel: Absence Table -----
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        JLabel tableTitle = new JLabel("Liste des Absences", JLabel.CENTER);
        tableTitle.setFont(new Font("Arial", Font.BOLD, 22));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        // Table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Cours");
        tableModel.addColumn("Élève");
        tableModel.addColumn("Date");
        tableModel.addColumn("Justification");

        // Table
        absenceTable = new JTable(tableModel);
        absenceTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(absenceTable);

        // Add to panel
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add panels to split pane
        splitPane.setLeftComponent(navPanel);
        splitPane.setRightComponent(tablePanel);

        // Add split pane to frame
        add(splitPane);
    }

    // Method to update the absence table
    public void updateAbsenceTable(List<Object[]> absences) {
        tableModel.setRowCount(0); // Clear the table
        for (Object[] row : absences) {
            tableModel.addRow(row);
        }
    }

    // Navigation Listener
    private class NavigationListener implements ActionListener {
        private String target;
        private JFrame currentFrame;
        private int teacherId;

        public NavigationListener(String target, JFrame currentFrame, int teacherId) {
            this.target = target;
            this.currentFrame = currentFrame;
            this.teacherId = teacherId;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (target) {
                case "Accueil":
                    // Go back to the schedule view (Accueil)
                    currentFrame.dispose(); // Close the absence view
                    appController.showScheduleView(teacherId); // Open the schedule view
                    break;
                case "Déconnexion":
                    // Close the app
                    int choice = JOptionPane.showConfirmDialog(currentFrame, "Êtes-vous sûr de vouloir vous déconnecter ?", "Déconnexion", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        currentFrame.dispose();
                    }
                    break;
            }
        }
    }
}