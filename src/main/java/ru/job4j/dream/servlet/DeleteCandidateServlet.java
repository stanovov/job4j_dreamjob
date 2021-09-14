package ru.job4j.dream.servlet;

import ru.job4j.dream.store.Store;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeleteCandidateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Store store = Store.instOf();
        store.deleteCandidate(Integer.parseInt(id));
        Path path = Path.of("C:\\images\\" + id);
        if (Files.exists(path) && Files.isRegularFile(path)) {
            Files.delete(path);
        }
        req.setAttribute("candidates", store.findAllCandidates());
        RequestDispatcher dispatcher = req.getRequestDispatcher("/candidates.jsp");
        dispatcher.forward(req, resp);
    }
}
