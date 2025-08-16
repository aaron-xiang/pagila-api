package com.example.pagila_api.service;

import com.example.pagila_api.exception.ResourceNotFoundException;
import com.example.pagila_api.integration.BaseIntegrationTest;
import com.example.pagila_api.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryServiceTest extends BaseIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void testCreateAndGetCategory() {
        Category category = new Category();
        category.setName("Service Category");
        Category created = categoryService.createCategory(category);

        assertThat(created.getCategoryId()).isNotNull();

        Category found = categoryService.getCategoryById(created.getCategoryId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Service Category");
    }

    @Test
    void testGetAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        assertThat(categories).isNotNull();
    }

    @Test
    void testUpdateCategory() {
        Category category = new Category();
        category.setName("Update Me");
        Category created = categoryService.createCategory(category);

        Category updateData = new Category();
        updateData.setName("Updated Name");

        Category updated = categoryService.updateCategory(created.getCategoryId(), updateData);

        assertThat(updated.getName()).isEqualTo("Updated Name");
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category();
        category.setName("Delete Me");
        Category created = categoryService.createCategory(category);

        categoryService.deleteCategory(created.getCategoryId());

        assertThatThrownBy(() -> categoryService.getCategoryById(created.getCategoryId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with ID: " + created.getCategoryId());
    }

    @Test
    void testGetNonExistentCategory() {
        assertThatThrownBy(() -> categoryService.getCategoryById(99999))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with ID: 99999");
    }
}