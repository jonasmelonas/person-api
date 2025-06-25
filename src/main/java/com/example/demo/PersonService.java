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
    public void loadData() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("export.csv"))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
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

                System.out.println(p.toString());

                // lagrer den nye personen i liste
                persons.add(p);

                // lagerer den nye personen også i map'et med epost som key
                personsMap.put(p.getEmail(), p);
            }
        }
    }

    public List<Person> getPersons() {
        System.out.println(this.persons);
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
            StringBuilder fullName = new StringBuilder();
            fullName.append(p.getFirstName()).append(" ").append(p.getLastName());

            if (fullName.toString().toLowerCase().contains(search.toLowerCase())) {
                results.add(p);
            }
        }
        return results;
    }
}
