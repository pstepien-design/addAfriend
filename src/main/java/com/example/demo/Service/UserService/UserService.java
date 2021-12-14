package com.example.demo.Service.UserService;

import com.example.demo.Entity.User;

import java.util.List;

public interface UserService {

    public List<User> findAllUsers();

    public User findUserByEmail(String email);

    public void updateUser(Long id, User user);

    public void addUser(User user);

    public void deleteUserById(Long id);
}
