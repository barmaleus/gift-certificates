package by.rekuts.giftcertificates.repository;

import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.embedded.PreparedDbProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = "by.rekuts.giftcertificates.repository")
public class RepositoryConfig {

    @Bean
    @Profile("debug")
    public DataSource getDebugDataSource() {
        FlywayPreparer preparer = FlywayPreparer.forClasspathLocation("db");
        PreparedDbProvider provider = PreparedDbProvider.forPreparer(preparer);
        DataSource dataSource = null;
        try {
            dataSource = provider.createDataSource();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    @Bean
    @Profile("release")
    public DataSource getReleaseDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.addDataSourceProperty("databaseName", "gcerts");
        config.addDataSourceProperty("serverName", "127.0.0.1");
        return new HikariDataSource(config);
    }
}
