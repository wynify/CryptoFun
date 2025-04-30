import java.net.MalformedURLException;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        // Запуск сервера в отдельном потоке
        new Thread(() -> {
            try {
                TranslateServer.startServer(); // 👈 Вынесем запуск сервера в отдельный метод
            } catch (Exception e) {
                System.err.println("Failed to start server: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        // Запуск GUI
        UI.CreateWindow();
    }
}
