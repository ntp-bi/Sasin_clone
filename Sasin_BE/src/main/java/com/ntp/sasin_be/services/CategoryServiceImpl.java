package com.ntp.sasin_be.services;

import com.ntp.sasin_be.dto.CategoryDTO;
import com.ntp.sasin_be.entities.Category;
import com.ntp.sasin_be.exceptions.ResourceNotFoundException;
import com.ntp.sasin_be.mapper.CategoryMapper;
import com.ntp.sasin_be.repositories.CategoryRepository;
import com.ntp.sasin_be.services.impl.ICategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDTO);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    }

    @Override
    public CategoryDTO getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug).map(categoryMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug " + slug));
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        boolean exists = categoryRepository.existsByName(categoryDTO.getName());

        if (exists) {
            throw new IllegalArgumentException("Category name already exists");
        }

        Category category = categoryMapper.toEntity(categoryDTO);

        if (categoryDTO.getParentId() != null) {
            Category parent = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryDTO.getParentId()));

            category.setParent(parent);
        }

        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));

        category.setName(categoryDTO.getName());
        category.setSlug(categoryDTO.getSlug());
        category.setDescription(categoryDTO.getDescription());

        if (categoryDTO.getParentId() != null) {
            if (categoryDTO.getParentId().equals(id)) {
                throw new IllegalArgumentException("Category cannot be parent of itself");
            }

            Category parent = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryDTO.getParentId()));

            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
        categoryRepository.delete(category);
    }
}
