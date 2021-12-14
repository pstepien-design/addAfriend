package com.example.demo.Controller;

import com.example.demo.Entity.Friendship;
import com.example.demo.Service.FriendshipService.FriendshipService;
import com.example.demo.Service.ResponseService.ResponseService;
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

    @Autowired
    private ResponseService responseService;



    @RequestMapping(method = RequestMethod.POST, path = "/friendship")
   public ResponseEntity<String> getFriendship(@RequestBody Object friendshipRequest){

        String destinationHost = "localhost:8080";
        String ourHost = "localhost:8080";
        System.out.println("Root request: " +friendshipRequest);
        String reqSplit[] = friendshipRequest.toString().split(" ");
        for(String s: reqSplit){
            System.out.println(s);
        }
        System.out.println("preparing response");
        if(reqSplit[0].toUpperCase().contains("ADD")){
            String sourceEmail = reqSplit[1]+"@"+reqSplit[2];
            String destinationEmail = reqSplit[3]+"@"+reqSplit[4];
           if(!(friendshipService.ifFriendshipExists(sourceEmail, destinationEmail))) {
               Friendship friendship = new Friendship(sourceEmail, destinationEmail, date, destinationHost);
               friendshipService.addFriendship(friendship);
               String phrase = "Friendship between " + sourceEmail + " and " + destinationEmail + " has been added";
               return ResponseEntity.ok(responseService.createResponse(HttpStatus.OK, phrase));
           }
           else{
               String phrase = "Friendship already exists";
               return ResponseEntity.ok(responseService.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, phrase));  }
        }
        else if(reqSplit[0].toUpperCase().contains("BLOCK")){
            String sourceEmail = reqSplit[1]+"@"+reqSplit[2];
            String destinationEmail = reqSplit[3]+"@"+reqSplit[4];
            if((friendshipService.ifFriendshipExists(sourceEmail, destinationEmail))) {
             Friendship friendship = friendshipService.findBySourceEmailAndDestinationEmail(sourceEmail, destinationEmail);
             friendship.setStatus("blocked");
             String phrase = "Friendship between " + sourceEmail + " and " + destinationEmail + " has been blocked";
                return ResponseEntity.ok(responseService.createResponse(HttpStatus.OK, phrase));
            }
            else{
                String phrase ="Friendship does not exists";
                return ResponseEntity.ok(responseService.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, phrase));
            }
        }
        else if(reqSplit[0].toUpperCase().contains("DENY")){
            String sourceEmail = reqSplit[1]+"@"+reqSplit[2];
            String destinationEmail = reqSplit[3]+"@"+reqSplit[4];
            if(friendshipService.ifFriendshipExists(sourceEmail, destinationEmail)) {
                System.out.println("denying");
                Friendship friendship = friendshipService.findBySourceEmailAndDestinationEmail(sourceEmail, destinationEmail);
                friendship.setStatus("denied");
                String phrase = "Friendship between " + sourceEmail + " and " + destinationEmail + " has been denied";
                System.out.println(phrase);
                return ResponseEntity.ok(responseService.createResponse(HttpStatus.OK, phrase));
            }
            else{
                String phrase = "Friendship does not exists";
                return ResponseEntity.ok(responseService.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, phrase));
            }
        }
        else if(reqSplit[0].toUpperCase().contains("ACCEPT")){
            String sourceEmail = reqSplit[1]+"@"+reqSplit[2];
            String destinationEmail = reqSplit[3]+"@"+reqSplit[4];
            if(((friendshipService.ifFriendshipExists(sourceEmail, destinationEmail)))&& (destinationHost != ourHost)) {
                Friendship friendship = friendshipService.findBySourceEmailAndDestinationEmail(sourceEmail, destinationEmail);
                friendship.setStatus("accepted");
                String phrase = "Friendship between " + sourceEmail + " and " + destinationEmail + " has been accepted";
                return ResponseEntity.ok(responseService.createResponse(HttpStatus.OK, phrase));
            }
            else{
                String phrase = "Friendship could not have been accepted";
                System.out.println(phrase);
                return ResponseEntity.ok(responseService.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, phrase));
            }
        }
        else if(reqSplit[0].toUpperCase().contains("REMOVE")){
            String sourceEmail = reqSplit[1]+"@"+reqSplit[2];
            String destinationEmail = reqSplit[3]+"@"+reqSplit[4];
            if(((friendshipService.ifFriendshipExists(sourceEmail, destinationEmail)))&& (destinationHost != ourHost)) {
                Friendship friendship = friendshipService.findBySourceEmailAndDestinationEmail(sourceEmail, destinationEmail);
                friendshipService.deleteFriendship(friendship.getId());
                String phrase = "Friendship between " + sourceEmail + " and " + destinationEmail + " has been removed";
                return ResponseEntity.ok(responseService.createResponse(HttpStatus.OK, phrase));
            }
            else{
                String phrase = "Friendship could not have been removed";
                return ResponseEntity.ok(responseService.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, phrase));
            }
        }
        else{
          String phrase = "Desired request could not be made";
            return ResponseEntity.ok(responseService.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, phrase));

        }

    }



}
