package ru.job4j.dream.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadPhotoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Path path = Path.of("C:\\images\\" + id);
        File photo;
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            photo = new File("C:\\images\\no_photo.jpg");
        } else {
            photo = path.toFile();
        }
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + photo.getName() + "\"");
        try (FileInputStream stream = new FileInputStream(photo)) {
            resp.getOutputStream().write(stream.readAllBytes());
        }
    }
}
