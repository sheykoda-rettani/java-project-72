package hexlet.code.dto;

import io.javalin.http.Context;

public final class HomePage extends BasePage {
    public HomePage(final Context aCtx) {
        super(aCtx, "Анализатор страниц");
    }
}
