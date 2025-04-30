import java.util.HashMap;
import java.util.Map;

public class TranslateService {
    private final Map<String, Map<String, String>> dictionaries = new HashMap<>();

    public TranslateService() {
        Map<String, String> enToRu = new HashMap<>();
        enToRu.put("hello", "привет");
        enToRu.put("world", "мир");
        enToRu.put("how", "как");
        enToRu.put("are", "есть");
        enToRu.put("you", "ты");

        dictionaries.put("ru", enToRu); // <-- ВАЖНО! Эта строка добавляет словарь "ru"
    }

    public String translate(String text, String targetLang) {
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

