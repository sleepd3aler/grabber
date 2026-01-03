package ru.grabber.stores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ru.grabber.model.Post;

public class MemStore implements Store {
    private final Map<Long, Post> storage = new HashMap<>();
    private int ids = 1;

    @Override
    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(ids++);
            storage.put(post.getId(), post);
        }
        return post;
    }

    @Override
    public List<Post> getAll() {
        return storage.values().stream().toList();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

}
