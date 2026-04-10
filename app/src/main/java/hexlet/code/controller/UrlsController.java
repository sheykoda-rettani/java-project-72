package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.exception.UrlNotFoundException;
import hexlet.code.exception.ValidationException;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static io.javalin.rendering.template.TemplateUtil.model;

public final class UrlsController {
    private UrlsController() { }

    public static void index(final Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.findAll();

        UrlsPage page = new UrlsPage(ctx, urls);
        ctx.render("urls/index.jte", model("urlsPage", page));
    }

    public static void showOne(final Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        Url urlFound = UrlRepository.findById(id).orElseThrow(() ->
                new UrlNotFoundException("Информация по странице с id %d не найдена".formatted(id)));
        List<UrlCheck> urlChecks = UrlCheckRepository.findByUrlId(urlFound.getId());
        UrlPage page = new UrlPage(ctx, urlFound, urlChecks);
        ctx.render("urls/show.jte", model("urlPage", page));
    }

    public static void runCheck(final Context ctx) throws SQLException {
        long urlId = ctx.pathParamAsClass("id", Long.class).get();
        Url urlFound = UrlRepository.findById(urlId).orElseThrow(() ->
                new UrlNotFoundException("Информация по странице с id %d не найдена".formatted(urlId)));
        String name = urlFound.getName();
        HttpResponse<String> response = Unirest.get(name).asString();
        int statusCode = response.getStatus();
        String title, h1, description;
        if (statusCode < HttpStatus.BAD_REQUEST.getCode()) {
            Document doc = Jsoup.parse(response.getBody());
            title = doc.title();
            Elements h1Tags = doc.select("h1");
            Element h1First = h1Tags.isEmpty() ? null : h1Tags.first();
            h1 = h1First == null ? null : h1First.text();
            Elements metaDesc = doc.select("meta[name=description]");
            description = metaDesc.isEmpty() ? null : metaDesc.attr("content");
            UrlCheck check = new UrlCheck(urlId, statusCode, title, h1, description);
            UrlCheckRepository.save(check);
            ctx.sessionAttribute("flashMessage", "Страница успешно проверена");
        } else {
            ctx.sessionAttribute("flashMessage", "Произошла ошибка при проверке");
        }
        //в тестах, если делать редиррект изнутри контроллера не "поглощается" атрибут "flashMessage"
        showOne(ctx);
    }

    public static void addUrl(final Context ctx) throws SQLException {
        String rawUrl = ctx.formParam("url");
        String domain = getDomain(rawUrl);

        Optional<Url> existingUrlOpt = UrlRepository.findByName(domain);
        if (existingUrlOpt.isPresent()) {
            Long existingId = existingUrlOpt.get().getId();
            ctx.sessionAttribute("flashMessage", "Страница уже существует");
            ctx.redirect("/urls/" + existingId);
            return;
        }

        Url newUrl = new Url(domain);
        UrlRepository.save(newUrl);

        Long newId = newUrl.getId();
        ctx.sessionAttribute("flashMessage", "Страница успешно добавлена");
        ctx.redirect("/urls/" + newId);
    }

    @NotNull
    private static String getDomain(final String rawUrl) {
        String domain;
        try {
            if (rawUrl == null || rawUrl.trim().isEmpty()) {
                throw new ValidationException("URL не может быть пустым");
            }
            URI uri = new URI(rawUrl);
            URL parsedUrl = uri.toURL();
            domain = parsedUrl.getProtocol() + "://" + parsedUrl.getHost();
            int port = parsedUrl.getPort();
            if (port != -1 && port != parsedUrl.getDefaultPort()) {
                domain += ":" + port;
            }
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            throw new ValidationException(("Некорректный URL \"%s\". "
                    + "Url должен быть в формате http(s)://(www.)host.domain(/otional)").formatted(rawUrl));
        }
        return domain;
    }
}
