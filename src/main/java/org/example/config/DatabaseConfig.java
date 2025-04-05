package org.example.config;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConfig {
    private static final String PROPERTIES_FILE = "local/application.properties";

    public static DataSource createDataSource() {
        Properties props = loadProperties();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.username"));
        config.setPassword(props.getProperty("db.password"));
        config.setDriverClassName(props.getProperty("db.driver"));

        // Настройки пула соединений
        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.maxSize")));
        config.setMinimumIdle(Integer.parseInt(props.getProperty("db.pool.minIdle")));
        config.setConnectionTimeout(Long.parseLong(props.getProperty("db.pool.connectionTimeout")));
        config.setIdleTimeout(Long.parseLong(props.getProperty("db.pool.idleTimeout")));
        config.setMaxLifetime(Long.parseLong(props.getProperty("db.pool.maxLifetime")));

        return new HikariDataSource(config);
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + PROPERTIES_FILE);
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file", e);
        }
        return props;
    }
}