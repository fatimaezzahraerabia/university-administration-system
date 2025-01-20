package view;

import controller.AppController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class LoginView extends JPanel {

    // Reference to the main controller
    private final AppController appController;

    // UI components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JButton loginButton;

    public LoginView(AppController appController) {
        this.appController = appController;
        initializeComponents();
        configureLayout(); // Arrange the components within the panel
    }

    private void initializeComponents() {
        // Input fields
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(0, 120, 215));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        // Message label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        messageLabel.setForeground(Color.RED);
    }



            private void configureLayout() {
                // Main Panel with Blurry Background
                JPanel mainPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);

                        // Load the background image
                        ImageIcon background = new ImageIcon(getClass().getResource("/icons/images1.jpg")); // Path to your image
                        Image originalImage = background.getImage();

                        // Draw the image
                        g.drawImage(originalImage, 0, 0, getWidth(), getHeight(), this);

                        // Overlay a translucent black layer
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setColor(new Color(0, 0, 0, 150)); // Black color with 150/255 transparency
                        g2d.fillRect(0, 0, getWidth(), getHeight());
                    }
                };
                mainPanel.setLayout(new GridBagLayout());

                // Rounded Panel for Login Form
                JPanel loginPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2d.setColor(Color.WHITE);
                        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    }
                };
                loginPanel.setOpaque(false);
                loginPanel.setPreferredSize(new Dimension(700, 500)); // Adjust panel size
                loginPanel.setLayout(null);

                // Add components to the login panel
                JLabel titleLabel = new JLabel("Login");
                titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
                titleLabel.setBounds(130, 20, 200, 40);
                loginPanel.add(titleLabel);

                JLabel usernameLabel = new JLabel("Username");
                usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                usernameLabel.setBounds(50, 100, 100, 25);
                loginPanel.add(usernameLabel);

                usernameField.setBounds(50, 130, 250, 40);
                loginPanel.add(usernameField);

                JLabel passwordLabel = new JLabel("Password");
                passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                passwordLabel.setBounds(50, 180, 100, 25);
                loginPanel.add(passwordLabel);

                passwordField.setBounds(50, 210, 250, 40);
                loginPanel.add(passwordField);

                loginButton.setBounds(50, 270, 250, 40);
                loginPanel.add(loginButton);

                // Message Label
                messageLabel.setBounds(50, 320, 250, 25);
                loginPanel.add(messageLabel);

                // Image inside the login panel
                JLabel imageLabel = new JLabel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        ImageIcon cardImage = new ImageIcon(getClass().getResource("/icons/images1.jpg")); // Path to your image
                        g.drawImage(cardImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                    }
                };
                imageLabel.setBounds(350, 0, 400, 500); // Adjusted position and size
                imageLabel.setOpaque(false);
                loginPanel.add(imageLabel);

                // Add login panel to the center of the main panel
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(10, 10, 10, 10);
                mainPanel.add(loginPanel, gbc);

                // Add main panel to this panel
                setLayout(new BorderLayout());
                add(mainPanel, BorderLayout.CENTER);
            }

    // Action Listener for login button
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    // Getter for the login button
    public JButton getLoginButton() {
        return loginButton;
    }
    // Method to clear the message
    public void clearMessage() {
        messageLabel.setText("");
    }


    public void showMessage(String message) {
        messageLabel.setText(message);
    }
    // Method to show an error dialog
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    // Method to create a blurred image
    private BufferedImage getBlurredImage(Image originalImage) {
        BufferedImage image = new BufferedImage(
                originalImage.getWidth(null), originalImage.getHeight(null), BufferedImage.TYPE_INT_ARGB
        );

        Graphics g = image.getGraphics();
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();

        float[] blurKernel = {
                1 / 16f, 2 / 16f, 1 / 16f,
                2 / 16f, 4 / 16f, 2 / 16f,
                1 / 16f, 2 / 16f, 1 / 16f
        };

        Kernel kernel = new Kernel(3, 3, blurKernel);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }


}



