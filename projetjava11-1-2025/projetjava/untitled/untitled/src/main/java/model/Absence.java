// Absence.java
package model;

import java.sql.Date;

public class Absence {
    private int id;
    private Date dateAbsence;
    private String justification;
    private int idCours;
    private int idVieScolaire;
    private String status;
    private int idEleve;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateAbsence() {
        return dateAbsence;
    }

    public void setDateAbsence(Date dateAbsence) {
        this.dateAbsence = dateAbsence;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public int getIdCours() {
        return idCours;
    }

    public void setIdCours(int idCours) {
        this.idCours = idCours;
    }

    public int getIdVieScolaire() {
        return idVieScolaire;
    }

    public void setIdVieScolaire(int idVieScolaire) {
        this.idVieScolaire = idVieScolaire;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdEleve() {
        return idEleve;
    }

    public void setIdEleve(int idEleve) {
        this.idEleve = idEleve;
    }
}