package hexlet.code;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App {
    private App() { }

    /**
     * Логирование.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    /**
     * Само Javalin приложение.
     */
    private static Javalin app;

    public static Javalin getApp() {
        if (app == null) {
            app = Javalin.create(config -> {
                config.routes.get("/", ctx -> {
                    LOGGER.info("Обработка запроса к корневому маршруту");
                    ctx.result("Hello World");
                });
            });
        }
        return app;
    }

    public static void main(final String[] args) {
        final int defaultPort = 7000;
        String portEnv = System.getenv("PORT");
        int port = (portEnv != null && !portEnv.isEmpty()) ? Integer.parseInt(portEnv) : defaultPort;
        Javalin appInstance = getApp();
        appInstance.start(defaultPort);
        LOGGER.info("Приложение запущено на порту {}", port);
    }
}
