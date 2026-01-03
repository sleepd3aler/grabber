package ru.grabber.stores;

import java.util.List;
import java.util.Optional;
import ru.grabber.model.Post;

public interface Store {
    public void save(Post post);

    List<Post> getAll();

    Optional<Post> findById(Long id);

}
