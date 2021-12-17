package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;

import java.util.List;

public class ArticleService {
    private final ArticleRepository articleRepository = new ArticleRepositoryImpl();
    public void save(String title, String text, long userId) {
        Article article = new Article();
        article.setTitle(title);
        article.setText(text);
        article.setUserId(userId);
        articleRepository.save(article);
    }
    public void validateArticle(String title, String text) throws ValidationException {
        if (title.isEmpty() || text.isEmpty()) {
            throw new ValidationException("Write title and text");
        }
        if (title.length() > 255 || text.length() > 65000) {
            throw new ValidationException("Very long title or text");
        }
    }
    public List<Article> findAll() {
        return articleRepository.findAll();
    }
    public List<Article> findAllByUser(long userId) {
        return articleRepository.findAllByUser(userId);
    }
    public void setHidden(long articleId, long userId, boolean value) {
        Article article = articleRepository.find(articleId);
        if (article.getUserId() == userId) {
            article.setHidden(value);
            articleRepository.update(article);
        }
    }
}
