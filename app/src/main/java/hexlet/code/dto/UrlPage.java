package hexlet.code.dto;

import hexlet.code.model.Url;
import io.javalin.http.Context;

public final class UrlPage extends BasePage {
    /**
     * Модель URL для информации на странице.
     */
    private Url url;

    public UrlPage(final Context aCtx, final Url aUrl) {
        super(aCtx, "Информация о URL");
        this.url = aUrl;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(final Url url) {
        this.url = url;
    }
}
