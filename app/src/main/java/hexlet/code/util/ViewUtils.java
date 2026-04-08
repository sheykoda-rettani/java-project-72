package hexlet.code.util;

import java.time.format.DateTimeFormatter;

public final class ViewUtils {
    private ViewUtils() { }

    /**
     * Форматирование даты для вывода на страницах.
     */
    public static final DateTimeFormatter PAGE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
