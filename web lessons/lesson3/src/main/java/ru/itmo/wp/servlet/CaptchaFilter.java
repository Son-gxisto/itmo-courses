package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Random;

public class CaptchaFilter extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        //System.out.println(request.getRequestURI());
        if (request.getMethod().equals("GET")) {
            if ("1".equals(session.getAttribute("captcha")) || request.getRequestURI().endsWith(".ico")) {
                chain.doFilter(request, response);
            } else {
                generateCaptcha(request, response);
                if (!request.getRequestURI().endsWith(".ico"))
                session.setAttribute("lastReq", request.getRequestURI());
            }
        } else if (request.getMethod().equals("POST") && request.getRequestURI().endsWith("captcha")){
            String reqcapt = request.getParameter("captcha");
            String sessioncapt = (String) session.getAttribute("captcha");
            if (session.getAttribute("captcha").equals("1") || reqcapt.equals(sessioncapt)) {
                session.setAttribute("captcha", "1");
                response.sendRedirect((String) session.getAttribute("lastReq"));
            } else {
                generateCaptcha(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
    private void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer num = new Random().nextInt(900) + 100;
        request.getSession().setAttribute("captcha", String.valueOf(num));
        OutputStream output = response.getOutputStream();
        byte[] image = Base64.getEncoder().encode(ImageUtils.toPng(String.valueOf(num)));
        output.write("<div><img src=\"data:image/png;base64, ".getBytes());
        output.write(image);
        output.write("\"></div>\n".getBytes());
        output.write(("<div class=\"captcha-form\">" +
                "    <form action=\"captcha\" method=\"post\">" +
                "        <input name=\"captcha\" id=\"captcha_id\">" +
                "    </form>" +
                "</div>").getBytes());
        response.setContentType("text/html");
    }
}
