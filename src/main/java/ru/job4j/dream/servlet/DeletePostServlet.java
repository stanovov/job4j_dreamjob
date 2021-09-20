package ru.job4j.dream.servlet;

import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeletePostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Store store = PsqlStore.instOf();
        store.deletePost(Integer.parseInt(id));
        req.setAttribute("posts", store.findAllPosts());
        RequestDispatcher dispatcher = req.getRequestDispatcher("/posts.jsp");
        dispatcher.forward(req, resp);
    }
}
