/*
package com.example.service;

import com.example.models.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class BookService {

    @Inject
    private SessionFactory sessionFactory; // Фабрика сессий Hibernate

    // Получение текущей сессии
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    // 1. CRUD операции через Hibernate API

    public Book createBook(Book book) {
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        getCurrentSession().persist(book);
        return book;
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = getCurrentSession().get(Book.class, id);
        if (book == null) {
            throw new RuntimeException("Book with id " + id + " not found");
        }

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPages(bookDetails.getPages());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setDescription(bookDetails.getDescription());
        book.setUpdatedAt(LocalDateTime.now());

        return getCurrentSession().merge(book);
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(getCurrentSession().get(Book.class, id));
    }

    public Book findByIdOrThrow(Long id) {
        Book book = getCurrentSession().get(Book.class, id);
        if (book == null) {
            throw new RuntimeException("Book not found: " + id);
        }
        return book;
    }

    public void deleteBook(Long id) {
        Book book = getCurrentSession().get(Book.class, id);
        if (book != null) {
            getCurrentSession().remove(book);
        }
    }

    // 2. Поиск и фильтрация

    public List<Book> findAll() {
        return getCurrentSession()
                .createSelectionQuery("FROM Book b ORDER BY b.title", Book.class)
                .getResultList();
    }

    public List<Book> findByAuthor(String author) {
        return getCurrentSession()
                .createSelectionQuery(
                        "FROM Book b WHERE b.author = :author ORDER BY b.createdAt DESC",
                        Book.class
                )
                .setParameter("author", author)
                .getResultList();
    }



    public List<Book> searchByTitle(String titleKeyword) {
        return getCurrentSession()
                .createSelectionQuery(
                        "FROM Book b WHERE LOWER(b.title) LIKE LOWER(:keyword)",
                        Book.class
                )
                .setParameter("keyword", "%" + titleKeyword + "%")
                .getResultList();
    }

    public Optional<Book> findByIsbn(String isbn) {
        try {
            Book book = getCurrentSession()
                    .createSelectionQuery(
                            "FROM Book b WHERE b.isbn = :isbn",
                            Book.class
                    )
                    .setParameter("isbn", isbn)
                    .getSingleResultOrNull();
            return Optional.ofNullable(book);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // 3. Пагинация и сортировка

    public List<Book> findPaginated(int page, int size, String sortBy, boolean ascending) {
        String order = ascending ? "ASC" : "DESC";
        String queryString = String.format(
                "FROM Book b ORDER BY b.%s %s",
                sortBy, order
        );

        return getCurrentSession()
                .createSelectionQuery(queryString, Book.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Long countAllBooks() {
        return getCurrentSession()
                .createSelectionQuery("SELECT COUNT(b) FROM Book b", Long.class)
                .getSingleResult();
    }

    // 4. Batch операции

    public void createBooksBatch(List<Book> books) {
        Session session = getCurrentSession();

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            book.setCreatedAt(LocalDateTime.now());
            book.setUpdatedAt(LocalDateTime.now());
            session.persist(book);

            // Очистка сессии каждые 50 записей для оптимизации
            if (i % 50 == 0 && i > 0) {
                session.flush();
                session.clear();
            }
        }
    }



    // 5. Использование StatelessSession для массовых операций

    public void updateBookTitlesBatch(Map<Long, String> idToTitleMap) {
        try (StatelessSession statelessSession = sessionFactory.openStatelessSession()) {
            statelessSession.getTransaction().begin();

            for (Map.Entry<Long, String> entry : idToTitleMap.entrySet()) {
                Book book = statelessSession.get(Book.class, entry.getKey());
                if (book != null) {
                    book.setTitle(entry.getValue());
                    book.setUpdatedAt(LocalDateTime.now());
                    statelessSession.update(book);
                }
            }

            statelessSession.getTransaction().commit();
        }
    }

    // 6. Нативные SQL запросы

    public List<Book> findBooksNativeSql(String titlePattern) {
        return getCurrentSession()
                .createNativeQuery(
                        "SELECT * FROM books WHERE title ILIKE :pattern ORDER BY publication_year DESC",
                        Book.class
                )
                .setParameter("pattern", "%" + titlePattern + "%")
                .getResultList();
    }

    public List<Object[]> getBookStatistics() {
        return getCurrentSession()
                .createNativeQuery(
                        """
                        SELECT 
                            genre,
                            COUNT(*) as count,
                            AVG(pages) as avg_pages,
                            MIN(publication_year) as oldest_year,
                            MAX(publication_year) as newest_year
                        FROM books 
                        GROUP BY genre
                        """
                )
                .getResultList();
    }

    // 7. Критерия API (типобезопасные запросы)

    public List<Book> findBooksWithCriteria(String author, Integer minPages) {
        var cb = getCurrentSession().getCriteriaBuilder();
        var query = cb.createQuery(Book.class);
        var book = query.from(Book.class);

        var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

        if (author != null && !author.isEmpty()) {
            predicates.add(cb.equal(book.get("author"), author));
        }



        if (minPages != null) {
            predicates.add(cb.ge(book.get("pages"), minPages));
        }

        query.where(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        query.orderBy(cb.asc(book.get("title")));

        return getCurrentSession()
                .createSelectionQuery(query)
                .getResultList();
    }

    // 8. Работа с Lazy loading и JOIN FETCH

    public Book findBookWithReviews(Long bookId) {
        return getCurrentSession()
                .createSelectionQuery(
                        """
                        SELECT b FROM Book b 
                        LEFT JOIN FETCH b.reviews 
                        WHERE b.id = :id
                        """,
                        Book.class
                )
                .setParameter("id", bookId)
                .getSingleResultOrNull();
    }

    public List<Book> findAllBooksWithMetadata() {
        return getCurrentSession()
                .createSelectionQuery(
                        """
                        SELECT DISTINCT b FROM Book b 
                        LEFT JOIN FETCH b.metadata
                        """,
                        Book.class
                )
                .getResultList();
    }

    // 9. Работа с версиями (optimistic locking)

    public Book updateWithVersionCheck(Long id, String newTitle, Integer expectedVersion) {
        Book book = getCurrentSession().get(Book.class, id);

        if (book == null) {
            throw new RuntimeException("Book not found");
        }

        if (!book.getVersion().equals(expectedVersion)) {
            throw new RuntimeException(
                    "Concurrent modification detected. Expected version: " +
                            expectedVersion + ", actual: " + book.getVersion()
            );
        }

        book.setTitle(newTitle);
        book.setUpdatedAt(LocalDateTime.now());

        return book;
    }

    // 10. Продвинутые запросы с агрегацией



    public Double getAveragePagesByAuthor(String author) {
        return getCurrentSession()
                .createSelectionQuery(
                        "SELECT AVG(b.pages) FROM Book b WHERE b.author = :author",
                        Double.class
                )
                .setParameter("author", author)
                .getSingleResultOrNull();
    }

    // 11. Кэширование запросов

    @SuppressWarnings("unchecked")
    public List<Book> findPopularBooks() {
        Query<Book> query = getCurrentSession()
                .createSelectionQuery(
                        "FROM Book b WHERE b.pages > 300 ORDER BY b.createdAt DESC",
                        Book.class
                )
                .setCacheable(true) // Включаем кэширование запроса
                .setCacheRegion("popularBooks"); // Регион кэша

        return query.getResultList();
    }

    // 12. Очистка кэша

    public void clearCache() {
        sessionFactory.getCache().evictAll(); // Очистка всего кэша
        // Или конкретного региона:
        // sessionFactory.getCache().evictQueryRegion("popularBooks");
    }

    // 13. Проверка существования

    public boolean existsByIsbn(String isbn) {
        Long count = getCurrentSession()
                .createSelectionQuery(
                        "SELECT COUNT(b) FROM Book b WHERE b.isbn = :isbn",
                        Long.class
                )
                .setParameter("isbn", isbn)
                .getSingleResult();
        return count > 0;
    }

    // 14. Поиск по диапазону

    public List<Book> findByPublicationYearRange(Integer fromYear, Integer toYear) {
        return getCurrentSession()
                .createSelectionQuery(
                        "FROM Book b WHERE b.publicationYear BETWEEN :fromYear AND :toYear",
                        Book.class
                )
                .setParameter("fromYear", fromYear)
                .setParameter("toYear", toYear)
                .getResultList();
    }

    // 15. Транзакционные методы с разными уровнями изоляции

    @Transactional
    public Book updateBookSafely(Long id, String newTitle) {
        // Здесь можно настроить уровень изоляции через @TransactionAttribute
        Book book = getCurrentSession().get(Book.class, id);
        book.setTitle(newTitle);
        book.setUpdatedAt(LocalDateTime.now());
        return book;
    }
}*/
