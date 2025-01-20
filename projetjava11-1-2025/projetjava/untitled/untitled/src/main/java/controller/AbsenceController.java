package controller;

import service.AbsenceService;
import view.AbsenceView;

import java.util.List;

public class AbsenceController {
    private AbsenceView absenceView;
    private AbsenceService absenceService;

    public AbsenceController(AbsenceView absenceView, AbsenceService absenceService) {
        this.absenceView = absenceView;
        this.absenceService = absenceService;
    }

    public void loadAbsencesForTeacher(int teacherId) {
        List<Object[]> absences = absenceService.getAbsencesByTeacherId(teacherId);
        absenceView.updateAbsenceTable(absences);
    }
}