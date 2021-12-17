package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage {
    private final ArticleService articleService = new ArticleService();
    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }
    private void createArticle(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        if (request.getSession().getAttribute("user") != null) {
            String title = request.getParameter("title");
            String text = request.getParameter("text");
            long userId = ((User) request.getSession().getAttribute("user")).getId();
            articleService.validateArticle(title, text);
            articleService.save(title, text, userId);
            throw new RedirectException("/article");
        } else {
            throw new ValidationException("You aren't logged");
        }
    }
}
