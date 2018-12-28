package by.rekuts.giftcertificates.repository;

import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.embedded.PreparedDbProvider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.SQLException;

@TestConfiguration
@SpringBootTest
public class TestConfig {
    @Profile("debug")
    @Bean
    public DataSource dataSource() {
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

}