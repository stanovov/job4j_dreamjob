package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = PsqlStore.instOf().findByEmail(email);
        req.setAttribute("email", email);
        req.setAttribute("password", password);
        if (user != null) {
            req.setAttribute("name", name);
            req.setAttribute("error", "Пользователь с такой электронной почтой уже есть");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
            return;
        }
        user = new User(0, name, email, password);
        PsqlStore.instOf().save(user);
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }
}
