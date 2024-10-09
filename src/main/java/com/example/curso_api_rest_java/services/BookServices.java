package com.example.curso_api_rest_java.services;

import com.example.curso_api_rest_java.controllers.BookController;
import com.example.curso_api_rest_java.dataVoV1.BookVO;
import com.example.curso_api_rest_java.exceptions.ResourceNotFoundException;
import com.example.curso_api_rest_java.mapper.MyMapper;
import com.example.curso_api_rest_java.model.Book;
import com.example.curso_api_rest_java.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    @Autowired

    BookRepository repository;
    private Logger logger = Logger.getLogger(BookServices.class.getName());

    public List<BookVO> findAll() {
        logger.info("finding all books...");

        var books = MyMapper.parseListObject(repository.findAll(), BookVO.class);
        books.stream().forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
        return books;
    }

    public BookVO findById(Long id) {

        logger.info("finding one book...");


        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));
        var vo = MyMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO book) {

        logger.info("create one book...");

        var entity = MyMapper.parseObject(book, Book.class); // Mapeando DTO para entidade
        entity = repository.save(entity); // Salvando a entidade no banco de dados
        var vo = MyMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(Long id, BookVO book) {

        logger.info("update one book...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());

        var vo = MyMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;

    }

    public void delete(Long id) {

        logger.info("delete one book...");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        repository.delete(entity);
    }


}
