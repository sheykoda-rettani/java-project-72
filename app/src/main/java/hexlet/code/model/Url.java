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
    /**
     * Самый свежий статус проверки.
     */
    private Integer lastStatus;

    public Url(final String aName) {
        this.name = aName;
        this.createdAt = LocalDateTime.now();
    }

    public Url(final Long aId, final String aName, final LocalDateTime aCreatedAt, final Integer aLastStatus) {
        this.id = aId;
        this.name = aName;
        this.createdAt = aCreatedAt;
        this.lastStatus = aLastStatus;
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

    public Integer getLastStatus() {
        return lastStatus;
    }
}
