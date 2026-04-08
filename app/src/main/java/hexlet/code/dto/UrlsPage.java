package hexlet.code.dto;

import hexlet.code.model.Url;
import io.javalin.http.Context;

import java.util.List;

public final class UrlsPage extends BasePage {
    /**
     * Список URL-ов.
     */
    private List<Url> urls;

    public UrlsPage(final Context aCtx, final List<Url> aUrls) {
        super(aCtx, "Список URL");
        this.urls = aUrls;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(final List<Url> urls) {
        this.urls = urls;
    }
}
