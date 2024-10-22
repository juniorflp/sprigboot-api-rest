package com.example.curso_api_rest_java.services;

import com.example.curso_api_rest_java.controllers.BookController;
import com.example.curso_api_rest_java.dto.BookDTO;
import com.example.curso_api_rest_java.exceptions.ResourceNotFoundException;
import com.example.curso_api_rest_java.model.Book;
import com.example.curso_api_rest_java.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    @Autowired
    BookRepository repository;

    public Page<BookDTO> findAll(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return repository.findByTitleContainingIgnoreCase(search, pageable)
                    .map(this::toBookDTO);
        } else {
            return repository.findAll(pageable)
                    .map(this::toBookDTO);
        }
    }

    public BookDTO findById(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        return toBookDTO(entity);
    }

    public BookDTO create(BookDTO bookDTO) {
        var entity = toBookEntity(bookDTO);
        entity = repository.save(entity);

        return toBookDTO(entity);
    }

    public BookDTO update(Long id, BookDTO bookDTO) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        entity.setTitle(bookDTO.getTitle());
        entity.setAuthor(bookDTO.getAuthor());
        entity.setLaunchDate(bookDTO.getLaunchDate());
        entity.setPrice(bookDTO.getPrice());

        entity = repository.save(entity);
        return toBookDTO(entity);
    }

    public void delete(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        repository.delete(entity);
    }


    public BookDTO updateImageUrl(Long id, String imagePath) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        entity.setImageUrl(imagePath);
        entity = repository.save(entity);

        return toBookDTO(entity);
    }

    public BookDTO updatePartial(Long id, Map<String, Object> updates) {
        Book existingBook = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Book.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, existingBook, value);
            } else {
                throw new IllegalArgumentException("Update not allowed for field: " + key);
            }
        });

        Book updatedBook = repository.save(existingBook);

        return toBookDTO(updatedBook);

    }


    // Método auxiliar para converter Book em BookDTO e adicionar links
    private BookDTO toBookDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setLaunchDate(book.getLaunchDate());
        dto.setPrice(book.getPrice());
        dto.setImageUrl(book.getImageUrl());

        // Adiciona links HATEOAS
        dto.addLink(linkTo(methodOn(BookController.class).findById(book.getId())).withSelfRel());


        return dto;
    }

    // Método auxiliar para converter BookDTO em Book
    private Book toBookEntity(BookDTO bookDTO) {
        Book book = new Book();
        // Não definir o ID ao criar um novo livro, pois ele é gerado automaticamente
        if (bookDTO.getId() != null) {
            book.setId(bookDTO.getId());
        }
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setLaunchDate(bookDTO.getLaunchDate());
        book.setPrice(bookDTO.getPrice());
        book.setImageUrl(bookDTO.getImageUrl());
        return book;
    }
}
