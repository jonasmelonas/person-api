package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService service;

    public PersonController(PersonService personService) {
        this.service = personService;
    }

    @GetMapping("")
    public List<Person> getAll() {
        return service.getPersons();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByEmail(@PathVariable String email) {
        Person person = service.getByEmail(email);
        if (person == null) {
            return ResponseEntity.ok(Collections.emptyMap());
        } else {
            return ResponseEntity.ok(person);
        }
    }

    @GetMapping("/search")
    public List<Person> getByNameSearch(@RequestParam String name) {
        return service.getByNameSearch(name);
    }
}
