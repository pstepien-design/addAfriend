package com.example.demo.Service.UserService;


import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository.UserRepository;
import com.example.demo.Service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                () -> new RuntimeException("User not found"));
    }

    @Override
    public void updateUser(Long id, User user) {
    User newUser = userRepository.findById(id)
                    .orElseThrow(
                    () -> new RuntimeException("User not found"));
    newUser.setEmail(user.getEmail());
    newUser.setFirstName(user.getFirstName());
    newUser.setLastName(user.getLastName());
    userRepository.save(newUser);
    }

    @Override
    public void addUser(User user) {
    userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        User myUser = userRepository.findById(id)
                .orElseThrow( () ->
                        new RuntimeException("User not Found")
                        );
        userRepository.delete(myUser);
    }
}
