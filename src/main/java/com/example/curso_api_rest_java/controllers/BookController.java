package com.example.curso_api_rest_java.controllers;

import com.example.curso_api_rest_java.dataVoV1.BookVO;
import com.example.curso_api_rest_java.services.BookServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/book/v1")
@Tag(name = "Book", description = "Endpoints for Managing book")
public class BookController {

    @Autowired
    private BookServices service;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookVO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BookVO findById(
            @PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookVO create(@RequestBody BookVO book) {
        return service.create(book);
    }


    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookVO update(@PathVariable(value = "id") Long id, @RequestBody BookVO book) {
        return service.update(id, book);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
