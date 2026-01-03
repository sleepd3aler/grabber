package ru.grabber.stores;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ru.grabber.model.Post;

public class JdbcStore implements Store {
    private final Connection connection;

    public JdbcStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Post save(Post post) {
        try (PreparedStatement statement = connection.prepareStatement(
                "insert into posts(title, link, description, creation_time )" +
                        " values ( ?, ?, ?, ?) " +
                        "on conflict (link) do nothing ;",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getLink());
            statement.setString(3, post.getDescription());
            Timestamp created = Timestamp.valueOf(post.getCreated());
            statement.setTimestamp(4, created);
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                post.setId(resultSet.getLong("id"));
            }
            return post;
        } catch (SQLException se) {
            throw new RuntimeException(se);
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "select * from posts", Statement.RETURN_GENERATED_KEYS)) {
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                Post post = new Post(
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getTimestamp(5).toLocalDateTime());
                post.setId(resultSet.getInt(1));
                posts.add(post);
            }
        } catch (SQLException se) {
            throw new RuntimeException(se);
        }
        return posts;
    }

    @Override
    public Optional<Post> findById(Long id) {
        Post result = null;
        try (PreparedStatement statement = connection.prepareStatement(
                "select * from posts where id = ?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long currentId = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String link = resultSet.getString("link");
                String description = resultSet.getString("description");
                LocalDateTime created = resultSet.getTimestamp("creation_time").toLocalDateTime();
                result = new Post(title, link, description, created);
                result.setId(currentId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(result);
    }

}
