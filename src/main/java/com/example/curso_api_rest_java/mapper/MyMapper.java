package com.example.curso_api_rest_java.mapper;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MyMapper {
    private static ModelMapper mapper = new ModelMapper();

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObject(List<O> originList, Class<D> destination) {
        return originList.stream()
                .map(element -> mapper.map(element, destination))
                .collect(Collectors.toList());

    }
}
