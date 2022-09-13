package com.example.Test.repositories;

import com.example.Test.models.Users;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Users, Long> {
}
