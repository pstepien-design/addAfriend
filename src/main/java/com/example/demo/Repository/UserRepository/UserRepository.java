package com.example.demo.Repository.UserRepository;

import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public List<User> findAll();
    public Optional<User> findById(Long id);
    public Optional<User> findByEmail(String email);

}
