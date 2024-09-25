package com.example.curso_api_rest_java.services;

import com.example.curso_api_rest_java.exceptions.ResourceNotFoundException;
import com.example.curso_api_rest_java.model.Person;
import com.example.curso_api_rest_java.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {

    @Autowired
    PersonRepository repository;
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public List<Person> findAll() {
        logger.info("finding all person...");

        return repository.findAll();
    }

    public Person findById(Long id) {

        logger.info("finding one person...");


        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));
    }

    public Person create(Person person) {

        logger.info("create one person...");

        return repository.save(person);
    }

    public Person update(Long id, Person person) {

        logger.info("update one person...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        // Salva a entidade atualizada
        return repository.save(entity);
    }

    public void delete(Long id) {

        logger.info("delete one person...");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        repository.delete(entity);
    }


}
