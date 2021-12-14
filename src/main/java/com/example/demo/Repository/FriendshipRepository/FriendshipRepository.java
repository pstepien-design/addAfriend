package com.example.demo.Repository.FriendshipRepository;

import com.example.demo.Entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    public Optional<Friendship> findById(Long id);
    public List<Friendship> findAll();
    public Optional<Friendship> findFriendshipBySourceEmailAndDestinationEmail(String sourceEmail, String destinationEmail);


}

