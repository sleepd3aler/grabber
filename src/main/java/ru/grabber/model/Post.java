package ru.grabber.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Post {
    private int id;

    private String description;

    private String link;

    private LocalDateTime created = LocalDateTime.now();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
            "dd.MM.y HH:mm:ss"
    );

    public Post(int id, String description, String link) {
        this.id = id;
        this.description = description;
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }

    @Override
    public String toString() {
        return "Post{" +
                " id=" + id +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", created=" + created.format(FORMATTER) +
                '}';
    }

}
