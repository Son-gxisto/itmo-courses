package ru.itmo.wp.servlet;

import com.google.gson.Gson;
import ru.itmo.wp.util.Messages;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.String;

public class MessagesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String uri = request.getRequestURI();
        ServletContext servlet = getServletContext();
        String json = "";
        if (uri.endsWith("findAll")) {
            if (servlet.getAttribute("messages") == null)
                servlet.setAttribute("messages", new Messages());
            json = new Gson().toJson(((Messages) servlet.getAttribute("messages")).getMsgList());
        } else if (uri.endsWith("auth")) {
            String user = request.getParameter("user");
            if (user != null) {
                request.getSession().setAttribute("user", user);
                json = new Gson().toJson(user);
            }
        } else if (uri.endsWith("add")) {
            if (servlet.getAttribute("messages") == null)
                servlet.setAttribute("messages", new Messages());
            ((Messages) servlet.getAttribute("messages")).add(
                    request.getSession().getAttribute("user").toString(), request.getParameter("text"));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        response.getWriter().print(json);
        response.getWriter().flush();
    }
}