package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EnseignantTimetable {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Enseignant - Horaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create a panel for the sidebar menu
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(45, 62, 80));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Add buttons to the sidebar
        JButton homeButton = new JButton("Accueil");
        JButton absencesButton = new JButton("Les absences");
        JButton logoutButton = new JButton("Déconnexion");

        // Customize button styles
        for (JButton button : new JButton[]{homeButton, absencesButton, logoutButton}) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(200, 50));
            button.setBackground(new Color(64, 92, 120));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
        }

        sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
        sidebar.add(homeButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(absencesButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(logoutButton);

        // Create a panel for the timetable
        JPanel timetablePanel = new JPanel();
        timetablePanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Horaire", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timetablePanel.add(titleLabel, BorderLayout.NORTH);

        // Create a table for the timetable
        String[] columnNames = {"", "08h-10h", "10h-12h", "12h-14h", "14h-16h", "16h-18h"};
        String[][] data = fetchTimetableData();
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable timetableTable = new JTable(tableModel);
        timetableTable.setRowHeight(50);

        JScrollPane scrollPane = new JScrollPane(timetableTable);
        timetablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add panels to the frame
        frame.setLayout(new BorderLayout());
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(timetablePanel, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
    }

    // Fetch timetable data from the database
    private static String[][] fetchTimetableData() {
        String[][] data = new String[6][6]; // Days of the week + time slots
        String[] days = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};

        try {
            // Database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_ecole", "root", "");
            Statement stmt = conn.createStatement();

            // Fill data for each day
            for (int i = 0; i < days.length; i++) {
                data[i][0] = days[i]; // Day name in the first column

                String query = "SELECT * FROM cours WHERE jour = '" + days[i] + "' ORDER BY heure_debut";
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    int timeSlot = mapTimeToColumn(rs.getString("heure_debut"));
                    data[i][timeSlot] = rs.getString("nom_cours");
                }
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    // Map time slots to table columns
    private static int mapTimeToColumn(String time) {
        switch (time) {
            case "08:00:00": return 1;
            case "10:00:00": return 2;
            case "12:00:00": return 3;
            case "14:00:00": return 4;
            case "16:00:00": return 5;
            default: return 0;
        }
    }
}
