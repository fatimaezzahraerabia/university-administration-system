package controller;

import service.EnseignantService;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class enseignantController {
    private EnseignantService enseignantService;

    public enseignantController() {
        this.enseignantService = new EnseignantService();
    }
    public List<Object[]> getScheduleData(int userId) {
        return enseignantService.getTeacherSchedule(userId);
    }
    public void populateScheduleTable(DefaultTableModel tableModel, int userId) {
        List<Object[]> scheduleData = enseignantService.getTeacherSchedule(userId);

        // Clear existing data
        tableModel.setRowCount(0);

        // Add new data to the table model
        for (Object[] row : scheduleData) {
            tableModel.addRow(row);
        }

    }
}