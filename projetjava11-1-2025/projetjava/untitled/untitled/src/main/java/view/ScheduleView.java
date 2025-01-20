package view;

import controller.enseignantController;
import controller.AppController;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ScheduleView extends JPanel {

    private AppController appController; // Reference to AppController
    private int userId; // Teacher ID

    public ScheduleView(int userId, AppController appController) {
        this.userId = userId; // Store the teacher ID
        this.appController = appController; // Store the AppController reference

        setLayout(new BorderLayout());

        // Initialize the controller
        enseignantController controller = new enseignantController();

        // Fetch schedule data using the controller
        List<Object[]> scheduleData = controller.getScheduleData(userId);

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
        String[] menuItems = {"Accueil", "Les absences", "Déconnexion"};
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
            button.addActionListener(new NavigationListener(item, scheduleData, this, userId));
        }

        // ----- Right Panel: Schedule Table -----
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        JLabel tableTitle = new JLabel("Emploi du Temps", JLabel.CENTER);
        tableTitle.setFont(new Font("Arial", Font.BOLD, 22));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        // Table Column Names
        String[] columnNames = {"", "08h-10h", "10h-12h", "12h-14h", "14h-16h", "16h-18h"};

        // Table Model
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Add real data to the table model
        for (Object[] row : scheduleData) {
            tableModel.addRow(row);
        }

        // Table
        JTable horaireTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing
            }
        };
        horaireTable.setRowHeight(50);
        horaireTable.getTableHeader().setBackground(Color.gray);
        horaireTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        horaireTable.setFont(new Font("Arial", Font.PLAIN, 14));

        // Set table grid color and border
        horaireTable.setGridColor(Color.gray); // Black grid
        horaireTable.setShowHorizontalLines(true);
        horaireTable.setShowVerticalLines(true);

        // Add padding and border to table cells
        horaireTable.setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTextArea textArea = new JTextArea(value == null ? "" : value.toString());
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setOpaque(true);
                textArea.setFont(table.getFont());
                textArea.setEditable(false);
                textArea.setBorder(BorderFactory.createLineBorder(Color.gray, 1)); // Black border around cells
                textArea.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                textArea.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                return textArea;
            }
        });

        JScrollPane scrollPane = new JScrollPane(horaireTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray, 1)); // Black border around the scroll pane
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add panels to split pane
        splitPane.setLeftComponent(navPanel);
        splitPane.setRightComponent(tablePanel);

        // Add split pane to this JPanel
        add(splitPane, BorderLayout.CENTER);
    }
    private void disposePanel(JPanel panel) {
        Container parent = panel.getParent();
        if (parent != null) {
            parent.remove(panel); // Remove the panel from its parent
            parent.revalidate();  // Refresh the layout
            parent.repaint();     // Repaint the UI
        }
    }
    // Navigation Listener
    private class NavigationListener implements ActionListener {
        private String target;
        private List<Object[]> scheduleData;
        private JPanel currentPanel;
        private int teacherId;

        public NavigationListener(String target, List<Object[]> scheduleData, JPanel currentPanel, int teacherId) {
            this.target = target;
            this.scheduleData = scheduleData;
            this.currentPanel = currentPanel;
            this.teacherId = teacherId;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (target) {
                case "Accueil":
                    JOptionPane.showMessageDialog(currentPanel, "Vous êtes déjà sur Accueil !");
                    break;
                case "Les absences":
                    // Show Absences View

                    appController.showAbsenceView(teacherId);
                    // Open the absence view
                    break;
                case "Déconnexion":
                    int confirmed = JOptionPane.showConfirmDialog(
                            null,
                            "Voulez-vous vraiment vous déconnecter ?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirmed == JOptionPane.YES_OPTION) {
                        appController.showLoginView();
                    }
                    break;

            }
        }
    }
}
