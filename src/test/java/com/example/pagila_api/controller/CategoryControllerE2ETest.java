package com.example.pagila_api.controller;

import com.example.pagila_api.integration.BaseIntegrationTest;
import com.example.pagila_api.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryControllerE2ETest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCategoryCrudLifecycle() {
        // CREATE
        Category category = new Category();
        category.setName("E2E Category");

        ResponseEntity<Category> createResponse = restTemplate.postForEntity("/api/categories", category, Category.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Category created = createResponse.getBody();
        assertThat(created).isNotNull();
        Integer categoryId = created.getCategoryId();

        // READ (by ID)
        ResponseEntity<Category> getResponse = restTemplate.getForEntity("/api/categories/" + categoryId, Category.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Category found = getResponse.getBody();
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("E2E Category");

        // UPDATE
        found.setName("Updated E2E Category");
        HttpEntity<Category> updateEntity = new HttpEntity<>(found);
        ResponseEntity<Category> updateResponse = restTemplate.exchange(
                "/api/categories/" + categoryId, HttpMethod.PUT, updateEntity, Category.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Category updated = updateResponse.getBody();
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("Updated E2E Category");

        // READ ALL
        ResponseEntity<Category[]> allResponse = restTemplate.getForEntity("/api/categories", Category[].class);
        assertThat(allResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allResponse.getBody()).isNotNull();
        assertThat(allResponse.getBody().length).isGreaterThanOrEqualTo(1);

        // DELETE
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/categories/" + categoryId, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // VERIFY DELETION
        try {
            restTemplate.getForEntity("/api/categories/" + categoryId, Category.class);
            assertThat(false).isTrue();
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}