package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.List;

public interface BookService {

    public List<BookDto> findAllBooks();

    public BookDto findBookById(long id);

    public Book saveBook(BookDto bookDto);

    public void deleteById(long id);
}
