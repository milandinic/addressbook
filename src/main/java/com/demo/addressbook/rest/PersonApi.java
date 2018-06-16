package com.demo.addressbook.rest;

import com.demo.addressbook.model.Person;
import com.demo.addressbook.reepository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonApi {

    @Autowired
    private PersonRepository personRepository;

    @PostMapping
    public Person create(@RequestBody Person person) {
        return personRepository.save(person);
    }

    // GET api/persons?query=1234
    @GetMapping
    public List<Person> getPersons(@RequestParam String query) {
        if (query == null || query.isEmpty()) {
            return personRepository.findAll();
        }

        return personRepository.findByFirstName(query);
    }

}
