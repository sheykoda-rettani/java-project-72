package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
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
        Optional<Url> urlOpt = UrlRepository.findById(id);
        if (urlOpt.isEmpty()) {
            throw new NoSuchElementException("Не найден URL по id %d".formatted(id));
        }
        UrlPage page = new UrlPage(ctx, urlOpt.get());
        ctx.render("urls/show.jte", model("urlPage", page));
    }

    public static void addUrl(final Context ctx) throws SQLException {
        String rawUrl = ctx.formParam("url");
        String domain;
        try {
            if (rawUrl == null || rawUrl.trim().isEmpty()) {
                throw new URISyntaxException("", "URL не может быть пустым");
            }
            URI uri = new URI(rawUrl);
            URL parsedUrl = uri.toURL();
            domain = parsedUrl.getProtocol() + "://" + parsedUrl.getHost();
            int port = parsedUrl.getPort();
            if (port != -1 && port != parsedUrl.getDefaultPort()) {
                domain += ":" + port;
            }
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            ctx.status(422);
            ctx.sessionAttribute("flashMessage", ("Некорректный URL \"%s\". " +
                    "Url должен быть в формате http(s)://(www.)host.domain(/otional)").formatted(rawUrl));
            ctx.redirect("/");
            return;
        }

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
}
