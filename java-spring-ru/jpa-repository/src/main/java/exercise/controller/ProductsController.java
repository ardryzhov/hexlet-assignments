package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping()
    public List<Product> getProductsWithOptions(@RequestParam(defaultValue = Integer.MIN_VALUE + "", required = false) Integer min,
                                                @RequestParam(defaultValue = Integer.MIN_VALUE + "", required = false) Integer max) {
        List<Product> products;

        if (min >= 0 && max >= 0) {
            products = productRepository.findByPriceBetween(min, max);
        } else if (min >= 0 && max < 0) {
            products = productRepository.findByPriceGreaterThanEqual(min);
        } else if (max >= 0 && min < 0) {
            products = productRepository.findByPriceLessThanEqual(max);
        } else {
            products = productRepository.findAll();
        }

        products.sort(Comparator.comparing(Product::getPrice));
        return products;
    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
