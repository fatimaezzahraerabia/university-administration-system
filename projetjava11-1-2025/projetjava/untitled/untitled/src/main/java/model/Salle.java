package model;

import java.util.ArrayList;
import java.util.List;

public class Salle {

    private int id;
    private int capacite;
    private String type;
    private List<Equipements> equipements;
    private String nomSalle;


    public Salle(int id, int capacite, String type, List<Equipements> equipements, String nomSalle) {
        this.id = id;
        this.capacite = capacite;
        this.type = type;
        this.equipements = equipements;
        this.nomSalle = nomSalle;

    }

    public Salle(String nomSalle) {
        this.nomSalle = nomSalle;

    }

    public Salle() {
        this.id = 0;
        this.capacite = 0;
        this.type = "";
        this.equipements = new ArrayList<>();
        this.nomSalle = "";

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Equipements> getEquipements() {
        return equipements;
    }

    public void setEquipements(List<Equipements> equipements) {
        this.equipements = equipements;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public void setNomSalle(String nomSalle) {
        this.nomSalle = nomSalle;
    }



    public String afficherDetails() {
        return "Salle " + nomSalle + " (" + type + "), Capacité: " + capacite + ", Statut: " ;
    }

    @Override
    public String toString() {
        return nomSalle;
    }
}