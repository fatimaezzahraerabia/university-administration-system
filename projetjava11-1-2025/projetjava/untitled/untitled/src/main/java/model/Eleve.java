package model;

import java.util.Date;

public class Eleve extends Utilisateur {
    private int eleveID;          // Identifiant unique de l'élève
    private String prenom;        // Prénom de l'élève
    private Classe classe;        // Classe de l'élève
      private Date datedenaissance ;
     private String genre ;
    private String   adresse ;

    // Constructeur

    public Eleve(int eleveID,  String prenom, String username, String password, Role role,  String genre,String   adresse ,Date datedenaissance) {
        super( username, password,  role);
        this.eleveID = eleveID;
        this.prenom = prenom;


        this.datedenaissance = datedenaissance ;
        this.genre= genre;
        this.adresse= adresse;

    }
    public Eleve(  String prenom, String username, String password, Role role,  String genre,String   adresse, Date dateNaissance) {

        super(username, password,  role);

        this.prenom = prenom;
        this.adresse = adresse;
        this.genre = genre;
        this.datedenaissance = dateNaissance;
    }


    // Getters et setters
    public int getEleveID() {
        return eleveID;
    }

    public void setEleveID(int eleveID) {
        this.eleveID = eleveID;
    }

    public Date getDatedenaissance() {
        return datedenaissance;
    }

    public void setDatedenaissance(Date datedenaissance) {
        datedenaissance = datedenaissance;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        genre = genre;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        adresse = adresse;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }


}
