package ru.itmo.web.lesson4.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getServletContext().getRealPath(request.getRequestURI()));
        String sourcePath = "C:\\Users\\istra\\OneDrive\\Рабочий стол\\lesson4\\src\\main\\webapp";
        File sourceFile = new File(sourcePath + request.getRequestURI());
        if (sourceFile.isFile()) {
            file = sourceFile;
        }
        if (file.isFile()) {
            response.setContentType(getServletContext().getMimeType(file.getCanonicalPath()));
            Files.copy(file.toPath(), response.getOutputStream());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
