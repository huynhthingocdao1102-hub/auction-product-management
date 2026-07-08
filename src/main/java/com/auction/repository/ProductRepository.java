package com.auction.repository;

import com.auction.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR p.name LIKE %:name%) AND " +
            "(:price IS NULL OR p.price >= :price) AND " +
            "(:typeId IS NULL OR p.productType.id = :typeId)")
    Page<Product> searchProducts(@Param("name") String name,
                                 @Param("price") Double price,
                                 @Param("typeId") Integer typeId,
                                 Pageable pageable);
}
