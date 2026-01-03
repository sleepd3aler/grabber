package ru;

import java.sql.DriverManager;
import java.time.LocalDateTime;
import ru.grabber.model.Post;
import ru.grabber.service.Config;
import ru.grabber.service.SchedulerManager;
import ru.grabber.service.SuperJobGrab;
import ru.grabber.stores.JdbcStore;

import static ru.grabber.service.Config.log;

public class Main {
    public static void main(String[] args) {
        var config = new Config();
        config.load("application.properties");
        config.load("rabbit.properties");
        try (var connection = DriverManager.getConnection(
                config.get("db.url"),
                config.get("db.username"),
                config.get("db.password"));
             var scheduler = new SchedulerManager()) {
            var store = new JdbcStore(connection);
            var post = new Post("Super Java Job",
                    "link",
                    "",
                    LocalDateTime.of(2026, 1, 3, 12, 50));
            store.save(post);
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store);
            Thread.sleep(10000);
        } catch (Exception e) {
            log.error("When create a connection", e);
        }
    }
}
