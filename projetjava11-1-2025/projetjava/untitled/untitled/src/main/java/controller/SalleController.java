package controller;

import model.AffectationSalle;
import model.Classe;
import model.Cours;
import model.Salle;
import service.AffectationSalleCours;
import service.SalleService;

import java.util.List;

public class SalleController {
    private SalleService salleService;
    private AffectationSalleCours affectationSalleCours;

    public SalleController() {
        this.salleService = new SalleService();
        this.affectationSalleCours = new AffectationSalleCours();
    }

    public boolean affecterCours(Cours cours, Classe classe, String jour, String heureDebut, String heureFin) {
        return affectationSalleCours.affecterCours(cours, classe, jour, heureDebut, heureFin);
    }

    public boolean saveAffectationToDatabase(AffectationSalle affectation) {
        return affectationSalleCours.saveAffectationToDatabase(affectation);
    }

    public List<AffectationSalle>  getAllAffectations() {

            affectationSalleCours.loadAffectationsFromDatabase(); // Ensure data is reloaded from the database
            return affectationSalleCours.getAllAffectations();


    }

    public void addSalle(Salle salle) {
        salleService.addSalle(salle);
    }

    public void updateSalle(Salle salle) {
        salleService.updateSalle(salle);
    }

    public void deleteSalle(int id) {
        salleService.deleteSalle(id);
    }
}