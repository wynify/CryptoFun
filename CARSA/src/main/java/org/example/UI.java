package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI {

    public static void CreateWindow() {
        LoginScreen.CreateLoginScreen();
    }

    private static class LoginScreen {
        private static JFrame login_screen = new JFrame("Login to app");
        private static JLabel user_name_lb = new JLabel("Username: ");
        private static JLabel user_pass_lb = new JLabel("Password: ");
        private static JTextField user_name_tf = new JTextField();
        private static JPasswordField user_pass_tf = new JPasswordField();

        private static JButton submit_login_btn = new JButton("Submit");

        public static void CreateLoginScreen() {
            user_name_tf.setPreferredSize(new Dimension(230, 40));  // Размер 230x40 для имени пользователя
            user_pass_tf.setPreferredSize(new Dimension(230, 40));  // Размер 230x40 для пароля

            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

            submit_login_btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = user_name_tf.getText();
                    char[] password = user_pass_tf.getPassword();

                    if (!username.isEmpty() && password.length > 0) {
                       JOptionPane.showConfirmDialog(
                               login_screen,
                               "Login success!",
                               "Login in...",
                               JOptionPane.DEFAULT_OPTION
                       );

                       login_screen.dispose();

                       ClientScreen.open_client_side();
                    } else {
                        JOptionPane.showMessageDialog(
                                login_screen,
                                "Enter Credentials",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            });

            panel.add(user_name_lb);
            panel.add(user_name_tf);
            panel.add(user_pass_lb);
            panel.add(user_pass_tf);
            panel.add(new JLabel());
            panel.add(submit_login_btn);

            login_screen.add(panel);

            login_screen.setSize(320, 320);
            login_screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            login_screen.setLocationRelativeTo(null);
            login_screen.setVisible(true);
        }
    }

    private static class ClientScreen {
        private static void open_client_side(){
            String username = LoginScreen.user_name_tf.getText();
            JFrame client_screen = new JFrame("Client " + username);

            JPanel client_panel = new JPanel();
            client_panel.setLayout(new BorderLayout());

            JLabel ClientWelcomeLB = new JLabel("Welcome " + username + " to your client side app", JLabel.CENTER);
            ClientWelcomeLB.setFont(new Font("Arial", Font.PLAIN, 24));

            client_panel.add(ClientWelcomeLB, BorderLayout.CENTER);

            client_screen.add(client_panel);

            client_screen.setSize(600, 600);
            client_screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            client_screen.setLocationRelativeTo(null);
            client_screen.setVisible(true);
        }
    }
}
