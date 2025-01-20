// ClassCourView.java
package view;

import com.formdev.flatlaf.FlatLightLaf;
import controller.AppController;
import model.Classe;
import model.Eleve;
import service.ClasseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClassCourView extends JFrame {
    private final AppController appController;
    private JTable table;
    private ClasseService classeService;
    private int currentCourseId;

    public ClassCourView(AppController appController, int courseId) {
        this.appController = appController;
        this.classeService = new ClasseService();
        this.currentCourseId = courseId;
        FlatLightLaf.setup();
        setTitle("Les Classes");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Maximize the window to fit the screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainContent = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configuration du bouton back
        JButton backButton = new JButton("<--");
        backButton.setBackground(new Color(52, 73, 94));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.addActionListener(e -> dispose());

        // Ajout du bouton back au panneau supérieur
        topPanel.add(backButton, BorderLayout.WEST);

        mainContent.add(topPanel, BorderLayout.WEST);

        // Ajout du panneau supérieur
        add(topPanel, BorderLayout.NORTH);
        JLabel titleLabel = new JLabel("Les Classes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainContent.add(titleLabel, BorderLayout.NORTH);

        table = createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContent.add(scrollPane, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int classId = (int) table.getValueAt(selectedRow, 0);
                    openTousAbsView(classId, currentCourseId);
                }
            }
        });

        // Ensure the window is visible
        setVisible(true);
    }

    private JTable createTable() {
        String[] columnNames = {"ID", "Nom Classe", "Niveau Scolaire", "Année Scolaire"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setDefaultEditor(Object.class, null);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);

        return table;
    }

    public void updateClasseTable(List<Classe> classes) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Classe classe : classes) {
            model.addRow(new Object[]{
                    classe.getId(),
                    classe.getNomClasse(),
                    classe.getNiveauScolaire(),
                    classe.getAnneeScolaire()
            });
        }
    }

    private void openTousAbsView(int classId, int idCour) {
        List<Eleve> eleves = classeService.getElevesByClasseId(classId);
        new TousAbsView(eleves, idCour);
    }

    public JTable getTable() {
        return table;
    }

    public int getCurrentCourseId() {
        return currentCourseId;
    }
}