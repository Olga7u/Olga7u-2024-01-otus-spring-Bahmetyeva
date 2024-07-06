package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final String ERR_MESSAGE = "Book with id %s not found";

    private final BookRepository bookRepository;

    @Override
    public List<BookDto> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(BookDto::fromDomainObject)
                .toList();
    }

    @Override
    public BookDto findBookById(long id) {
        Book book = new Book();
        if (id != 0) {
            Optional<Book> bookOptional = bookRepository.findById(id);
            if (bookOptional.isPresent()) {
                book = bookOptional.get();
            } else {
                throw new EntityNotFoundException(ERR_MESSAGE);
            }
        }
        return BookDto.fromDomainObject(book);
    }

    @Transactional
    @Override
    public Book saveBook(BookDto bookDto) {
        if (bookDto.getId() == 0) {
            Long bookId = bookRepository.getLastInsertedBookId();
            bookRepository.saveBook(bookId, bookDto.getTitle(), bookDto.getAuthor().getId());
            if (bookDto.getGenres() != null && !bookDto.getGenres().isEmpty()) {
                for (Long genreId : bookDto.genreIds()) {
                    bookRepository.addGenreToBook(bookId, genreId);
                }
            }
            return bookRepository.findById(bookId).orElseThrow();
        } else {
            return bookRepository.save(bookDto.toDomainObject());
        }
    }

    @Override
    public void deleteById(long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException(ERR_MESSAGE);
        }
        bookRepository.deleteById(id);
    }
}
