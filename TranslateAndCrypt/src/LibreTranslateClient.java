import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class LibreTranslateClient {

    // Метод для перевода текста с использованием локального API LibreTranslate
    public static String translate(String text, String source, String target) throws IOException {
        String json = String.format("{\"q\":\"%s\",\"source\":\"%s\",\"target\":\"%s\",\"format\":\"text\"}", text, source, target);

        // Подключение к локальному серверу LibreTranslate
        HttpURLConnection conn = (HttpURLConnection) new URL("http://localhost:5050/translate").openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        // Чтение ответа от сервера
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return in.lines().collect(Collectors.joining());
        }
    }
}

