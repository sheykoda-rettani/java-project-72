package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.JavalinTest;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static hexlet.code.repository.BaseRepository.getConnection;
import static org.assertj.core.api.Assertions.assertThat;


final class AppTest {
    /**
     * Наше приложение для тестирования.
     */
    private static Javalin app;

    /**
     * Мок-сервер, для проверки веб-взаимодействия.
     */
    private static MockWebServer mockServer;

    @BeforeAll
    public static void init() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        mockServer.close();
    }

    @BeforeEach
    public void clear() throws SQLException {
        app = App.getApp();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM urls");
        }
    }

    @Test
    public void testHomePageIsUpAndRunning() {
        JavalinTest.test(app, (javalinServer, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.body()).isNotNull();
            String body = response.body().string();
            assertThat(body).contains("Анализатор страниц");
            assertThat(body).contains("Url для проверки");
            assertThat(body).contains("Проверить");
        });
    }

    @Test
    public void testUrlsPageIsUpAndRunning() {
        JavalinTest.test(app, (javalinServer, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.body()).isNotNull();
            String body = response.body().string();
            assertThat(body).contains("Список добавленных URL");
        });
    }

    @Test
    public void testUrlPageIsUpAndRunning() {
        JavalinTest.test(app, (javalinServer, client) -> {
            var url = new Url("https://example.com");
            UrlRepository.save(url);
            var response = client.get("/urls/" + url.getId());
            assertThat(response.body()).isNotNull();
            String body = response.body().string();
            Document doc = Jsoup.parse(body);
            Element tableUrl = doc.selectFirst("table[data-test=url]");
            assertThat(tableUrl).isNotNull();
            Element tableChecks = doc.selectFirst("table[data-test=checks]");
            assertThat(tableChecks).isNotNull();
        });
    }

    @Test
    public void testAddNewUrlSuccessfully() {
        final String urlToAdd = "https://ru.hexlet.io";
        JavalinTest.test(app, (javalinServer, client) -> {
            try (var response = client.post("/urls", "url=" + urlToAdd)) {
                checkUrlPath(response);
                assertThat(response.body()).isNotNull();
                String responseBody = response.body().string();
                assertThat(responseBody).contains(urlToAdd);
            }
            List<Url> urls = UrlRepository.findAll();
            assertThat(urls).isNotEmpty();
            assertThat(urls.size()).isEqualTo(1);
            Url toCheck = urls.getFirst();
            assertThat(toCheck.getName()).isEqualTo(urlToAdd);
        });
    }

    @Test
    public void testDuplicateUrlOnlyResultsInOneRecord() {
        final String urlToAdd = "https://example.com";
        JavalinTest.test(app, (server, client) -> {
            try (var responseFirst = client.post("/urls", "url=" + urlToAdd)) {
                String toRedirect = URI.create(responseFirst.request().url().toString()).getPath();
                assertThat(toRedirect).matches("/urls/\\d+$");
            }
            try (var responseSecond = client.post("/urls", "url=" + urlToAdd)) {
                String toRedirect = URI.create(responseSecond.request().url().toString()).getPath();
                assertThat(toRedirect).matches("/urls/\\d+$");
            }
            List<Url> urls = UrlRepository.findAll();
            assertThat(urls).isNotEmpty();
            assertThat(urls.size()).isEqualTo(1);
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

    @Test
    public void testRunCheckSuccessfullySavesDataToDb() {
        String htmlBody = "<html><head><title>Заголовок 1</title><meta name='description' content='Содержимое'></head>"
                + "<body><h1>Главный H1</h1></body></html>";
        mockServer.enqueue(new MockResponse.Builder().code(HttpStatus.OK.getCode()).body(htmlBody).build());
        var urlToCheck = mockServer.url("/works");
        JavalinTest.test(app, (javalinServer, client) -> {
            client.post("/urls", "url=" + urlToCheck);
            String savedServer = urlToCheck.toString().substring(0, urlToCheck.toString().lastIndexOf('/'));
            Optional<Long> optionalId = UrlRepository.getIdByName(savedServer);
            assertThat(optionalId).isPresent();
            Url url = UrlRepository.findById(optionalId.get()).orElse(null);
            assertThat(url).isNotNull();
            try (var response = client.post("/urls/%d/checks".formatted(url.getId()))) {
                checkUrlPath(response);
            }
            List<UrlCheck> urlChecks = UrlCheckRepository.findByUrlId(url.getId());
            assertThat(urlChecks).isNotEmpty();
            assertThat(urlChecks.size()).isEqualTo(1);
            UrlCheck urlCheck = urlChecks.getFirst();
            assertThat(urlCheck.getStatusCode()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(urlCheck.getTitle()).isEqualTo("Заголовок 1");
            assertThat(urlCheck.getH1()).isEqualTo("Главный H1");
            assertThat(urlCheck.getDescription()).isEqualTo("Содержимое");
        });
    }

    @Test
    public void testFailedCheckAfterSuccessProducesCorrectMessageAndNoExtraRecords() {
        String htmlBody = "<html><head><title>Заголовок</title><meta name='description' content='Содержимое'></head>"
                + "<body><h1>Главный H1</h1></body></html>";
        mockServer.enqueue(new MockResponse.Builder().code(HttpStatus.OK.getCode()).body(htmlBody).build());
        mockServer.enqueue(new MockResponse.Builder().code(HttpStatus.BAD_REQUEST.getCode()).build());
        var urlToCheck = mockServer.url("/works");
        JavalinTest.test(app, (javalinServer, client) -> {
            client.post("/urls", "url=" + urlToCheck);
            String savedServer = urlToCheck.toString().substring(0, urlToCheck.toString().lastIndexOf('/'));
            Optional<Long> optionalId = UrlRepository.getIdByName(savedServer);
            assertThat(optionalId).isPresent();
            Url url = UrlRepository.findById(optionalId.get()).orElse(null);
            assertThat(url).isNotNull();
            try (var respSuccess = client.post("/urls/%d/checks".formatted(url.getId()))) {
                checkUrlPath(respSuccess);
            }
            try (var respError = client.post("/urls/%d/checks".formatted(url.getId()))) {
                checkUrlPath(respError);
            }
            List<UrlCheck> urlChecks = UrlCheckRepository.findByUrlId(url.getId());
            assertThat(urlChecks).isNotEmpty();
            assertThat(urlChecks.size()).isEqualTo(1);
        });
    }

    private static void checkUrlPath(final Response response) {
        assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
        String toRedirect = URI.create(response.request().url().toString()).getPath();
        assertThat(toRedirect).matches("/urls/\\d+$");
    }
}
