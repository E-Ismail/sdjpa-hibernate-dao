package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Created by jt on 8/29/21.
 */
@Component
public class BookDaoImpl implements BookDao {
    private final EntityManagerFactory emf;

    public BookDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Book> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Book> query = em.createNamedQuery("find_all_book", Book.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Book findbyISBN(String isbn) {
        EntityManager em = getEntityManager();
        try {
            //Query query = em.createQuery("select b from Book b where b.isbn = :isbn");
            TypedQuery<Book> query = em.createQuery("select b from Book b where b.isbn = :isbn", Book.class);
            query.setParameter("isbn", isbn);
            Book found = query.getSingleResult();
            return found;
        } finally {
            em.close();
        }
    }

    @Override
    public Book getById(Long id) {
        EntityManager em = getEntityManager();
        Book book = getEntityManager().find(Book.class, id);
        em.close();
        return book;
    }

    @Override
    public Book findBookByTitle(String title) {
        EntityManager em = getEntityManager();
        //TypedQuery<Book> query = em
        //        .createQuery("SELECT b FROM Book b where b.title = :title", Book.class);
        TypedQuery<Book> query = em.createNamedQuery("find_book_by_title", Book.class);
        query.setParameter("title", title);
        Book book = query.getSingleResult();
        em.close();
        return book;
    }

    @Override
    public Book saveNewBook(Book book) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(book);
        em.flush();
        em.getTransaction().commit();
        em.close();
        return book;
    }

    @Override
    public Book updateBook(Book book) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(book);
        em.flush();
        em.clear();
        Book savedBook = em.find(Book.class, book.getId());
        em.getTransaction().commit();
        em.close();
        return savedBook;
    }

    @Override
    public void deleteBookById(Long id) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Book book = em.find(Book.class, id);
        em.remove(book);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Book findBookByTitleCriteria(String title) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Book> cq = cb.createQuery(Book.class);
            Root<Book> root = cq.from(Book.class);
            cq.select(root).where(cb.equal(root.get("title"), title));
            return em.createQuery(cq).getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public Book findBookByTitleNative(String title) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNativeQuery("SELECT * FROM book b where b.title = :title", Book.class);
            query.setParameter("title", title);
            return (Book) query.getSingleResult();
        } finally {
            em.close();
        }
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
