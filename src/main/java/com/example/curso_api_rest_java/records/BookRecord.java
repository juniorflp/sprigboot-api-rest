package com.example.curso_api_rest_java.records;

import org.springframework.hateoas.Link;

import java.util.Date;
import java.util.List;

public record BookRecord(Long id, String title, String author, Date launchDate, Double price) {
}