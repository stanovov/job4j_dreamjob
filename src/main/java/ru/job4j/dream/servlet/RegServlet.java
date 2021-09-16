package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (name == null || email == null || password == null) {
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("name", name);
        req.setAttribute("email", email);
        req.setAttribute("password", password);
        if (email.isEmpty()) {
            req.setAttribute("error", "Электронная почта не заполнена");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
            return;
        }
        Store store = PsqlStore.instOf();
        User user = store.findByEmail(email);
        if (user != null) {
            req.setAttribute("error", "Пользователь с такой электронной почтой уже есть");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
            return;
        }
        user = new User(0, name, email, password);
        store.save(user);
        req.removeAttribute("name");
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }
}
