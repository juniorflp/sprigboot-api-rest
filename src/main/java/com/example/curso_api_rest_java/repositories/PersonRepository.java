package com.example.curso_api_rest_java.repositories;

import com.example.curso_api_rest_java.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {}

