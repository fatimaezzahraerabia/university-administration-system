package controller;

import model.Utilisateur;
import service.AuthService;
import view.LoginView;

import javax.swing.*;


public class AuthController {
    private LoginView loginView;
    private AuthService authService;
    private AppController appController;

    public AuthController(LoginView loginView, AuthService authService, AppController appController) {
        this.loginView = loginView;
        this.authService = authService;
        this.appController = appController;

        // Attacher l'écouteur au bouton de connexion
        loginView.getLoginButton().addActionListener(e -> {
            System.out.println("Button clicked!");  // Debug print
            handleLogin();
        });

    }
    public void handleLogin() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            loginView.showError("Veuillez remplir tous les champs.");
            return;
        }


            Utilisateur utilisateur = authService.authenticate(username, password);
            SwingUtilities.invokeLater(() -> {
                if (utilisateur != null) {
                    loginView.clearMessage();
                    appController.redirectToRoleBasedView(utilisateur);
                } else {
                    loginView.showError("Nom d'utilisateur ou mot de passe incorrect.");
                }
            });

    }   }
