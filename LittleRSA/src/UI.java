import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI {
    public static void CreateWindow() {
        JFrame window = new JFrame("RSA Encryptor");
        window.setSize(600, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ENCRYPTION
        JLabel inputLabel = new JLabel("Enter text down bellow:: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        window.add(inputLabel, gbc);

        JTextField inputField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 0;
        window.add(inputField, gbc);

        JButton encryptButton = new JButton("Encrypt!");
        gbc.gridx = 1;
        gbc.gridy = 1;
        window.add(encryptButton, gbc);

        JLabel encryptText = new JLabel("Encrypted text:: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        window.add(encryptText, gbc);

        JTextArea outputField = new JTextArea(10, 30);
        outputField.setLineWrap(true);
        outputField.setWrapStyleWord(true);
        outputField.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputField);
        gbc.gridx = 1;
        gbc.gridy = 2;
        window.add(scrollPane, gbc);

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = inputField.getText();
                if(inputText.isEmpty()){
                    JOptionPane.showMessageDialog(window, "Enter TEXT!", "= E R R R R R =", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                RSAEncryptor rsa = new RSAEncryptor(61, 53, 17);
                String asciiString = RSAEncryptor.toAsciiString(inputText);
                String encrypted = rsa.encrypt(asciiString);
                outputField.setText(encrypted);
            }
        });

        // DE-ENCRYPTION
        JButton DecryptButton = new JButton("Decrypt");
        gbc.gridx = 1;
        gbc.gridy = 5;
        window.add(DecryptButton, gbc);

        DecryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str_to_decrypt = outputField.getText().trim();
                RSAEncryptor rsa = new RSAEncryptor(61, 53, 17);
                String decryptedAscii = rsa.decrypt(str_to_decrypt);
                String decryptedText = RSAEncryptor.asciiToString(decryptedAscii);
                outputField.setText(decryptedText);
            }
        });

        window.setVisible(true);
    }
}
