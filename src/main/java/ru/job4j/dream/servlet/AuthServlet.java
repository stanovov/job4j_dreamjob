package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (email == null || password == null) {
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }
        if (email.isEmpty()) {
            req.setAttribute("password", password);
            req.setAttribute("error", "Электронная почта не заполнена");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }
        Store store = PsqlStore.instOf();
        User user = store.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            req.setAttribute("email", email);
            req.setAttribute("password", password);
            req.setAttribute("error", "Не верный email или пароль");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }
        HttpSession sc = req.getSession();
        sc.setAttribute("user", user);
        resp.sendRedirect(req.getContextPath() + "/posts.do");
    }
}