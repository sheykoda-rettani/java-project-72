package hexlet.code.controller;

import hexlet.code.dto.HomePage;
import io.javalin.http.Context;

import static io.javalin.rendering.template.TemplateUtil.model;

public final class HomeController {
    private HomeController() { }

    public static void index(final Context ctx) {
        HomePage page = new HomePage(ctx);
        ctx.render("home.jte", model("homePage", page));
    }
}
