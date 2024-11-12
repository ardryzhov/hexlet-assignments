package exercise.controller;

import java.util.List;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.mapper.ProductMapper;
import exercise.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;

    // BEGIN
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> index() {
        return productRepository.findAll().stream()
                .map(productMapper::map)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO show(@PathVariable Long id) {
        var entityProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productMapper.map(entityProduct);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody @Valid ProductCreateDTO productCreateDTO) {
        var category = categoryRepository.findById(productCreateDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        var entity = productMapper.map(productCreateDTO);
        entity.setCategory(category);
        productRepository.save(entity);

        return productMapper.map(entity);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO update(@PathVariable Long id, @RequestBody @Valid ProductUpdateDTO productUpdateDTO) {
        var productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        var categoryEntity = categoryRepository.findById(productUpdateDTO.getCategoryId().get())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        productEntity.setCategory(categoryEntity);
        productMapper.update(productUpdateDTO, productEntity);
        productRepository.save(productEntity);

        return productMapper.map(productEntity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ProductDTO delete(@PathVariable Long id) {
        var entity = productRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.deleteById(id);
        return productMapper.map(entity);
    }
    // END
}
