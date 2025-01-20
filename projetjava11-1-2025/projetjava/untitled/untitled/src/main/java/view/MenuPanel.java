package view;

import controller.AppController;

import javax.swing.*;
import java.awt.*;

public class MenuPanel {
    public static JPanel createMenu(AppController appController) {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setLayout(new GridLayout(8, 1));
        sidebar.setPreferredSize(new Dimension(300, 0));

        String[] menuItems = {"Accueil",  "Gestion Enseignant", "Gestion Absences", "Gestion Cours", "Gestion Classe", "Gestion Salle", "Déconnexion"};
        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(52, 73, 94));
            button.setFocusPainted(false);
            button.setFont(new Font("SansSerif", Font.PLAIN, 16));
            button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            switch (item) {
                case "Accueil":
                    button.addActionListener(e -> appController.showVieScolaireView());

                    break;
                case "Gestion Enseignant": // L'action de ce bouton redirige maintenant vers la vue GestionEnseignant
                    button.addActionListener(e -> appController.showGestionEnseignantView());
                  break;
                case "Gestion Absences": // L'action de ce bouton redirige maintenant vers la vue GestionEnseignant
                    button.addActionListener(e -> appController.showGestionCoursView());
                    break;
                case "Gestion Cours": // L'action de ce bouton redirige maintenant vers la vue GestionEnseignant
                    button.addActionListener(e -> appController.showGestionMatiereView());
                    break;
                case "Gestion Classe":
                    button.addActionListener(e -> appController.showGestionClasseView());
                    break;
                case "Gestion Salle":
                    button.addActionListener(e -> appController.showGestionSalleView());
                    break;
                case "Déconnexion":
                    button.addActionListener(e -> {
                        int confirmed = JOptionPane.showConfirmDialog(
                                null,
                                "Voulez-vous vraiment vous déconnecter ?",
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (confirmed == JOptionPane.YES_OPTION) {
                            appController.showLoginView();
                        }
                    });
                    break;
                default:
                    button.addActionListener(e -> JOptionPane.showMessageDialog(
                            null,
                            "Fonctionnalité non encore implémentée : " + item,
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE
                    ));
                    break;
            }

            sidebar.add(button);
        }

        return sidebar;
    }
}