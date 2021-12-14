package com.example.demo.Service.FriendshipService;

import com.example.demo.Entity.Friendship;

import java.util.List;
import java.util.Optional;

public interface FriendshipService {
    public List<Friendship> findAllFriendships();

    public Friendship findById(Long id);

    public Friendship findBySourceEmailAndDestinationEmail(String sourceEmail, String destinationEmail);

    public boolean ifFriendshipExists(String sourceEmail, String destinationEmail);

    public void addFriendship(Friendship friendship);

    public void deleteFriendship(Long id);

    public void editFriendship(Long id, Friendship friendship);

}
