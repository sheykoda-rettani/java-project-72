package hexlet.code.dto;

import io.javalin.http.Context;

public class BasePage {
    /**
     * flash сообщение.
     */
    private String flashMessage;

    /**
     * Заголовок страницы.
     */
    private final String title;

    public BasePage(final Context aCtx, final String aTitle) {
        this.flashMessage = aCtx.consumeSessionAttribute("flashMessage");
        this.title = aTitle;
    }

    /**
     * Получение flash сообщения.
     * @return текст сообщения
     */
    public String getFlashMessage() {
        return flashMessage;
    }

    /**
     * Установка flash сообщения.
     * @param flashMessage текст сообщения
     */
    public void setFlashMessage(final String flashMessage) {
        this.flashMessage = flashMessage;
    }

    /**
     * Получение заголовка страницы.
     * @return Заголовок страницы
     */
    public String getTitle() {
        return title;
    }
}
