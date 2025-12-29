package ru.grabber.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Post {
    private int id;

    private String description;

    private String link;

    private LocalDateTime created;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
            "dd.MM.y HH:mm:ss"
    );

    public Post(String description, String link, LocalDateTime created) {
        this.description = description;
        this.link = link;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
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
