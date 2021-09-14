package ru.job4j.dream.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeletePhotoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Path path = Path.of("C:\\images\\" + id);
        if (Files.exists(path) && Files.isRegularFile(path)) {
            Files.delete(path);
        }
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
