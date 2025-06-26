package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


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
    public Person getByEmail(@PathVariable String email) {
        Person person = service.getByEmail(email);
        if (person == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
        return person;
    }

    @GetMapping("/search")
    public List<Person> getByNameSearch(@RequestParam String name) {
        return service.getByNameSearch(name);
    }

    @PostMapping("")
    public Person createPerson(@RequestBody Person person) {

        // gjør enkle valideringer av dataen
        if (person.getFirstName() == null || person.getFirstName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
        }
        if (person.getLastName() == null || person.getLastName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required");
        }
        if (person.getEmail() == null || person.getEmail().isBlank() || !person.getEmail().contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A correct email is required");
        }
        if (person.getSalary() == null || person.getSalary().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary is required");
        }

        service.createPerson(person);
        return person;
    }

    @PutMapping("/email/{email}")
    public Person updatePerson(@PathVariable String email, @RequestBody Person person) {

        // gjør enkle valideringer av dataen
        if (person.getFirstName() == null || person.getFirstName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
        }
        if (person.getLastName() == null || person.getLastName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required");
        }
        if (person.getEmail() == null || person.getEmail().isBlank() || !person.getEmail().contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A correct email is required");
        }
        if (person.getSalary() == null || person.getSalary().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary is required");
        }
        
        Person updatedPerson = service.updatePerson(email, person);
        if(updatedPerson == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
        return updatedPerson;
    }

    @DeleteMapping("/email/{email}")
    public Person deletePerson(@PathVariable String email) {
        Person person = service.deletePerson(email);
        if(person == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
        return person;
    }
}
