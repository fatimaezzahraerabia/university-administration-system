package view;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtil {

    public static void sendEmail(String to, String subject, String content) {
        // Informations du serveur SMTP
        String host = "sandbox.smtp.mailtrap.io"; // Adresse du serveur SMTP
        String port = "2525"; // Port du serveur SMTP
        String username = "77848866d8cb36"; // Nom d'utilisateur pour l'authentification
        String password = "dcd4fb2dd881d0"; // Mot de passe pour l'authentification

        // Configuration des propriétés SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host); // Adresse du serveur SMTP
        properties.put("mail.smtp.port", port); // Port du serveur SMTP
        properties.put("mail.smtp.auth", "true"); // Activer l'authentification SMTP
        properties.put("mail.smtp.starttls.enable", "true"); // Activer STARTTLS pour sécuriser la connexion

        // Création d'une session avec un authentificateur pour gérer l'authentification
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password); // Fournir les informations d'authentification
            }
        });

        try {
            // Création d'un message e-mail
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // Définir l'expéditeur du message
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // Définir le(s) destinataire(s)
            message.setSubject(subject); // Définir le sujet de l'e-mail
            message.setText(content); // Définir le contenu textuel de l'e-mail

            // Envoi du message
            Transport.send(message);
            System.out.println("Email sent successfully!"); // Confirmation en cas de succès

        } catch (MessagingException e) {
            e.printStackTrace(); // Afficher les détails de l'erreur en cas d'échec
            throw new RuntimeException(e); // Relancer l'exception pour le traitement ultérieur
        }
    }

}