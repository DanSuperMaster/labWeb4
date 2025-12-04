/*
package com.example.repository;

import com.example.models.Book;
import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class BookRepository {

    @Inject
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // Базовые операции

    public Book save(Book book) {
        if (book.getId() == null) {
            getSession().persist(book);
            return book;
        } else {
            return getSession().merge(book);
        }
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(getSession().get(Book.class, id));
    }

    public void delete(Long id) {
        Book book = getSession().get(Book.class, id);
        if (book != null) {
            getSession().remove(book);
        }
    }

    // Named Queries (можно вынести в аннотации Entity)

    @SuppressWarnings("unchecked")
    public List<Book> findRecentBooks(int limit) {
        Query<Book> query = getSession().createNamedQuery("Book.findRecent", Book.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    // Stored Procedure
    @SuppressWarnings("unchecked")
    public List<Book> findBooksByAuthorProcedure(String author) {
        return getSession()
                .createStoredProcedureQuery("find_books_by_author", Book.class)
                .registerStoredProcedureParameter(1, String.class, jakarta.persistence.ParameterMode.IN)
                .setParameter(1, author)
                .getResultList();
    }
}*/
