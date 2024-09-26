package com.example.curso_api_rest_java.services;

import com.example.curso_api_rest_java.dataVoV1.PersonVO;
import com.example.curso_api_rest_java.exceptions.ResourceNotFoundException;
import com.example.curso_api_rest_java.mapper.MyMapper;
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

    public List<PersonVO> findAll() {
        logger.info("finding all person...");

        return MyMapper.parseListObject(repository.findAll(), PersonVO.class);
    }

    public PersonVO findById(Long id) {

        logger.info("finding one person...");


        var entity =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));
        return MyMapper.parseObject(entity, PersonVO.class);
    }

    public PersonVO create(PersonVO person) {

        logger.info("create one person...");

        var entity = MyMapper.parseObject(person, Person.class);
        var vo = MyMapper.parseObject(repository.save(entity), PersonVO.class);

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

        return vo;
    }

    public void delete(Long id) {

        logger.info("delete one person...");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Records found for this id"));

        repository.delete(entity);
    }


}
