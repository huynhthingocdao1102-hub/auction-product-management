package com.auction.service;

import com.auction.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IProductService {
    Page<Product> searchAndPaginate(String name, Double price, Integer typeId, Pageable pageable);
    void save(Product product);
    void deleteByIds(List<Integer> ids);
    Product findById(Integer id);
    void validateProduct(Product product);
    void update(Product product);
}