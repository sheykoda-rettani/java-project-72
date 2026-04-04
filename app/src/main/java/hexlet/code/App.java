package hexlet.code;

import gg.jte.ContentType;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.db.DatabaseFactory;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.JdbiException;
import gg.jte.TemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class App {
    private App() {
    }

    /**
     * Логирование.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static Javalin getApp() {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.before(ctx -> {
            ctx.contentType("text/html; charset=utf-8");
        });

        app.get("/", ctx -> {
            ctx.render("home.jte");
        });

        return app;
    }

    public static void main(final String[] args) {
        final int defaultPort = 7000;

        try {
            DataSource dataSource = DatabaseFactory.getDataSource();
            Jdbi jdbi = DatabaseFactory.getJdbi(dataSource);

            try (InputStream is = App.class.getClassLoader().getResourceAsStream("schema.sql")) {
                if (is == null) {
                    throw new IOException("Файл схемы schema.sql не найден в classpath");
                }
                String schema = new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A").next();
                jdbi.withHandle(handle -> handle.execute(schema));
                LOGGER.info("База данных успешно инициализирована.");
            } catch (IOException e) {
                LOGGER.error("Ошибка чтения файла схемы БД: {}", e.getMessage());
                System.err.println("Не удалось запустить приложение. Отсутствует файл схемы.");
                return;
            }
        } catch (JdbiException e) {
            LOGGER.error("Ошибка инициализации БД: {}", e.getMessage());
            System.err.println("Не удалось запустить приложение из-за ошибки базы данных.");
            System.err.println(e.getMessage());
            return;
        }

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
