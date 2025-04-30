import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class UI {
    public static void CreateWindow() {
        JFrame window = new JFrame("Translate this");
        window.setSize(600, 600);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label for language selection
        JLabel languageLabel = new JLabel("Choose language to translate:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        window.add(languageLabel, gbc);

        // ComboBox for language selection
        String[] languages = {"English", "XYZ(Fictional)"};
        String[] languageCodes = {"en", "xyz"};
        JComboBox<String> languageSelector = new JComboBox<>(languages);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        window.add(languageSelector, gbc);

        // Input text field
        JTextField inputField = new JTextField(30);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        window.add(inputField, gbc);

        // Translate button
        JButton translateButton = new JButton("Translate!");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        window.add(translateButton, gbc);

        // Label for output
        JLabel translatedLabel = new JLabel("Translated text:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        window.add(translatedLabel, gbc);

        // Output area
        JTextArea outputField = new JTextArea(10, 30);
        outputField.setLineWrap(true);
        outputField.setWrapStyleWord(true);
        outputField.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputField);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        window.add(scrollPane, gbc);

        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputField.getText();
                int langIndex = languageSelector.getSelectedIndex();
                String targetLang = languageCodes[langIndex];

                try {
                    URL url = new URL("http://localhost:8000/translate");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setDoOutput(true);

                    JSONObject jsonRequest = new JSONObject();
                    jsonRequest.put("q", text);  // Используем "q" вместо "text"
                    jsonRequest.put("target", targetLang);

                    OutputStream os = con.getOutputStream();
                    byte[] input = jsonRequest.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8")
                    );
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String translated = jsonResponse.getString("translatedText");
                    outputField.setText(translated);

                } catch (Exception ex) {
                    outputField.setText("Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        window.setVisible(true);
    }
}
