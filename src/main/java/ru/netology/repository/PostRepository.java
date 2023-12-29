package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class PostRepository {

  private Map<Long, Post> posts;
  private long nextId;

  public PostRepository() {
    this.posts = new HashMap<>();
    this.nextId = 0;
  }

  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(posts.get(id));
  }

  public synchronized Post save(Post post) {
    long id = post.getId();
    if (id == 0) {
      id = nextId++;
      post.setId(id);
      posts.put(id, post);
    } else {
      long currentId = post.getId();
      posts.put(currentId, post);
    }
    return post;
  }

  public void removeById(long id) {
    if (posts.containsKey(id)) {
      posts.remove(id);
    } else {
      throw new NotFoundException("Error id");
    }
  }
}