package com.auction.service.impl;

import com.auction.entity.Product;
import com.auction.repository.ProductRepository;
import com.auction.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> searchAndPaginate(String name, Double price, Integer typeId, Pageable pageable) {
        String searchName = (name == null || name.trim().isEmpty()) ? null : name.trim();
        Integer searchTypeId = (typeId == null || typeId == 0) ? null : typeId;
        return productRepository.searchProducts(searchName, price, searchTypeId, pageable);
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Product product) {
        validateProduct(product);
        product.setStatus("Chờ duyệt");
        productRepository.save(product);
    }

    @Override
    public void update(Product product) {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Không tìm thấy ID sản phẩm cần cập nhật!");
        }

        Product existingProduct = productRepository.findById(product.getId()).orElse(null);
        if (existingProduct == null) {
            throw new IllegalArgumentException("Không thể cập nhật! Sản phẩm có ID " + product.getId() + " không tồn tại.");
        }

        validateProduct(product);

        if (product.getStatus() == null || product.getStatus().isEmpty()) {
            product.setStatus(existingProduct.getStatus());
        }

        productRepository.save(product);
    }

    @Override
    public void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty() ||
                product.getPrice() == null ||
                product.getProductType() == null || product.getProductType().getId() == null) {
            throw new IllegalArgumentException("Tất cả các ô dữ liệu dấu * không được để trống!");
        }

        String trimmedName = product.getName().trim();
        if (trimmedName.length() < 5 || trimmedName.length() > 50) {
            throw new IllegalArgumentException("Tên sản phẩm phải nằm trong khoảng từ 5 đến 50 ký tự!");
        }

        if (product.getPrice() < 100000.0) {
            throw new IllegalArgumentException("Giá bắt đầu phải là số và thấp nhất phải từ 100.000 VND!");
        }

        if (product.getPrice() > 999999999.0) {
            throw new IllegalArgumentException("Giá bắt đầu không được vượt quá 999.999.999 VND để đảm bảo an toàn!");
        }
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ít nhất một sản phẩm để xóa!");
        }

        try {
            productRepository.deleteAllById(ids);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Không thể xóa! Một hoặc nhiều sản phẩm đã chọn đang có dữ liệu liên kết ở bảng khác.");
        }
    }
}