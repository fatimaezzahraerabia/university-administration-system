package view;

import controller.AppController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionCoursView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private AppController appController;

    public GestionCoursView(AppController appController) {
        this.appController = appController;
        setLayout(new BorderLayout());

        JPanel sidebar = MenuPanel.createMenu(appController);
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Gestion des absences", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContent.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"IDCours", "Cours", "Type"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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
                        appController.showClassCourView(courseId);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
    }

    public JTable getTable() {
        return table;
    }
}