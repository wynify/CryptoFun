import com.sun.net.httpserver.*;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class TranslateServer {
    public static void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/translate", new TranslateHandler());
        server.setExecutor(null);
        System.out.println("Server started at http://localhost:8000");
        server.start();
    }

    static class TranslateHandler implements HttpHandler {
        private final TranslateService translator = new TranslateService();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                String body = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));

                JSONObject json = new JSONObject(body);
                String text = json.getString("q");  // Смотрите, что вы используете ключ "q"
                String source = json.optString("source", "en");  // В случае отсутствия "source" использовать "en"
                String target = json.getString("target");

                String translatedText = translator.translate(text, source, target);

                String jsonResponse = new JSONObject().put("translatedText", translatedText).toString();

                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(jsonResponse.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}
