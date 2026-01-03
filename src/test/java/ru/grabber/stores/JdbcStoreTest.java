package ru.grabber.stores;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;
import org.junit.jupiter.api.*;
import ru.grabber.model.Post;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcStoreTest {

    private static Connection connection;
    private JdbcStore store;
    private Post first;
    private Post second;
    private Post third;

    @BeforeEach
    void setUp() {
        LocalDateTime created = LocalDateTime.of(2026, 1, 1, 1, 15);
        store = new JdbcStore(connection);
        first = new Post("title1", "link1", "desc1", created);
        second = new Post("title2", "link2", "desc2", created);
        third = new Post("title3", "link3", "desc3", created);
    }

    @BeforeAll
    public static void initConnection() {
        try (InputStream in = JdbcStore.class.getClassLoader().getResourceAsStream("db/liquibase_test.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @AfterEach
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "delete from posts")) {
            statement.execute();
        }
    }

    @Test
    void whenSavePostThenFindByIdMustBeSame() {
        store.save(first);
        store.save(second);
        store.save(third);
        second.setId(2);
        assertThat(store.findById(2L).get()).isEqualTo(second);
    }

}