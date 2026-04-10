package hexlet.code.model;

import java.time.LocalDateTime;

public final class Url {
    /**
     * Поле id.
     */
    private Long id;
    /**
     * Имя url.
     */
    private String name;
    /**
     * Время создания.
     */
    private LocalDateTime createdAt;

    public Url(final String aName) {
        this.name = aName;
        this.createdAt = LocalDateTime.now();
    }

    public Url(final Long aId, final String aName, final LocalDateTime aCreatedAt) {
        this.id = aId;
        this.name = aName;
        this.createdAt = aCreatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
