package edu.school21.repositories;

import edu.school21.models.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class ProductsRepositoryJdbcImpl implements ProductsRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProductsRepositoryJdbcImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<Product> productRowMapper = (rs, rowNum) ->
            new Product(rs.getLong("identifier"), rs.getString("name"), rs.getBigDecimal("price"));

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM electronic.product", productRowMapper);
    }

    @Override
    public Optional<Product> findById(Long id) {
        List<Product> products = jdbcTemplate.query(
                "SELECT * FROM electronic.product WHERE identifier = ?", productRowMapper, id);
        return products.stream().findFirst();
    }

    @Override
    public void update(Product product) {
        jdbcTemplate.update(
                "UPDATE electronic.product SET name = ?, price = ? WHERE identifier = ?",
                product.getName(), product.getPrice(), product.getIdentifier());
    }

    @Override
    public void save(Product product) {
        jdbcTemplate.update(
                "INSERT INTO electronic.product (identifier, name, price) VALUES (?, ?, ?)",
                product.getIdentifier(), product.getName(), product.getPrice());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM electronic.product WHERE identifier = ?", id);
    }
}