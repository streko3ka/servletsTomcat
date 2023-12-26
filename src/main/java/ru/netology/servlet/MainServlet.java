package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private static final String API_HOME = "/";
  private static final String API_POSTS = "/api/posts";
  private static final String API_POSTS_ID = "/api/posts/\\d+";
  private static final String GET = "GET";
  private static final String POST = "POST";
  private static final String DELETE = "DELETE";

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(GET) && path.equals(API_HOME)) {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print("Hello, I am your server");
      }
      if (method.equals(GET) && path.equals(API_POSTS)) { //выводит всё
        controller.all(resp);
        return;
      }
      if (method.equals(GET) && path.matches(API_POSTS_ID)) { //получаем id
        // easy way
        final var id = parser(path);
        controller.getById(id, resp);
        return;
      }
      if (method.equals(POST) && path.equals(API_POSTS)) { //делаем запись
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(DELETE) && path.matches(API_POSTS_ID)) { //удаление
        // easy way
        final var id = parser(path);
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private long parser(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
  }
}