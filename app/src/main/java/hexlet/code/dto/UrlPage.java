package hexlet.code.dto;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import io.javalin.http.Context;

import java.util.List;

public final class UrlPage extends BasePage {
    /**
     * Модель URL для информации на странице.
     */
    private final Url url;
    /**
     * Список проверок по странице.
     */
    private final List<UrlCheck> urlChecks;

    public UrlPage(final Context aCtx, final Url aUrl, final List<UrlCheck> aUrlChecks) {
        super(aCtx, "Информация о URL");
        this.url = aUrl;
        this.urlChecks = aUrlChecks;
    }

    public Url getUrl() {
        return url;
    }

    public List<UrlCheck> getUrlChecks() {
        return urlChecks;
    }
}
