package com.example.pagila_api.controller;

import com.example.pagila_api.model.Category;
import com.example.pagila_api.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryService.getAllCategories(pageable);
    }

    @GetMapping("/all")
    public List<Category> getAllCategoriesAsList() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/{id}/with-films")
    public Category getCategoryByIdWithFilms(@PathVariable Integer id) {
        return categoryService.getCategoryByIdWithFilms(id);
    }

    @GetMapping("/by-name/{name}")
    public Category getCategoryByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(201).body(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @Valid @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public Page<Category> searchCategories(@RequestParam String name, Pageable pageable) {
        return categoryService.searchCategoriesByName(name, pageable);
    }

    @GetMapping("/with-films")
    public List<Category> getCategoriesWithMoreThanXFilms(@RequestParam(defaultValue = "0") int filmCount) {
        return categoryService.getCategoriesWithMoreThanXFilms(filmCount);
    }

    @GetMapping("/ordered-by-film-count")
    public List<Category> getCategoriesOrderedByFilmCount() {
        return categoryService.getCategoriesOrderedByFilmCount();
    }
}