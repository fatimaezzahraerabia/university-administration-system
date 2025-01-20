package service;

import model.DatabaseConnection;
import model.Utilisateur;
import model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    public Utilisateur authenticate(String username, String password) {
        String sql = "SELECT * FROM utilisateur WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("username");
                String role = rs.getString("role");
                return new Utilisateur(username, password, Role.valueOf(role.toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isAuthorized(Utilisateur utilisateur, Role role) {
        return utilisateur.getRole() == role;
    }
}
