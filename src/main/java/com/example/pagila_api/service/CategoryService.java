package com.example.pagila_api.service;

import com.example.pagila_api.exception.ResourceNotFoundException;
import com.example.pagila_api.model.Category;
import com.example.pagila_api.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    }

    public Category getCategoryByIdWithFilms(Integer id) {
        return categoryRepository.findByIdWithFilms(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
    }

    @Transactional
    public Category createCategory(Category category) {
        // Check if category with same name already exists
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Integer id, Category updatedCategory) {
        Category existingCategory = getCategoryById(id);

        // Check if the new name conflicts with existing categories (excluding current one)
        if (!existingCategory.getName().equalsIgnoreCase(updatedCategory.getName()) &&
                categoryRepository.existsByNameIgnoreCase(updatedCategory.getName())) {
            throw new IllegalArgumentException("Category with name '" + updatedCategory.getName() + "' already exists");
        }

        existingCategory.setName(updatedCategory.getName());
        return categoryRepository.save(existingCategory);
    }

    @Transactional
    public void deleteCategory(Integer id) {
        Category category = getCategoryById(id);

        // Check if category has associated films
        if (category.getFilms() != null && !category.getFilms().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated films. Remove film associations first.");
        }

        categoryRepository.deleteById(id);
    }

    public Page<Category> searchCategoriesByName(String name, Pageable pageable) {
        return categoryRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public List<Category> getCategoriesWithMoreThanXFilms(int filmCount) {
        return categoryRepository.findCategoriesWithMoreThanXFilms(filmCount);
    }

    public List<Category> getCategoriesOrderedByFilmCount() {
        return categoryRepository.findCategoriesOrderByFilmCount();
    }
}