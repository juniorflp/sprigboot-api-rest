package com.example.curso_api_rest_java.services;

import com.example.curso_api_rest_java.controllers.PersonController;
import com.example.curso_api_rest_java.dto.PersonDTO;
import com.example.curso_api_rest_java.exceptions.ResourceNotFoundException;
import com.example.curso_api_rest_java.model.Person;
import com.example.curso_api_rest_java.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class PersonServices {

    @Autowired
    PersonRepository repository;

    public List<PersonDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toPersonDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        return toPersonDTO(entity);
    }

    public PersonDTO create(PersonDTO personDTO) {
        var entity = toPersonEntity(personDTO);
        entity = repository.save(entity);

        return toPersonDTO(entity);
    }

    public PersonDTO update(Long id, PersonDTO personDTO) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        entity.setFirstName(personDTO.getFirstName());
        entity.setLastName(personDTO.getLastName());
        entity.setAddress(personDTO.getAddress());
        entity.setGender(personDTO.getGender());

        entity = repository.save(entity);
        return toPersonDTO(entity);
    }

    public void delete(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        repository.delete(entity);
    }

    // Método auxiliar para converter Person em PersonDTO e adicionar links
    private PersonDTO toPersonDTO(Person person) {
        PersonDTO dto = new PersonDTO();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getGender());

        // Adiciona links HATEOAS
        dto.addLink(linkTo(methodOn(PersonController.class).findById(person.getId())).withSelfRel());


        return dto;
    }

    // Método auxiliar para converter PersonDTO em Person
    private Person toPersonEntity(PersonDTO personDTO) {
        Person person = new Person();
        // Não definir o ID ao criar uma nova pessoa, pois ele é gerado automaticamente
        if (personDTO.getId() != null) {
            person.setId(personDTO.getId());
        }
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setAddress(personDTO.getAddress());
        person.setGender(personDTO.getGender());
        return person;
    }
}
