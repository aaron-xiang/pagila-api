package com.example.pagila_api.repository;

import com.example.pagila_api.integration.BaseIntegrationTest;
import com.example.pagila_api.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testCreateAndFindCategory() {
        Category category = new Category();
        category.setName("Test Category");
        Category saved = categoryRepository.save(category);

        assertThat(saved.getCategoryId()).isNotNull();

        Optional<Category> found = categoryRepository.findById(saved.getCategoryId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Category");
    }

    @Test
    void testFindAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).isNotNull();
    }

    @Test
    void testUpdateCategory() {
        Category category = new Category();
        category.setName("Old Name");
        Category saved = categoryRepository.save(category);

        saved.setName("New Name");
        Category updated = categoryRepository.save(saved);

        assertThat(updated.getName()).isEqualTo("New Name");
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category();
        category.setName("To Delete");
        Category saved = categoryRepository.save(category);

        categoryRepository.deleteById(saved.getCategoryId());
        Optional<Category> deleted = categoryRepository.findById(saved.getCategoryId());
        assertThat(deleted).isEmpty();
    }
}