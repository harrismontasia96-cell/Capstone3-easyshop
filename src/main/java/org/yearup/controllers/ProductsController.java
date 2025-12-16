package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductsController {

    private ProductDao productDao;

    @Autowired
    public ProductsController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Product> search(
            @RequestParam(name = "cat", required = false) Integer categoryId,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "subCategory", required = false) String subCategory
    ) {
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minPrice cannot be greater than maxPrice");
        }
        return productDao.search(categoryId, minPrice, maxPrice, subCategory);
    }

    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Product getById(@PathVariable int id) {
        Product product = productDao.getById(id);
        if (product == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return product;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody Product product) {
        try {
            return productDao.create(product);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to create product",
                    ex
            );
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product updateProduct(@PathVariable int id, @RequestBody Product product) {
        Product existing = productDao.getById(id);
        if (existing == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        product.setProductId(id);
        productDao.update(id, product);
        return productDao.getById(id);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable int id) {
        Product product = productDao.getById(id);
        if (product == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        productDao.delete(id);
    }
}