package hexlet.code.model;

import java.time.LocalDateTime;

public final class Url {
    /**
     * Поле id.
     */
    private Long id;
    /**
     * Поле name.
     */
    private String name;
    /**
     * Поле createdAt.
     */
    private LocalDateTime createdAt;

    public Url(final String aName) {
        this.name = aName;
        this.createdAt = LocalDateTime.now();
    }

    public Url() {
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

    public void setName(final String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
