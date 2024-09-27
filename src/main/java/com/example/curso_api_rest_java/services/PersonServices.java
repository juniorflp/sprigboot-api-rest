package com.example.curso_api_rest_java.services;

import com.example.curso_api_rest_java.controllers.PersonController;
import com.example.curso_api_rest_java.dataVoV1.PersonVO;
import com.example.curso_api_rest_java.exceptions.ResourceNotFoundException;
import com.example.curso_api_rest_java.mapper.MyMapper;
import com.example.curso_api_rest_java.model.Person;
import com.example.curso_api_rest_java.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    @Autowired
    PersonRepository repository;
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public List<PersonVO> findAll() {
        logger.info("finding all person...");

        var persons = MyMapper.parseListObject(repository.findAll(), PersonVO.class);
        persons.stream().forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        return persons;
    }

    public PersonVO findById(Long id) {

        logger.info("finding one person...");


        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));
        var vo = MyMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO person) {

        logger.info("create one person...");

        var entity = MyMapper.parseObject(person, Person.class); // Mapeando DTO para entidade
        entity = repository.save(entity); // Salvando a entidade no banco de dados
        var vo = MyMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(Long id, PersonVO person) {

        logger.info("update one person...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = MyMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;

    }

    public void delete(Long id) {

        logger.info("delete one person...");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        repository.delete(entity);
    }


}
