package exercise.controller;

import exercise.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;

import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper mapper;

    // BEGIN
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(mapper::map)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO findProduct(@PathVariable Long id) {
        var entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        return mapper.map(entity);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductCreateDTO product) {
        var entity = mapper.map(product);

        productRepository.save(entity);

        return mapper.map(entity);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductUpdateDTO product) {
        var entity = productRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        mapper.update(product, entity);
        productRepository.save(entity);

        return mapper.map(entity);
    }
    // END
}
