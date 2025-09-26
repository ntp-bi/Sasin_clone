package com.ntp.sasin_be.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ntp.sasin_be.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    Page<ProductDTO> getAllProducts(Pageable pageable);

    ProductDTO getProductById(Long id);

    List<ProductDTO> getProductsByCategoryId(Long categoryId);

    ProductDTO createProduct(ProductDTO productDTO, MultipartFile[] files) throws JsonProcessingException;

    ProductDTO updateProduct(Long id, ProductDTO productDTO, MultipartFile[] files) throws JsonProcessingException;

    void deleteProduct(Long id);

    List<ProductDTO> search(String keyword);
}
