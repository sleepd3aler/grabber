package ru;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import ru.grabber.model.Post;
import ru.grabber.service.*;
import ru.grabber.stores.JdbcStore;
import ru.grabber.stores.Store;
import ru.grabber.utils.HabrDateTimeParser;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Config config = new Config();
        config.load("application.properties");
        try (Connection connection = DriverManager.getConnection(config.get("db.url"),
                config.get("db.username"),
                config.get("db.password"))) {
            Store store = new JdbcStore(connection);

            HabrCareerParse parse = new HabrCareerParse(new HabrDateTimeParser());
            List<Post> postList = parse.fetch("https://career.habr.com/");
            postList.forEach(store::save);
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
