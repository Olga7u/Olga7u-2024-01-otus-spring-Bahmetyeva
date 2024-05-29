package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbc;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);

        return Optional.ofNullable(jdbc.query(
                "select distinct b.id, b.title, b.author_id, a.full_name, bg.genre_id, g.name " +
                        "from books b " +
                        "left join authors a on a.id = b.author_id " +
                        "left join books_genres bg on b.id = bg.book_id " +
                        "left join genres g on g.id = bg.genre_id " +
                        "where b.id = :id", params, new BookResultSetExtractor()));
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    @Transactional
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        removeGenresRelationsFor(id);
        jdbc.getJdbcOperations().update("delete from books where id=?", id);
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbc.query(
                "select b.id, b.title, b.author_id, a.full_name " +
                        "from books b left join authors a on a.id=b.author_id", new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("select book_id, genre_id from books_genres",
                (rs, rownum) -> new BookGenreRelation(
                        rs.getLong("book_id"), rs.getLong("genre_id")));
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
        Map<Long, Genre> genreMap = new LinkedHashMap<>();
        genres.forEach(g -> genreMap.put(g.getId(), g));
        booksWithoutGenres.forEach(b -> {
            b.setGenres(new ArrayList<>());
            relations.forEach(r -> {
                if (r.bookId == b.getId()) {
                    b.getGenres().add(genreMap.get(r.genreId));
                }
            });
        });
    }

    private Book insert(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle())
                .addValue("author_id", book.getAuthor().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update("insert into books (title, author_id) values (:title, :author_id)", params, keyHolder);

        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle())
                .addValue("author_id", book.getAuthor().getId())
                .addValue("id", book.getId());

        int cnt = jdbc.update("update books set title=:title, author_id=:author_id where id=:id", params);
        //...

        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД

        if (cnt == 0) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(book.getId()));
        }

        removeGenresRelationsFor(book.getId());
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        // Использовать метод batchUpdate
        SqlParameterSource[] batchValues = book.getGenres().stream().map(g -> new MapSqlParameterSource()
                        .addValue("book_id", book.getId())
                        .addValue("genre_id", g.getId()))
                .toArray(SqlParameterSource[]::new);

        jdbc.batchUpdate("insert into books_genres(book_id, genre_id) values (:book_id, :genre_id)", batchValues);
    }

    private void removeGenresRelationsFor(long bookId) {
        jdbc.getJdbcOperations().update("delete from books_genres where book_id=?", bookId);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(
                    rs.getLong("id"),
                    rs.getString("title"),
                    new Author(rs.getLong("author_id"), rs.getString("full_name")),
                    null);
        }
    }

    // Использовать для findById
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;
            while (rs.next()) {
                if (rs.isFirst()) {
                    book = new Book(
                            rs.getLong("id"),
                            rs.getString("title"),
                            new Author(rs.getLong("author_id"), rs.getString("full_name")),
                            new ArrayList<>());
                }
                if (book != null) {
                    book.getGenres().add(new Genre(rs.getLong("genre_id"), rs.getString("name")));
                }
            }
            return book;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
