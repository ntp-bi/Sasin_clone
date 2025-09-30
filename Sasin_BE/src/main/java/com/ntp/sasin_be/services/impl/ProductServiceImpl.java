package com.ntp.sasin_be.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntp.sasin_be.dto.ProductDTO;
import com.ntp.sasin_be.entities.*;
import com.ntp.sasin_be.exceptions.ResourceNotFoundException;
import com.ntp.sasin_be.mapper.ProductMapper;
import com.ntp.sasin_be.repositories.CategoryRepository;
import com.ntp.sasin_be.repositories.RestaurantBranchRepository;
import com.ntp.sasin_be.repositories.ProductRepository;
import com.ntp.sasin_be.repositories.SupplierRepository;
import com.ntp.sasin_be.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final RestaurantBranchRepository branchRepository;

    private final LocalUploadServiceImpl localUploadService;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toDto);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id).map(productMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<ProductDTO> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO, MultipartFile[] files) throws JsonProcessingException {
        boolean existsByTitle = productRepository.existsByTitle(productDTO.getTitle());

        if (existsByTitle) {
            throw new IllegalArgumentException("Title already exists");
        }

        Optional<Product> existsBySku = productRepository.findBySku(productDTO.getSku());
        if (existsBySku.isPresent()) {
            throw new IllegalArgumentException("Sku already exists");
        }

        Product product = productMapper.toEntity(productDTO);

        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDTO.getCategoryId()));

            product.setCategory(category);
        }

        if (productDTO.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + productDTO.getSupplierId()));

            product.setSupplier(supplier);
        }

        if (productDTO.getFranchiseId() != null) {
            RestaurantBranch branch = branchRepository.findById(productDTO.getFranchiseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Franchise not found: " + productDTO.getFranchiseId()));

            product.setFranchise(branch);
        }

        // Upload nhiều ảnh
        if (files != null && files.length > 0) {
            List<String> fileNames = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String fileName = localUploadService.uploadFile(file);
                    fileNames.add(fileName);
                }
            }

            if (!fileNames.isEmpty()) {
                // ảnh chính là ảnh đầu tiên
                product.setImage01(fileNames.get(0));

                // ảnh phụ (nếu có)
                if (fileNames.size() > 1) {
                    product.setImage02(fileNames.get(1));
                }

                // lưu toàn bộ danh sách vào imagesJson dạng JSON string
                product.setImagesJson(new ObjectMapper().writeValueAsString(fileNames));
            }
        }

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO, MultipartFile[] files) throws JsonProcessingException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setSku(productDTO.getSku());
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setOriginalPrice(productDTO.getOriginalPrice());
        product.setStock(productDTO.getStock());
        product.setIsNew(productDTO.getIsNew());
        product.setIsHot(productDTO.getIsHot());
        product.setIsAvailable(productDTO.getIsAvailable());
        product.setUnit(productDTO.getUnit());

        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDTO.getCategoryId()));

            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        if (productDTO.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + productDTO.getSupplierId()));

            product.setSupplier(supplier);
        } else {
            product.setSupplier(null);
        }

        if (productDTO.getFranchiseId() != null) {
            RestaurantBranch branch = branchRepository.findById(productDTO.getFranchiseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Franchise not found: " + productDTO.getFranchiseId()));

            product.setFranchise(branch);
        } else {
            product.setFranchise(null);
        }

        if (files != null && files.length > 0) {
            List<String> fileNames = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String fileName = localUploadService.uploadFile(file);
                    fileNames.add(fileName);
                }
            }

            if (!fileNames.isEmpty()) {
                product.setImage01(fileNames.get(0));
                if (fileNames.size() > 1) {
                    product.setImage02(fileNames.get(1));
                }
                product.setImagesJson(new ObjectMapper().writeValueAsString(fileNames));
            }
        }

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (product != null) {
            productRepository.delete(product);
        }
    }

    @Override
    public List<ProductDTO> search(String keyword) {
        return productRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
}
