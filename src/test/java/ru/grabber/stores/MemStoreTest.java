package ru.grabber.stores;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.grabber.model.Post;

import static org.assertj.core.api.Assertions.*;

class MemStoreTest {

    private Post first;
    private Post second;
    private Post third;

    @BeforeEach
    void init() {
        LocalDateTime dt = LocalDateTime.of(2026, 1, 2, 15, 40);
        first = new Post("tittle 1", "link 1", "desc 1", dt);
        second = new Post("tittle 2", "link 2", "desc 2", dt);
        third = new Post("tittle 3", "link 3", "desc 3", dt);
    }

    @Test
    void whenFindByIllegalIdThenNotNullReturns() {
        MemStore store = new MemStore();
        assertThat(store.findById(1L)).isEmpty();
    }

    @Test
    void whenSaveThenFoundedById() {
        MemStore store = new MemStore();
        store.save(first);
        store.save(second);
        store.save(third);
        first.setId(1);
        second.setId(2);
        third.setId(3);
        assertThat(store.findById(1L).get()).isEqualTo(first);
        assertThat(store.findById(2L).get()).isEqualTo(second);
        assertThat(store.findById(3L).get()).isEqualTo(third);
    }

    @Test
    void whenGetAllThenExpectedResult() {
        MemStore store = new MemStore();
        store.save(first);
        store.save(second);
        store.save(third);
        List<Post> expected = List.of(first, second, third);
        assertThat(store.getAll()).isEqualTo(expected);
    }
}