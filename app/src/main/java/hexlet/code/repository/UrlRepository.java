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
        String sql = "SELECT * FROM urls ORDER BY created_at DESC";
        List<Url> urls = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                urls.add(new Url(id, name, createdAt));
            }
        }
        return urls;
    }

    public static Optional<Url> findById(final long id) throws SQLException {
        String sql = "SELECT * FROM urls WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                    return Optional.of(new Url(id, name, createdAt));
                }
            }
        }
        return Optional.empty();
    }

    public static boolean existsByName(final String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM urls WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public static Optional<Url> findByName(final String name) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                    return Optional.of(new Url(id, name, createdAt));
                }
            }
        }
        return Optional.empty();
    }
}
