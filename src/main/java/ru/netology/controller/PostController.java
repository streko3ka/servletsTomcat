package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import org.springframework.stereotype.Controller;

@Controller
public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private final PostService service;
  private final Gson gson = new Gson();

  public PostController(PostService service) {

    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var data = service.all();
    final var gson = new Gson();
    response.getWriter().print(gson.toJson(data));
  }

  public void getById(long id, HttpServletResponse response) {
    //TODO: deserialize request & serialize response
    response.setContentType(APPLICATION_JSON);
    try {
      response.getWriter().print(gson.toJson(service.getById(id)));
    } catch (Exception e) {
      try {
        response.getWriter().print(gson.toJson(null));
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var gson = new Gson();
    final var post = gson.fromJson(body, Post.class);
    final var data = service.save(post);
    response.getWriter().print(gson.toJson(data));
  }

  public void removeById(long id, HttpServletResponse response) throws IOException {
    // TODO: deserialize request & serialize response
    try {
      if (service.getById(id) != null) {
        service.removeById(id);
        response.getWriter().print("Post " + id + " removed");
      }
    } catch (Exception e) {
      response.getWriter().print("Post " + id + " not exists");
    }
  }
}
