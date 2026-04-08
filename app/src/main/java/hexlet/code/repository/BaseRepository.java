package hexlet.code.repository;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.db.DatabaseFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class BaseRepository {
    protected BaseRepository() { }
    /**
     * Datasource для подключения и операций.
     */
    protected static HikariDataSource dataSource;

    static {
        dataSource = (HikariDataSource) DatabaseFactory.getDataSource();
    }

    /**
     * Метод для доступа к соединению с БД.
     * @return Соединение для подключения и операций.
     * @throws SQLException Ошибки Sql соединения.
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
