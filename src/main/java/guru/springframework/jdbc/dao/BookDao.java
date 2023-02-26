package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;

/**
 * @author E.I.
 * <p>
 * {@code @Date}  11/13/2022
 */
public interface BookDao {

    Book getById(Long id);

    Book findBookByTitle(String title);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(Long id);
}
