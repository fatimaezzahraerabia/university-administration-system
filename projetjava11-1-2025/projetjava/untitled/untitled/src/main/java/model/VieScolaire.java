package model;

public class VieScolaire extends Utilisateur{
    private int id;


    public VieScolaire(String username, String password, Role role, int id) {
        super(username, password, role);
        this.id = id;

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }


}
