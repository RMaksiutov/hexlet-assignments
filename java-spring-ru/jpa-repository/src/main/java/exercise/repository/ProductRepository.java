package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import exercise.model.Product;

import org.springframework.data.domain.Sort;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    List<Product> findByPriceLessThan(Optional<Integer> price, Sort sort);
    List<Product> findByPriceGreaterThan(Optional<Integer> price, Sort sort);
    List<Product> findByPriceBetween(Optional<Integer> startPrice, Optional<Integer> endPrice, Sort sort);
    // END
}
