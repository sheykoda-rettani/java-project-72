package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static hexlet.code.repository.BaseRepository.getConnection;
import static org.assertj.core.api.Assertions.assertThat;


public final class AppTest {
    /**
     * Наше приложение для тестирования.
     */
    private static Javalin app;

    @BeforeEach
    public void clear() throws SQLException {
        System.setProperty("test.mode", "true");
        app = App.getApp();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM urls");
        }
    }

    @Test
    public void testHomePageIsUpAndRunning() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.body()).isNotNull();
            assertThat(response.body().string()).contains("Url для проверки");
        });
    }

    @Test
    public void testUrlsPageIsUpAndRunning() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.body()).isNotNull();
            assertThat(response.body().string()).contains("Список добавленных URL");
        });
    }

    @Test
    public void testUrlPageIsUpAndRunning() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("https://example.com");
            UrlRepository.save(url);
            var response = client.get("/urls/" + url.getId());
            assertThat(response.body()).isNotNull();
            assertThat(response.body().string()).contains("https://example.com");
        });
    }

    @Test
    public void testNotExistingUrlPageStatusNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999");
            assertThat(response.code()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
        });
    }

    @Test
    public void testAddNewUrlSuccessfully() {
        final String urlToAdd = "https://ru.hexlet.io";
        JavalinTest.test(app, (server, client) -> {
            try (var response = client.post("/urls", "url=" + urlToAdd)) {
                assertThat(response.body()).isNotNull();
                String responseBody = response.body().string();
                assertThat(responseBody).contains(urlToAdd);
            }
        });
    }

    @Test
    public void testDuplicateUrlOnlyResultsInOneRecord() {
        final String urlToAdd = "https://example.com";
        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=" + urlToAdd);
            client.post("/urls", "url=" + urlToAdd);
            List<Url> urls = UrlRepository.findAll();
            assertThat(urls).isNotEmpty();
        });
    }

    @Test
    public void testWrongUrlProducesError() {
        final String wrongUrl = "zdfsdfsdf";
        JavalinTest.test(app, (server, client) -> {
            try (var response = client.post("/urls", "url=" + wrongUrl)) {
                assertThat(response.code()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT.getCode());
            }
        });
    }
}
