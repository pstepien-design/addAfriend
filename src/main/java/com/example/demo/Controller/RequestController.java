package com.example.demo.Controller;

import com.example.demo.Entity.Friendship;
import com.example.demo.Service.FriendshipService.FriendshipService;
import com.example.demo.Service.Request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class RequestController {
    @Autowired
    private RequestService requestService;

    @Autowired
    private FriendshipService friendshipService;

    LocalDate date = LocalDate.now();



    @PostMapping("/addFriend")
    public String addFriend(@ModelAttribute("sourceEmail") String sourceEmail,
                            @ModelAttribute("destinationEmail") String destinationEmail,
                            @ModelAttribute("destinationHost") String destinationHost,
                            Model model){


        if(!(friendshipService.ifFriendshipExists(sourceEmail, destinationEmail))) {
            final String SERVER_URL = destinationHost+"/friendship";
            System.out.println(SERVER_URL);

            String response = requestService.request(SERVER_URL, "ADD", sourceEmail, destinationEmail);

            Friendship friendship = new Friendship(sourceEmail, destinationEmail, date, destinationHost);
            friendshipService.addFriendship(friendship);
            model.addAttribute("response", response);
        }
        else{
            model.addAttribute("response", "friendship already exists");
        }


        return "index";
    }
    @PostMapping("/acceptFriendship")
    public String acceptFriendship(@ModelAttribute("id") Long id,  @ModelAttribute("destinationHost") String destinationHost){
        Friendship friendship = friendshipService.findById(id);
        String sourceEmail = friendship.getSourceEmail();
        String destinationEmail = friendship.getDestinationEmail();

        final String SERVER_URL = destinationHost+"/friendship";

        String response = requestService.request(SERVER_URL, "ACCEPT", sourceEmail, destinationEmail);

        Friendship newFriendship = new Friendship(friendship.getSourceEmail(), friendship.getDestinationEmail(), friendship.getDateEstablished(), destinationHost, "accepted");
        friendshipService.editFriendship(id, newFriendship);
        return "redirect:/";
    }
    @PostMapping("/denyFriendship")
    public String denyFriendship(@ModelAttribute("id") Long id,  @ModelAttribute("destinationHost") String destinationHost){
        Friendship friendship = friendshipService.findById(id);
        String sourceEmail = friendship.getSourceEmail();
        String destinationEmail = friendship.getDestinationEmail();

        final String SERVER_URL = destinationHost+"/friendship";

        String response = requestService.request(SERVER_URL, "DENIED", sourceEmail, destinationEmail);

        friendshipService.deleteFriendship(id);
        return "redirect:/";
    }
    @PostMapping("/blockFriendship")
    public String blockFriendship(@ModelAttribute("id") Long id,  @ModelAttribute("destinationHost") String destinationHost){
        Friendship friendship = friendshipService.findById(id);
        String sourceEmail = friendship.getSourceEmail();
        String destinationEmail = friendship.getDestinationEmail();

        final String SERVER_URL = destinationHost+"/friendship";

        String response = requestService.request(SERVER_URL, "BLOCKED", sourceEmail, destinationEmail);

        Friendship newFriendship = new Friendship(friendship.getSourceEmail(), friendship.getDestinationEmail(), friendship.getDateEstablished(), destinationHost, "blocked");
        friendshipService.editFriendship(id, newFriendship);
        return "redirect:/";
    }
    @PostMapping("/removeFriendship")
    public String removeFriendship(@ModelAttribute("id") Long id,  @ModelAttribute("destinationHost") String destinationHost){
        Friendship friendship = friendshipService.findById(id);
        String sourceEmail = friendship.getSourceEmail();
        String destinationEmail = friendship.getDestinationEmail();

        final String SERVER_URL = destinationHost+"/friendship";

        String response = requestService.request(SERVER_URL, "REMOVE", sourceEmail, destinationEmail);

        friendshipService.deleteFriendship(id);
        friendshipService.deleteFriendship(id);
        return "redirect:/";
    }



    @GetMapping("/")
    public String index(Model model) {
        List<Friendship> friendshipList = friendshipService.findAllFriendships();
        List<Friendship> pendingFriendships = new ArrayList<>();
        List<Friendship> acceptedFriendships = new ArrayList<>();
        List<Friendship> deniedFriendships = new ArrayList<>();
        List<Friendship> blockedFriendships = new ArrayList<>();
        for(Friendship friendships: friendshipList){
            System.out.println(friendships.getStatus());
            if(friendships.getStatus().equals("pending")){
                pendingFriendships.add(friendships);
            }
            else if(friendships.getStatus().equals("accepted")){
                acceptedFriendships.add(friendships);
            }
            else if(friendships.getStatus().equals("denied")){
                deniedFriendships.add(friendships);
            }
            else{
                blockedFriendships.add(friendships);
            }
        }

        model.addAttribute("pendingFriendships", pendingFriendships);
        model.addAttribute("acceptedFriendships", acceptedFriendships);
        model.addAttribute("deniedFriendships", deniedFriendships);
        model.addAttribute("blockedFriendships", blockedFriendships);
        return "index";
    }
}

