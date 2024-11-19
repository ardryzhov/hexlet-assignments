package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.repository.AuthorRepository;
import exercise.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {
    // BEGIN

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private AuthorMapper authorMapper;



    public List<AuthorDTO> findAll() {
        var entity = authorRepository.findAll();

        return entity.stream()
                .map(authorMapper::map)
                .toList();
    }

    public AuthorDTO findById(Long id) {
        var entity = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        return authorMapper.map(entity);
    }

    public AuthorDTO createAuthor(AuthorCreateDTO dto) {
        var entity = authorMapper.map(dto);
        authorRepository.save(entity);
        return authorMapper.map(entity);
    }

    public AuthorDTO updateAuthor(AuthorUpdateDTO authorUpdateDTO, Long id) {
        var entity = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        authorMapper.update(authorUpdateDTO, entity);
        authorRepository.save(entity);

        return authorMapper.map(entity);
    }

    public AuthorDTO deleteAuthor(Long id) {
        var entity = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        authorRepository.delete(entity);

        return authorMapper.map(entity);
    }
    // END
}
