package view;

import model.Absence;
import model.Eleve;
import service.AbsenceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TousAbsView extends JFrame {

    private JTable absencesTable;
    private DefaultTableModel tableModel;
    private int idCour;
    private int idVieScolaire;
    private List<Eleve> eleves;
    private AbsenceService serviceAbsence;

    public TousAbsView(List<Eleve> eleves, int idCour) {
        this.eleves = eleves;
        this.idCour = idCour;
        this.serviceAbsence = new AbsenceService();

        setTitle("Gestion des Absences");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setLayout(new BorderLayout());

        JPanel mainContent = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("<--");
        backButton.setBackground(new Color(52, 73, 94));
        backButton.setForeground(Color.white);
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.addActionListener(e -> dispose());
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Liste des Absences", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainContent.add(topPanel, BorderLayout.NORTH);

        absencesTable = createTable();
        JScrollPane scrollPane = new JScrollPane(absencesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContent.add(scrollPane, BorderLayout.CENTER);

        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.white);
        saveButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        saveButton.setPreferredSize(new Dimension(150, 50));
        JButton reportButton = new JButton("Envoyer Rapport");
        reportButton.setBackground(new Color(52, 152, 219));
        reportButton.setForeground(Color.white);
        reportButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        reportButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        reportButton.setPreferredSize(new Dimension(150, 50));
        reportButton.addActionListener(e -> sendReportToParents());

        JButton openFileButton = new JButton("Ouvrir Fichier");
        openFileButton.setBackground(new Color(241, 196, 15));
        openFileButton.setForeground(Color.white);
        openFileButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        openFileButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        openFileButton.setPreferredSize(new Dimension(150, 50));
        openFileButton.addActionListener(e -> openAbsencesFile());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(openFileButton);
        mainContent.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            saveAbsences();
            saveAbsencesToFile();
        });

        add(mainContent, BorderLayout.CENTER);

        // Fetch and update the table with the list of students initially
        updateAbsencesTable(eleves);

        setVisible(true);
    }

    private JTable createTable() {
        String[] columnNames = {"ID", "Étudiant", "Status", "Justification"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column ==  2 || column == 3;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        return table;
    }

    public void updateAbsencesTable(List<Eleve> eleves) {
        tableModel.setRowCount(0);

        for (Eleve eleve : eleves) {
            tableModel.addRow(new Object[]{
                    eleve.getEleveID(),
                    eleve.getNom() + " " + eleve.getPrenom(),
                    "Présent",
                    ""
            });
        }
    }

    private void saveAbsences() {
        AbsenceService absenceService = new AbsenceService();
        Date today = new Date(System.currentTimeMillis());

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int eleveId = (int) tableModel.getValueAt(i, 0);
            String status = (String) tableModel.getValueAt(i, 2);
            String justification = (String) tableModel.getValueAt(i, 3);

            Absence absence = new Absence();
            absence.setIdEleve(eleveId);
            absence.setIdCours(idCour);
            absence.setIdVieScolaire(idVieScolaire);
            absence.setStatus(status);
            absence.setJustification(justification);
            absence.setDateAbsence(today);

            // Check if the absence already exists for the given student, course, and date
            Absence existingAbsence = absenceService.getAbsenceByEleveAndCourseAndDate(eleveId, idCour, today);
            if (existingAbsence != null) {
                // Update the existing absence
                existingAbsence.setStatus(status);
                existingAbsence.setJustification(justification);
                absenceService.updateAbsence(existingAbsence);
            } else {
                // Save the new absence
                absenceService.saveAbsence(absence);
            }
        }

        JOptionPane.showMessageDialog(this, "Absences enregistrées avec succès !");
        refreshTableForToday();
    }

    private void refreshTableForToday() {
        AbsenceService absenceService = new AbsenceService();
        LocalDate today = LocalDate.now();
        List<Absence> updatedAbsences = absenceService.getAbsencesByCourseIdAndDate(idCour, today);

        // If no absences are found for today, show the list of students
        if (updatedAbsences.isEmpty()) {
            updateAbsencesTable(eleves);
        } else {
            updateAbsencesTableFromAbsences(updatedAbsences);
        }
    }

    private void updateAbsencesTableFromAbsences(List<Absence> absences) {
        tableModel.setRowCount(0);

        for (Absence absence : absences) {
            Eleve eleve = getEleveById(absence.getIdEleve());
            if (eleve != null) {
                tableModel.addRow(new Object[]{
                        eleve.getEleveID(),
                        eleve.getNom() + " " + eleve.getPrenom(),
                        absence.getStatus(),
                        absence.getJustification()
                });
            }
        }
    }

    private Eleve getEleveById(int eleveId) {
        for (Eleve eleve : eleves) {
            if (eleve.getEleveID() == eleveId) {
                return eleve;
            }
        }
        return null;
    }

    public JTable getAbsencesTable() {
        return absencesTable;
    }

    public void sendReportToParents() {
        System.out.println("sendReportToParents method called");
        StringBuilder report = new StringBuilder();

        report.append("Rapport des absences pour la semaine");
        report.append("Voici la liste des absences pour la semaine :");

        List<Absence> absences = serviceAbsence.getAbsencesForWeekWithoutJustification();
        System.out.println("Number of absences: " + absences.size());

        report.append("Nombre total d'absences : ").append(absences.size()).append("");
        for (Absence absence : absences) {
            Eleve eleve = getEleveById(absence.getIdEleve());
            if (eleve != null) {
                report.append("")
                        .append(" Étudiant: ").append(eleve.getNom()).append(" ").append(eleve.getPrenom()).append("")
                        .append("  Status: ").append(absence.getStatus()).append("")
                        .append("  Justification: ").append(absence.getJustification())
                        .append("");

                String parentEmail = serviceAbsence.getParentEmailByEleveId(eleve.getEleveID());
                if (parentEmail != null && !parentEmail.isEmpty()) {
                    System.out.println("Parent email for " + eleve.getNom() + " " + eleve.getPrenom() + ": " + parentEmail);
                    sendEmailToParent(parentEmail, report.toString());
                } else {
                    System.out.println("No email found for " + eleve.getNom() + " " + eleve.getPrenom());
                }
            } else {
                System.err.println("No student found for absence ID: " + absence.getIdEleve());
            }
        }


        JOptionPane.showMessageDialog(null, "Rapport envoyé aux parents des étudiants absents !");
    }

    private void sendEmailToParent(String parentEmail, String reportContent) {
        String subject = "Rapport des absences de la semaine";
        EmailUtil.sendEmail(parentEmail, subject, reportContent);
    }

    private void saveAbsencesToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("absences.html", true))) {
            writer.println("<html><body>");
            writer.println("<h1>Liste des absences</h1>");

            // Group absences by date
            AbsenceService absenceService = new AbsenceService();
            List<Absence> allAbsences = absenceService.getAllAbsences(); // Assuming you have a method to get all absences

            Map<LocalDate, List<Absence>> absencesByDate = allAbsences.stream()
                    .collect(Collectors.groupingBy(absence -> absence.getDateAbsence().toLocalDate()));

            for (LocalDate date : absencesByDate.keySet()) {
                writer.println("<h2>" + date + "</h2>");
                writer.println("<table border='1' cellpadding='5' cellspacing='0'>");
                writer.println("<tr><th>ID</th><th>Étudiant</th><th>Status</th><th>Justification</th></tr>");

                for (Absence absence : absencesByDate.get(date)) {
                    Eleve eleve = getEleveById(absence.getIdEleve());
                    if (eleve != null) {
                        writer.println("<tr><td>" + eleve.getEleveID() + "</td><td>" + eleve.getNom() + " " + eleve.getPrenom() + "</td><td>"
                                + absence.getStatus() + "</td><td>" + absence.getJustification() + "</td></tr>");
                    }
                }
                writer.println("</table>");
            }

            writer.println("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openAbsencesFile() {
        try {
            Desktop.getDesktop().open(new File("absences.html"));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture du fichier !");
        }
    }
}