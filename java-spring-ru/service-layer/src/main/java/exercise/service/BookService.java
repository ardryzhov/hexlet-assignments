package exercise.service;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.BookMapper;
import exercise.repository.AuthorRepository;
import exercise.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {
    // BEGIN
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private AuthorRepository authorRepository;

    public List<BookDTO> findAll() {
        var entity = bookRepository.findAll();
        return entity.stream()
                .map(bookMapper::map)
                .toList();
    }

    public BookDTO findById(Long id) {
        var bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        var authorEntity = authorRepository.findById(bookEntity.getAuthor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        bookEntity.setAuthor(authorEntity);

        return bookMapper.map(bookEntity);
    }

    public BookDTO createBook(BookCreateDTO bookCreateDTO) {
        authorRepository.findById(bookCreateDTO.getAuthorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author not found"));
        var entity = bookMapper.map(bookCreateDTO);
        bookRepository.save(entity);

        return bookMapper.map(entity);
    }

    public BookDTO updateBook(Long id, BookUpdateDTO bookUpdateDTO) {
        var authorEntity = authorRepository.findById(bookUpdateDTO.getAuthorId().get())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        var bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        bookEntity.setAuthor(authorEntity);
        bookMapper.update(bookUpdateDTO, bookEntity);
        bookRepository.save(bookEntity);

        return bookMapper.map(bookEntity);
    }

    public BookDTO deleteBook(Long id) {
        var entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        bookRepository.delete(entity);

        return bookMapper.map(entity);
    }

    // END
}
