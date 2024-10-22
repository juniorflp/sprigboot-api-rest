package com.example.curso_api_rest_java.controllers;

import com.example.curso_api_rest_java.dto.BookDTO;
import com.example.curso_api_rest_java.exceptions.ResourceNotFoundException;
import com.example.curso_api_rest_java.services.BookServices;
import com.example.curso_api_rest_java.services.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/book/v1")
@Tag(name = "Book", description = "Endpoints for Managing Books")
public class BookController {

    @Autowired
    private BookServices service;

    @Autowired
    private ImageService imageService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<BookDTO>>findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search", required=false) String search
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<BookDTO> booksPage = service.findAll(search, pageable);

        return ResponseEntity.ok(booksPage);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookDTO findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookDTO create(@RequestBody BookDTO book) {
        return service.create(book);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookDTO update(@PathVariable(value = "id") Long id, @RequestBody BookDTO book) {
        return service.update(id, book);
    }

    @PatchMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDTO> patchBook(@PathVariable Long id,@RequestBody Map<String, Object> updates ){
        try{
            BookDTO updatedBook = service.updatePartial(id, updates);
            return ResponseEntity.ok(updatedBook);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }




    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDTO> uploadOrUpdateImage(@PathVariable Long id, @RequestParam("image") MultipartFile image){
        try{
            String imagePath = imageService.saveImage(image);
            BookDTO updatedBook = service.updateImageUrl(id, imagePath);

            return  ResponseEntity.ok(updatedBook);

        }catch(IOException e){
            return ResponseEntity.status(500).build();

        }
    }

}
