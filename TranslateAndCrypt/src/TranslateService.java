import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TranslateService {
    private final Map<String, Map<String, String>> dictionaries = new HashMap<>();

    public TranslateService() {
        loadDictionary("xyz", "dictionaries/en-to-xyz.json");
        loadDictionary("ru", "dictionaries/en-to-ru.json");
    }

    private void loadDictionary(String langCode, String filePath) {
        try {
            JSONObject json = new JSONObject(new String(java.nio.file.Files.readAllBytes(new File(filePath).toPath())));
            Map<String, String> dict = new HashMap<>();
            for(String key : json.keySet()) {
                dict.put(key.toLowerCase(), json.getString(key));
            }
            dictionaries.put(langCode, dict);
            System.out.println("Loaded dictionary: " + langCode);
        } catch (IOException e) {
            System.err.println("Failed to load dictionary for " + langCode + ": " + e.getMessage());
        }
    }

    public String translate(String text, String targetLang, String target) {
        Map<String, String> dict = dictionaries.get(targetLang);
        if (dict == null) return "Unsupported language " + targetLang;

        String[] words = text.toLowerCase().split("\\s+");
        StringBuilder translated = new StringBuilder();

        for (String word : words) {
            String translatedWord = dict.getOrDefault(word, "[" + word + "]");
            translated.append(translatedWord).append(" ");
        }

        return translated.toString().trim();
    }
}

