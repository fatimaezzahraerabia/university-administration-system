package model;


public enum Role {
    ENSEIGNANT,
    VIESCOLAIRE,
    ELEVE;
    public static Role fromString(String roleString) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(roleString)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rôle inconnu : " + roleString);
    }
}
