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
                            @ModelAttribute("sourceHost") String sourceHost,
                            @ModelAttribute("destinationEmail") String destinationEmail,
                            @ModelAttribute("destinationHost") String destinationHost,
                            Model model){


        if(!(friendshipService.ifFriendshipExists(sourceEmail, destinationEmail))) {
            final String SERVER_URL = destinationHost+"/friendship";
            System.out.println(SERVER_URL);



            Friendship friendship = new Friendship(sourceEmail, destinationEmail, date, destinationHost, sourceHost);
            friendshipService.addFriendship(friendship);
            String response = requestService.request(SERVER_URL, "ADD", sourceEmail, destinationEmail, sourceHost, destinationHost);

            model.addAttribute("response", response);
        }
        else{
            model.addAttribute("response", "Friendship already exists!");
        }


        return "redirect:/";
    }
    @PostMapping("/acceptFriendship")
    public String acceptFriendship(@ModelAttribute("id") Long id,  @ModelAttribute("destinationHost") String destinationHost, @ModelAttribute("sourceHost") String sourceHost){
        Friendship friendship = friendshipService.findById(id);
        if(friendship.getStatus().equals("requested")) {
            String sourceEmail = friendship.getSourceEmail();
            String destinationEmail = friendship.getDestinationEmail();

            final String SERVER_URL = destinationHost + "/friendship";

            Friendship newFriendship = new Friendship(friendship.getSourceEmail(), friendship.getDestinationEmail(), friendship.getDateEstablished(), destinationHost,  "accepted", sourceHost);
            friendshipService.editFriendship(id, newFriendship);


            String response = requestService.request(SERVER_URL, "ACCEPT", sourceEmail, destinationEmail, sourceHost, destinationHost);

            System.out.println(response);
        }
        return "redirect:/";
    }
    @PostMapping("/denyFriendship")
    public String denyFriendship(@ModelAttribute("id") Long id,  @ModelAttribute("destinationHost") String destinationHost, @ModelAttribute("sourceHost") String sourceHost){
        Friendship friendship = friendshipService.findById(id);
        String sourceEmail = friendship.getSourceEmail();
        String destinationEmail = friendship.getDestinationEmail();

        final String SERVER_URL = destinationHost+"/friendship";





        friendshipService.deleteFriendship(id);
        String response = requestService.request(SERVER_URL, "DENY", sourceEmail, destinationEmail, sourceHost, destinationHost);
        return "redirect:/";
    }
    @PostMapping("/blockFriendship")
    public String blockFriendship(@ModelAttribute("id") Long id,  @ModelAttribute("destinationHost") String destinationHost, @ModelAttribute("sourceHost") String sourceHost){
        Friendship friendship = friendshipService.findById(id);
        String sourceEmail = friendship.getSourceEmail();
        String destinationEmail = friendship.getDestinationEmail();

        final String SERVER_URL = destinationHost+"/friendship";



        Friendship newFriendship = new Friendship(friendship.getSourceEmail(), friendship.getDestinationEmail(), friendship.getDateEstablished(), destinationHost, "blocked", sourceHost);
        String response = requestService.request(SERVER_URL, "BLOCK", sourceEmail, destinationEmail, sourceHost, destinationHost);

        System.out.println(response);
        return "redirect:/";
    }
    @PostMapping("/removeFriendship")
    public String removeFriendship(@ModelAttribute("id") Long id,  @ModelAttribute("destinationHost") String destinationHost, @ModelAttribute("sourceHost") String sourceHost){
        Friendship friendship = friendshipService.findById(id);
        String sourceEmail = friendship.getSourceEmail();
        String destinationEmail = friendship.getDestinationEmail();

        final String SERVER_URL = destinationHost+"/friendship";





        friendshipService.deleteFriendship(id);
        String response = requestService.request(SERVER_URL, "REMOVE", sourceEmail, destinationEmail, sourceHost, destinationHost);
        System.out.println(response);
        return "redirect:/";
    }



    @GetMapping("/")
    public String index(Model model) {
        List<Friendship> friendshipList = friendshipService.findAllFriendships();
        List<Friendship> pendingFriendships = new ArrayList<>();
        List<Friendship> requestedFriendships = new ArrayList<>();
        List<Friendship> acceptedFriendships = new ArrayList<>();
        List<Friendship> deniedFriendships = new ArrayList<>();
        List<Friendship> blockedFriendships = new ArrayList<>();
        for(Friendship friendships: friendshipList){
            System.out.println(friendships.getStatus());
            if(friendships.getStatus().equals("pending")){
                pendingFriendships.add(friendships);
            }
            else if(friendships.getStatus().equals("requested")){
                requestedFriendships.add(friendships);
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
        model.addAttribute("requestedFriendships", requestedFriendships);
        model.addAttribute("acceptedFriendships", acceptedFriendships);
        model.addAttribute("deniedFriendships", deniedFriendships);
        model.addAttribute("blockedFriendships", blockedFriendships);
        return "index";
    }
}

