package edu.school21.repositories;

import edu.school21.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;


public class ProductsRepositoryJdbcImplTest {
    private ProductsRepository repository;
    private EmbeddedDatabase db;

    final List<Product> EXPECTED_FIND_ALL_PRODUCTS = List.of(
            new Product(1L, "Laptop", new BigDecimal("1200.00")),
            new Product(2L, "Smartphone", new BigDecimal("800.00")),
            new Product(3L, "Tablet", new BigDecimal("600.00")),
            new Product(4L, "Monitor", new BigDecimal("300.00")),
            new Product(5L, "Keyboard", new BigDecimal("100.00"))
    );
    final Product EXPECTED_FIND_BY_ID_PRODUCT = new Product(1L, "Laptop", new BigDecimal("1200.00"));
    final Product EXPECTED_UPDATED_PRODUCT = new Product(1L, "Updated Laptop", new BigDecimal("1100.00"));

    @BeforeEach
    void init() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        repository = new ProductsRepositoryJdbcImpl(db);
    }

    @Test
    void testFindAll() {
        List<Product> products = repository.findAll();
        assertEquals(EXPECTED_FIND_ALL_PRODUCTS, products);
    }

    @Test
    void testFindById() {
        Optional<Product> product = repository.findById(1L);
        assertTrue(product.isPresent());
        assertEquals(EXPECTED_FIND_BY_ID_PRODUCT, product.get());
    }

    @Test
    void testUpdate() {
        repository.update(EXPECTED_UPDATED_PRODUCT);
        Optional<Product> updatedProduct = repository.findById(1L);
        assertTrue(updatedProduct.isPresent());
        assertEquals(EXPECTED_UPDATED_PRODUCT, updatedProduct.get());
    }

    @Test
    void testSave() {
        Product newProduct = new Product(6L, "Mouse", new BigDecimal("50.00"));
        repository.save(newProduct);
        Optional<Product> savedProduct = repository.findById(6L);
        assertTrue(savedProduct.isPresent());
        assertEquals(newProduct, savedProduct.get());
    }

    @Test
    void testDelete() {
        repository.delete(5L);
        Optional<Product> deletedProduct = repository.findById(5L);
        assertFalse(deletedProduct.isPresent());
    }
}