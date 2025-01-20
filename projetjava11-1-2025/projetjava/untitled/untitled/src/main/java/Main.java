import com.formdev.flatlaf.FlatLightLaf;
import controller.AppController;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup(); // Set FlatLaf theme globally
        SwingUtilities.invokeLater(() -> {
            new AppController(); // Start your application
        });
    }
}



