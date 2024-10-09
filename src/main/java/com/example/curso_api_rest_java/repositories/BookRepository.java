package com.example.curso_api_rest_java.repositories;

import com.example.curso_api_rest_java.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {}

