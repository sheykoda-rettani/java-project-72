package hexlet.code.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public final class DatabaseFactory {
    private DatabaseFactory() { }

    public static DataSource getDataSource() {
        final int maximumPoolSize = 10;
        HikariConfig config = new HikariConfig();

        String dbUrl = System.getenv("JDBC_DATABASE_URL");

        if (dbUrl != null && !dbUrl.isEmpty()) {
            config.setJdbcUrl(dbUrl);
        } else {
            config.setJdbcUrl("jdbc:h2:mem:test-db;DB_CLOSE_DELAY=-1");
        }

        config.setMaximumPoolSize(maximumPoolSize);

        return new HikariDataSource(config);
    }
}
