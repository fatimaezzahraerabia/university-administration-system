package model;

import java.util.ArrayList;
import java.util.List;

public class Classe {
    private int id;
    private String niveauScolaire; // Niveau scolaire (ex: 6ème, 5ème, etc.)
    private String anneeScolaire;  // Année scolaire (ex: 2023-2024)
    private String nomClasse;      // Nom de la classe (ex: 6A, 5B, etc.)
    private List<Eleve> eleves;    // Liste des élèves de la classe
    private List<Cours> cours;     // Liste des cours suivis par la classe

    // Constructeur
    public Classe(int id, String niveauScolaire, String anneeScolaire, String nomClasse) {
        this.id = id;
        this.niveauScolaire = niveauScolaire;
        this.anneeScolaire = anneeScolaire;
        this.nomClasse = nomClasse;
        this.eleves = new ArrayList<>();
        this.cours = new ArrayList<>();
    }
    public Classe(String niveauScolaire, String anneeScolaire, String nomClasse) {
        this.niveauScolaire = niveauScolaire;
        this.anneeScolaire = anneeScolaire;
        this.nomClasse = nomClasse;
        this.eleves = new ArrayList<>();
        this.cours = new ArrayList<>();
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNiveauScolaire() {
        return niveauScolaire;
    }

    public void setNiveauScolaire(String niveauScolaire) {
        this.niveauScolaire = niveauScolaire;
    }

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public void setNomClasse(String nomClasse) {
        this.nomClasse = nomClasse;
    }

    public List<Eleve> getEleves() {
        return eleves;
    }

    public void setEleves(List<Eleve> eleves) {
        this.eleves = eleves;
    }

    public List<Cours> getCours() {
        return cours;
    }

    public void setCours(List<Cours> cours) {
        this.cours = cours;
    }

    // Méthode pour ajouter un élève à la classe
    public void ajouterEleve(Eleve eleve) {
        this.eleves.add(eleve);
    }

    // Méthode pour ajouter un cours à la classe
    public void ajouterCours(Cours cours) {
        this.cours.add(cours);
    }
    @Override
    public String toString() {
        return nomClasse;
    }
}
