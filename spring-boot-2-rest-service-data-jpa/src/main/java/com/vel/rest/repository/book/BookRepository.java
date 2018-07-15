package com.vel.rest.repository.book;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.vel.rest.domain.book.Book;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

	@RestResource(path="books/findBook/{id}")
	public Book findbyId(@Param("id") Long id);
	
    public Book findByIsbn(@Param("isbn") String isbn);
    
    @RestResource(path="books/getBooksByAuthor/{author}")
    public List<Book> findByAuthorIgnoreCaseOrderByTitleAsc(@Param("author") String author);  
    
    @RestResource(path="books/getBooksBetweenYears/{startYear}/{endYear}")
    @Query("SELECT b FROM Book b WHERE b.published BETWEEN :startYear AND :endYear ORDER BY b.published")
    public List<Book> getBooksBetweenYears(@Param("startYear")int startYear, @Param("endYear")int endYear);
    
    public List<Book> findByTitleContaining(@Param("title") String title); 
   
    public int countByAuthor(@Param("author") String author);
}
