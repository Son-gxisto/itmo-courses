package ru.itmo.wp.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class StaticServlet extends HttpServlet {
    private final static String srcPath = "C:\\Users\\istra\\OneDrive\\Рабочий стол\\web3\\src\\main\\webapp\\static"; //"..\\..\\src\\main\\webapp\\static";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        //System.out.println("responseStatic...");
        if (uri.contains("+")) {
            String[] uriList = uri.split("\\+");
            uriList[0] = uriList[0].substring(1);
            OutputStream outputStream = response.getOutputStream();
            for (String s : uriList) {
                File file = new File(srcPath + "\\" + s);
                if (!file.isFile()) {
                    file = new File(getServletContext().getRealPath("/static" + s));
                }
                if (file.isFile()) {
                    if (response.getContentType() == null)
                        response.setContentType(getContentTypeFromName(s));
                    Files.copy(file.toPath(), outputStream);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
            outputStream.flush();
        } else {
            File file = new File(srcPath + uri);
            if (!file.isFile()) {
                file = new File(getServletContext().getRealPath("/static" + uri));
            }
            if (file.isFile()) {
                response.setContentType(getContentTypeFromName(uri));
                OutputStream outputStream = response.getOutputStream();
                Files.copy(file.toPath(), outputStream);
                outputStream.flush();
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    private String getContentTypeFromName(String name) {
        name = name.toLowerCase();

        if (name.endsWith(".png")) {
            return "image/png";
        }

        if (name.endsWith(".jpg")) {
            return "image/jpeg";
        }

        if (name.endsWith(".html")) {
            return "text/html";
        }

        if (name.endsWith(".css")) {
            return "text/css";
        }

        if (name.endsWith(".js")) {
            return "application/javascript";
        }

        throw new IllegalArgumentException("Can't find content type for '" + name + "'.");
    }
}
