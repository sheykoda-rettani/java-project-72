package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class UrlCheckRepository extends BaseRepository {
    private UrlCheckRepository() { }

    public static void save(final UrlCheck check) throws SQLException {
        String sql = """
            INSERT INTO url_checks(url_id, status_code, title, h1, description, created_at)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        final int urlIdIdx = 1;
        final int statusCodeIdx = 2;
        final int titleIdx = 3;
        final int h1Idx = 4;
        final int descriptionIdx = 5;
        final int createdAtIdx = 6;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            LocalDateTime createdAt = LocalDateTime.now();
            stmt.setLong(urlIdIdx, check.getUrlId());
            stmt.setInt(statusCodeIdx, check.getStatusCode());
            stmt.setString(titleIdx, check.getTitle());
            stmt.setString(h1Idx, check.getH1());
            stmt.setString(descriptionIdx, check.getDescription());
            stmt.setTimestamp(createdAtIdx, Timestamp.valueOf(createdAt));

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    check.setId(generatedKeys.getLong(1));
                    check.setCreatedAt(createdAt);
                } else {
                    throw new SQLException("Не удалось получить ID проверки");
                }
            }
        }
    }

    public static List<UrlCheck> findByUrlId(final Long urlId) throws SQLException {
        List<UrlCheck> checks = new ArrayList<>();
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                checks.add(new UrlCheck(
                        rs.getLong("id"),
                        rs.getLong("url_id"),
                        rs.getInt("status_code"),
                        rs.getString("title"),
                        rs.getString("h1"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        }
        return checks;
    }
}
