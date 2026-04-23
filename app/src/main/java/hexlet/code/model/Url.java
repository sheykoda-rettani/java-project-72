package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
     * Время последней проверки.
     */
    private LocalDateTime lastCheckAt;
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
}
