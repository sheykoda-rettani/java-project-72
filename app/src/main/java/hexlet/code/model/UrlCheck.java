package hexlet.code.model;

import java.time.LocalDateTime;

public final class UrlCheck {
    /**
     * Поле id.
     */
    private Long id;
    /**
     * Статус ответа.
     */
    private Integer statusCode;
    /**
     * Название страницы.
     */
    private String title;
    /**
     * Заголовок.
     */
    private String h1;
    /**
     * Описание.
     */
    private String description;
    /**
     * Время создания.
     */
    private LocalDateTime createdAt;
    /**
     * Ссылка на родительскую запись.
     */
    private final Long urlId;

    public UrlCheck(final Long aUrlId,
                    final Integer aStatusCode,
                    final String aTitle,
                    final String aH1,
                    final String aDescription) {
        this.urlId = aUrlId;
        this.statusCode = aStatusCode;
        this.title = aTitle;
        this.h1 = aH1;
        this.description = aDescription;
        this.createdAt = LocalDateTime.now();
    }

    public UrlCheck(final Long aId,
                    final Long aUrlId,
                    final Integer aStatusCode,
                    final String aTitle,
                    final String aH1,
                    final String aDescription,
                    final LocalDateTime aCreatedAt) {
        this.id = aId;
        this.urlId = aUrlId;
        this.statusCode = aStatusCode;
        this.title = aTitle;
        this.h1 = aH1;
        this.description = aDescription;
        this.createdAt = aCreatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getTitle() {
        return title;
    }

    public String getH1() {
        return h1;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUrlId() {
        return urlId;
    }
}
