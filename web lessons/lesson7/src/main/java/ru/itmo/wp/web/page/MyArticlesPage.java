package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.ArticleService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class MyArticlesPage {
    private final ArticleService articleService = new ArticleService();
    void action(HttpServletRequest request, Map<String, Object> view) {
        if (request.getSession().getAttribute("user") != null) {
            long userId = ((User) request.getSession().getAttribute("user")).getId();
            view.put("articles", articleService.findAllByUser(userId));
        }
    }
    void hos(HttpServletRequest request, Map<String, Object> view) {
        long aId = Long.parseLong(request.getParameter("articleId"));
        articleService.setHidden(aId, ((User) request.getSession().getAttribute("user")).getId(), request.getParameter("value").equals("Hide"));
    }
}
