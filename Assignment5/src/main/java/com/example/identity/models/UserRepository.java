package com.example.identity.models;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserByPhoneNumber(String number);
}
