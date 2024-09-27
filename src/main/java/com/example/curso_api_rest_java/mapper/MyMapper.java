package com.example.curso_api_rest_java.mapper;

import com.example.curso_api_rest_java.dataVoV1.PersonVO;
import com.example.curso_api_rest_java.model.Person;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class MyMapper {
    private static final ModelMapper mapper = new ModelMapper();

    static {
        // change key for id
        mapper.addMappings(new PropertyMap<PersonVO, Person>() {
            @Override
            protected void configure() {
                map().setId(source.getKey());
            }
        });

        // change id for key
        mapper.addMappings(new PropertyMap<Person, PersonVO>() {
            @Override
            protected void configure() {
                map().setKey(source.getId());
            }
        });
    }

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObject(List<O> originList, Class<D> destination) {
        return originList.stream()
                .map(element -> mapper.map(element, destination))
                .collect(Collectors.toList());

    }
}
