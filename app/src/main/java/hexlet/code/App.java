package hexlet.code;

import gg.jte.ContentType;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.HomeController;
import hexlet.code.controller.UrlsController;
import hexlet.code.exception.UrlNotFoundException;
import hexlet.code.exception.ValidationException;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.rendering.template.JavalinJte;
import gg.jte.TemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import static hexlet.code.repository.BaseRepository.getConnection;

public final class App {
    private App() {
    }

    /**
     * Логирование.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static Javalin getApp() throws SQLException {
        initDb();

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.before(ctx -> {
            ctx.contentType("text/html; charset=utf-8");
        });

        //верхнеуровневый хэндлинг exception из контроллера. Выставление 422 тут
        app.exception(ValidationException.class, (e, ctx) -> {
            ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
            ctx.sessionAttribute("flashMessage", e.getMessage());
            HomeController.index(ctx);
        });

        app.exception(UrlNotFoundException.class, (e, ctx) -> {
            ctx.sessionAttribute("flashMessage", "Информация по странице не найдена");
            ctx.status(HttpStatus.NOT_FOUND);
            try {
                UrlsController.index(ctx);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
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
            LOGGER.error("Ощибка во время чтения файла схемы", e);
            return;
        }

        LOGGER.info("Инициализация БД:");
        LOGGER.info("Исполнение запроса {}", sql);
        try (Connection conn = getConnection()) {
            Statement statement = conn.createStatement();
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
                LOGGER.info("Используется порт из переменной окружения PORT: {}", port);
            } else {
                LOGGER.info("Переменная окружения PORT не задана. Используется порт по умолчанию: {}", port);
            }
        } catch (NumberFormatException e) {
            LOGGER.warn("Переменная окружения PORT содержит неверное значение '{}'. Используется порт по умолчанию: {}",
                    portEnv, port);
        }
        Javalin appInstance = getApp();
        appInstance.start(port);
        LOGGER.info("Приложение запущено на порту {}", port);
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }
}
