package hexlet.code.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

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
            config.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1");
        }

        config.setMaximumPoolSize(maximumPoolSize);
        config.setAutoCommit(false);

        return new HikariDataSource(config);
    }

    public static Jdbi getJdbi(final DataSource dataSource) {
        Jdbi jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());
        return jdbi;
    }
}
