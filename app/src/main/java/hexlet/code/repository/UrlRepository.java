package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class UrlRepository extends BaseRepository {
    private UrlRepository() { }

    /**
     * Основной запрос на URL + статус.
     */
    private static final String MAIN_URL_QUERY = "SELECT u.*, "
            + "("
            + "SELECT status_code from url_checks uc "
            + "WHERE uc.url_id = u.id ORDER BY uc.created_at DESC LIMIT 1"
            + ") as status_code  FROM urls u ";

    public static void save(final Url url) throws SQLException {
        String sql = "INSERT INTO urls(name, created_at) VALUES (?, ?)";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            LocalDateTime createdAt = LocalDateTime.now();
            stmt.setString(1, url.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(createdAt));

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    url.setId(generatedKeys.getLong(1));
                    url.setCreatedAt(createdAt);
                } else {
                    throw new SQLException("Не удалось получить ID после сохранения URL");
                }
            }
        }
    }

    public static List<Url> findAll() throws SQLException {
        String sql = MAIN_URL_QUERY + "ORDER BY u.created_at DESC";
        List<Url> urls = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                Integer statusCode = rs.getInt("status_code");
                urls.add(new Url(id, name, createdAt, statusCode));
            }
        }
        return urls;
    }

    public static Optional<Url> findById(final long id) throws SQLException {
        String sql = MAIN_URL_QUERY + "WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                    return Optional.of(new Url(id, name, createdAt, null));
                }
            }
        }
        return Optional.empty();
    }

    public static Long getIdByName(final String name) throws SQLException {
        String sql = "SELECT id FROM urls WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        }
        return null;
    }
}
