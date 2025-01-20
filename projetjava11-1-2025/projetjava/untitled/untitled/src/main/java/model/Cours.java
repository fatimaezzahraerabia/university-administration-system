package model;
public class Cours {
    // Attributs
    private int coursID;
    private String jour;
    private String typeCours;

    private String nomCours;
private int IdUtilisateur;
    // Constructeur par défaut


    // Constructeur avec paramètres
    public Cours(int coursID, String typeCours, String nomCours) {
        this.coursID = coursID;

        this.typeCours = typeCours;

        this.nomCours = nomCours;
    }

    public Cours(int coursID, String type, String name, int IdUtilisateur) {
        this.coursID = coursID;
        this.typeCours = type;
        this.nomCours = name;
        this.IdUtilisateur= IdUtilisateur;
    }

    public int getIdUtilisateur() {
        return IdUtilisateur;
    }

    public void setIdUtilisateur(int enseignantId) {
        this.IdUtilisateur = enseignantId;
    }

    // Getters et Setters
    public int getCoursID() {
        return coursID;
    }

    public void setCoursID(int coursID) {
        this.coursID = coursID;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getTypeCours() {
        return typeCours;
    }

    public void setTypeCours(String typeCours) {
        this.typeCours = typeCours;
    }


    public String getNomCours() {
        return nomCours;
    }

    public void setNomCours(String nomCours) {
        this.nomCours = nomCours;
    }

    // Méthode toString

    @Override
    public String toString() {
        return nomCours;
    }
}
