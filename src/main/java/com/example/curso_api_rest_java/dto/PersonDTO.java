package com.example.curso_api_rest_java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersonDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;

    @JsonProperty("_links")
    private List<Link> links = new ArrayList<>();

    // Construtor padrão
    public PersonDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void addLink(Link link) {
        this.links.add(link);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return Objects.equals(id, personDTO.id) && Objects.equals(firstName, personDTO.firstName) && Objects.equals(lastName, personDTO.lastName) && Objects.equals(address, personDTO.address) && Objects.equals(gender, personDTO.gender) && Objects.equals(links, personDTO.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, address, gender, links);
    }
}
