package com.vel.rest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vel.rest.domain.article.Article;
import com.vel.rest.repository.article.ArticleRepository;
import com.vel.rest.service.ArticleService;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleRepository articleRepository;

	@Override
	public List<Article> getAllArticles() {
		return (List<Article>) articleRepository.findAll();
	}

	@Override
	public Article getArticleById(int articleId) {
		return articleRepository.findById(articleId).get();
	}

	@Override
	public void addArticle(Article article) {

		articleRepository.save(article);
	}

	@Override
	public void updateArticle(Article article) {

		Article articleToUpdate = articleRepository.findById(article.getArticleId()).get();
		articleToUpdate.setCategory(article.getCategory());
		articleToUpdate.setTitle(article.getTitle());
		articleRepository.save(articleToUpdate);
	}

	@Override
	public void deleteArticle(int articleId) {

		articleRepository.deleteById(articleId);
	}

	@Override
	public boolean articleExists(String title, String category) {

		Article article = articleRepository.findByTitleAndCategory(title, category);

		return article != null ? Boolean.TRUE : Boolean.FALSE;
	}

}
