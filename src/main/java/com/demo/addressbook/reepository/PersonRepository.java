package com.demo.addressbook.reepository;

import com.demo.addressbook.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByFirstName(String query);
}
