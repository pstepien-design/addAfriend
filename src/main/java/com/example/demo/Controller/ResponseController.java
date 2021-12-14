package com.example.demo.Controller;

import com.example.demo.Entity.Friendship;
import com.example.demo.Service.FriendshipService.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class ResponseController {

    LocalDate date = LocalDate.now();


    @Autowired
    private FriendshipService friendshipService;


    @RequestMapping(method = RequestMethod.POST, path = "/friendship")
   public ResponseEntity<String> getFriendship(@RequestBody Object friendshipRequest){

        String destinationHost = "localhost:8080";
        String ourHost = "localhost:8080";
        System.out.println("Root request: " +friendshipRequest);
        String reqSplit[] = friendshipRequest.toString().split(" ");
        for(String s: reqSplit){
            System.out.println(s);
        }
        if(reqSplit[0].toUpperCase().contains("ADD")){
            String sourceEmail = reqSplit[1]+"@"+reqSplit[2];
            String destinationEmail = reqSplit[3]+"@"+reqSplit[4];
           if(!(friendshipService.ifFriendshipExists(sourceEmail, destinationEmail))) {
               Friendship friendship = new Friendship(sourceEmail, destinationEmail, date, destinationHost);
               friendshipService.addFriendship(friendship);
               return ResponseEntity.ok("Friendship between " + sourceEmail + " and " + destinationEmail + " has been added");
           }
           else{
               System.out.println("We are here, bad request");
               return ResponseEntity.ok("Friendship could not have been made");
           }
        }
        else if(reqSplit[0].toUpperCase().contains("BLOCK")){
            String sourceEmail = reqSplit[1]+"@"+reqSplit[2];
            String destinationEmail = reqSplit[3]+"@"+reqSplit[4];
            if(!(friendshipService.ifFriendshipExists(sourceEmail, destinationEmail))) {
             Friendship friendship = friendshipService.findBySourceEmailAndDestinationEmail(sourceEmail, destinationEmail);
             friendship.setStatus("blocked");
             return ResponseEntity.ok("Friendship between " + sourceEmail + " and " + destinationEmail + " has been blocked");
            }
            else{
                return ResponseEntity.ok("Friendship could not have been blocked");
            }
        }
        else if(reqSplit[0].toUpperCase().contains("DENY")){
            String sourceEmail = reqSplit[1]+"@"+reqSplit[2];
            String destinationEmail = reqSplit[3]+"@"+reqSplit[4];
            if(!(friendshipService.ifFriendshipExists(sourceEmail, destinationEmail))) {
                Friendship friendship = friendshipService.findBySourceEmailAndDestinationEmail(sourceEmail, destinationEmail);
                friendship.setStatus("denied");
                return ResponseEntity.ok("Friendship between " + sourceEmail + " and " + destinationEmail + " has been denied");
            }
            else{
                return ResponseEntity.ok("Friendship could not have been blocked");
            }
        }
        else if(reqSplit[0].toUpperCase().contains("ACCEPT")){
            String sourceEmail = reqSplit[1]+"@"+reqSplit[2];
            String destinationEmail = reqSplit[3]+"@"+reqSplit[4];
            if((!(friendshipService.ifFriendshipExists(sourceEmail, destinationEmail)))&& (destinationHost != ourHost)) {
                Friendship friendship = friendshipService.findBySourceEmailAndDestinationEmail(sourceEmail, destinationEmail);
                friendship.setStatus("accepted");
                return ResponseEntity.ok("Friendship between " + sourceEmail + " and " + destinationEmail + " has been accepted");
            }
            else{
                return ResponseEntity.ok("Friendship could not have been accepted");
            }
        }
        else if(reqSplit[0].toUpperCase().contains("REMOVE")){
            String sourceEmail = reqSplit[1]+"@"+reqSplit[2];
            String destinationEmail = reqSplit[3]+"@"+reqSplit[4];
            if((!(friendshipService.ifFriendshipExists(sourceEmail, destinationEmail)))&& (destinationHost != ourHost)) {
                Friendship friendship = friendshipService.findBySourceEmailAndDestinationEmail(sourceEmail, destinationEmail);
                friendshipService.deleteFriendship(friendship.getId());
                return ResponseEntity.ok("Friendship between " + sourceEmail + " and " + destinationEmail + " has been removed");
            }
            else{
                return ResponseEntity.ok("Friendship could not have been removed");
            }
        }
        else{
          return ResponseEntity.badRequest().body("Desired request could not be made");
        }

    }



}
