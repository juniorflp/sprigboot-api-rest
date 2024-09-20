package com.example.curso_api_rest_java.services;

import com.example.curso_api_rest_java.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public List<Person> findAll() {
        logger.info("finding all person...");
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            personList.add(mockPerson(i));
        }
        return personList;
    }

    public Person findById(String id) {

        logger.info("finding one person...");

        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Leandro");
        person.setLastName("Santos");
        person.setAddress("rua sao miguel");
        person.setGender("masculino");
        return person;
    }

    public Person create(Person person) {

        logger.info("create one person...");

        return person;
    }

    public Person update(Person person) {

        logger.info("update one person...");

        return person;
    }

    public void delete(String id) {

        logger.info("delete one person...");

    }

    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Leandro" + i);
        person.setLastName("Santos" + i);
        person.setAddress("rua sao miguel" + i);
        person.setGender("masculino" + i);

        return person;
    }


}
