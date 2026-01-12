package ru;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.apache.log4j.Logger;
import ru.grabber.model.Post;
import ru.grabber.service.Config;
import ru.grabber.service.SchedulerManager;
import ru.grabber.service.SuperJobGrab;
import ru.grabber.service.Web;
import ru.grabber.stores.JdbcStore;
import ru.grabber.stores.Store;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Config config = new Config();
        config.load("application.properties");
        try (Connection connection = DriverManager.getConnection(config.get("db.url"),
                config.get("db.username"),
                config.get("db.password"))) {
            Store store = new JdbcStore(connection);

            Post post = new Post();
            post.setTitle("Super Java Job");
            post.setLink("");
            post.setDescription("");
            post.setCreated(LocalDateTime.of(2025, 2, 12, 21, 39));
            store.save(post);

            SchedulerManager scheduler = new SchedulerManager();
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store
            );

            new Web(store).start(Integer.parseInt(config.get("server.port")));
        } catch (SQLException e) {
            log.error("When creating a connection", e);
        }

    }
}
