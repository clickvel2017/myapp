package com.vel.rest.repository.article;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vel.rest.domain.article.Article;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Integer> {

	public Article findByTitleAndCategory(String title,String category);
}
