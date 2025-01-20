package model;

public class Enseignant extends Utilisateur{
    private String prenom;
    private String gmail;
    private String adresse;
    private String genre;
    private String telephone;
    private int id;

    public Enseignant(Integer id, String username, String password, Role role, String prenom, String gmail, String adresse, String genre, String telephone) {
        super(username, password, role);
        this.id = id;
        this.prenom = prenom;
        this.gmail = gmail;
        this.adresse = adresse;
        this.genre = genre;
        this.telephone = telephone;
    }



    public String getUsername() {
        return super.getNom(); // You can still call the parent method if necessary
    }

    public int getId() {
        return id; // You can still call the parent method if necessary
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public void setid(int id) {
        this.id = id;
    }



    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}


