package com.auction.service;

import com.auction.entity.ProductType;

import java.util.List;

public interface IProductTypeService {
    List<ProductType> findAll();
}