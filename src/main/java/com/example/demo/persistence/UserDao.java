package com.example.demo.persistence;

import com.example.demo.domain.User;

import java.util.List;

public interface UserDao {

    void save (User u);

    byte[] findSalt(String username);

    boolean login(User u);

    List<User> leak();
}
