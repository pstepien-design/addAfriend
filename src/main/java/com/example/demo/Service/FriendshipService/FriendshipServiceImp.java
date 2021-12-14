package com.example.demo.Service.FriendshipService;

import com.example.demo.Entity.Friendship;
import com.example.demo.Repository.FriendshipRepository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendshipServiceImp implements FriendshipService {

  @Autowired
   private FriendshipRepository friendshipRepository;

    @Override
    public List<Friendship> findAllFriendships() {
        return friendshipRepository.findAll();
    }

    @Override
    public Friendship findById(Long id) {
        return friendshipRepository.findById(id).orElseThrow(()->new RuntimeException("Friendship not found"));
    }

    @Override
    public Friendship findBySourceEmailAndDestinationEmail(String sourceEmail, String destinationEmail) {
        return friendshipRepository.findFriendshipBySourceEmailAndDestinationEmail(sourceEmail, destinationEmail)
                .orElseThrow(()->new RuntimeException("Friendship not found"));
    }

    @Override
    public boolean ifFriendshipExists(String sourceEmail, String destinationEmail) {
        return friendshipRepository.findFriendshipBySourceEmailAndDestinationEmail(sourceEmail, destinationEmail).isPresent()
                || friendshipRepository.findFriendshipBySourceEmailAndDestinationEmail(destinationEmail, sourceEmail).isPresent();
    }

    @Override
    public void addFriendship(Friendship friendship) {
    friendshipRepository.save(friendship);
    }

    @Override
    public void deleteFriendship(Long id) {
        Friendship myFriendship = friendshipRepository.findById(id).orElseThrow(()-> new RuntimeException("Friendship not found"));
        friendshipRepository.delete(myFriendship);
    }

    @Override
    public void editFriendship(Long id, Friendship friendship) {
        Friendship myFriendship = friendshipRepository.findById(id).orElseThrow(()-> new RuntimeException("Friendship not found"));
    myFriendship.setDateEstablished(friendship.getDateEstablished());
    myFriendship.setStatus(friendship.getStatus());
    myFriendship.setSourceEmail(friendship.getSourceEmail());
    myFriendship.setDestinationEmail(friendship.getDestinationEmail());
    friendshipRepository.save(myFriendship);
    }


}
