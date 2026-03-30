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
                LOGGER.info("Обработка запроса к корневому маршруту");
                config.routes.get("/", ctx -> ctx.result("Hello World"));
            });
        }
        return app;
    }

    public static void main(final String[] args) {
        final int port = 7000;
        Javalin appInstance = getApp();
        appInstance.start(port);
        LOGGER.info("Приложение запущено на порту {}", port);
    }
}
