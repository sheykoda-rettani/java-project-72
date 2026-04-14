package hexlet.code;

import gg.jte.ContentType;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.HomeController;
import hexlet.code.controller.UrlsController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import gg.jte.TemplateEngine;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import static hexlet.code.repository.BaseRepository.getConnection;

@Slf4j
public final class App {
    private App() {
    }

    public static Javalin getApp() throws SQLException {
        initDb();

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.before(ctx -> {
            ctx.contentType("text/html; charset=utf-8");
        });

        app.get("/", HomeController::index);

        app.get("/urls", UrlsController::index);
        app.post("/urls", UrlsController::addUrl);
        app.get("/urls/{id}", UrlsController::showOne);
        app.post("/urls/{id}/checks", UrlsController::runCheck);

        return app;
    }

    private static void initDb() throws SQLException {
        String sql;
        try {
            sql = readResourceFile("schema.sql");
        } catch (IOException e) {
            log.error("Ощибка во время чтения файла схемы", e);
            return;
        }

        log.info("Инициализация БД:");
        log.info("Исполнение запроса {}", sql);
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
    }

    private static String readResourceFile(final String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static void main(final String[] args) throws SQLException {
        final int defaultPort = 7000;

        String portEnv = System.getenv("PORT");
        int port = defaultPort;
        try {
            if (portEnv != null && !portEnv.isEmpty()) {
                port = Integer.parseInt(portEnv);
                log.info("Используется порт из переменной окружения PORT: {}", port);
            } else {
                log.info("Переменная окружения PORT не задана. Используется порт по умолчанию: {}", port);
            }
        } catch (NumberFormatException e) {
            log.warn("Переменная окружения PORT содержит неверное значение '{}'. Используется порт по умолчанию: {}",
                    portEnv, port);
        }
        Javalin appInstance = getApp();
        appInstance.start(port);
        log.info("Приложение запущено на порту {}", port);
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }
}
