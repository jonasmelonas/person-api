package com.example.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class PersonService {
    private List<Person> persons = new ArrayList<>();
    private Map<String, Person> personsMap = new HashMap<>(); // for instant lookup på eposter som key

    @PostConstruct
    public void loadDataFromFile() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("export.csv"))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                // skipper headeren
                if (header) {
                    header = false;
                    continue;
                }

                String[] parts = line.split(",");
                Person p = new Person();
                p.setFirstName(parts[0]);
                p.setLastName(parts[1]);
                p.setEmail(parts[2]);
                p.setSalary(parts[3]);
                p.setAge(Integer.parseInt(parts[4]));

                // lagrer den nye personen i liste
                persons.add(p);

                // lagrer den nye personen også i map'et med epost som key
                personsMap.put(p.getEmail(), p);
            }
        }
    }

    public List<Person> getPersons() {
        return this.persons;
    }

    public Person getByEmail(String email) {
        if (this.personsMap.containsKey(email)) {
            return this.personsMap.get(email);
        } else {
            return null;
        }
    }

    public List<Person> getByNameSearch(String search) {
        List<Person> results = new ArrayList<>();

        for (Person p : this.persons) {
            /* setter sammen fornavn og etternavn til en samlet streng som søket skal gjøres
            på */
            StringBuilder fullName = new StringBuilder();
            fullName.append(p.getFirstName()).append(" ").append(p.getLastName());

            if (fullName.toString().toLowerCase().contains(search.toLowerCase())) {
                results.add(p);
            }
        }
        return results;
    }

    public void createPerson(Person person) {
        this.persons.add(person);
        this.personsMap.put(person.getEmail(), person);
        // eventuelt oppdatere csv-fil
    }

    public Person updatePerson(String email, Person person) {
        if(this.personsMap.containsKey(email)) {

            //må finne person i arraylisten og erstatte den
            for(Person p : persons) {
                if(p.getEmail().equals(email)) {
                    persons.remove(personsMap.get(email));
                    persons.add(person);
                    break;
                }
            }
            //oppdaterer map'et og returnerer den nye versjonen
            this.personsMap.remove(email);
            this.personsMap.put(person.getEmail(), person);
            return person;
        }
        return null;
    }

    public Person deletePerson(String email) {
        if(this.personsMap.containsKey(email)) {

            //må finne person i arraylisten og slette den
            for(Person p : persons) {
                if(p.getEmail().equals(email)) {
                    persons.remove(personsMap.get(email));
                    break;
                }
            }
            //sletter fra map'et og returnerer
            return this.personsMap.remove(email);
        }
        return null;
    }
}
