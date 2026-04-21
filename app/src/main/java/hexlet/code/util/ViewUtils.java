package hexlet.code.util;

import java.time.format.DateTimeFormatter;

public final class ViewUtils {
    private ViewUtils() { }

    /**
     * Форматирование даты для вывода на страницах.
     */
    public static final DateTimeFormatter PAGE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String truncateWithEllipsis(final String toTruncate, final int length)
            throws IllegalArgumentException {
        if (length <= 1) {
            throw new IllegalArgumentException("Длина обрезанной строки должна быть больше 1");
        }
        if (toTruncate == null || toTruncate.length() <= length) {
            return toTruncate;
        } else {
            return toTruncate.substring(0, length) + "...";
        }
    }

    public static String truncateWithEllipsis(final String toTruncate) throws IllegalArgumentException {
        final int defaultLength = 200;
        return truncateWithEllipsis(toTruncate, defaultLength);
    }
}
