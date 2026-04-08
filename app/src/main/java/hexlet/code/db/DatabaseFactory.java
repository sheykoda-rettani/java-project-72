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
            String dbFilePath = "D:/IDEAPR~1/hexlet/page_analyzer/project";
            String h2Url = "jdbc:h2:file:" + dbFilePath + ";AUTO_SERVER=TRUE";
            config.setJdbcUrl(h2Url);
        }

        config.setMaximumPoolSize(maximumPoolSize);

        return new HikariDataSource(config);
    }
}
