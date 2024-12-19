package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestParam String name, @RequestParam double initialDeposit){
        return userService.registerUser(name, initialDeposit);
    }

    @PostMapping("/{userId}/buy")
    public User buyStock(@PathVariable Long userId, @RequestParam String stockSymbol,
                         @RequestParam int quantity, @RequestParam double pricePerShare){
        return userService.buyStock(userId, stockSymbol, quantity, pricePerShare);
    }

    @PostMapping("{userId}/sell")
    public User sellStock(@PathVariable Long userId, @RequestParam String stockSymbol,
                          @RequestParam int quantity, @RequestParam double pricePerShare){
        return userService.sellStock(userId, stockSymbol, quantity, pricePerShare);
    }

    @GetMapping("/{userId}")
    public User viewPortfolio(@PathVariable Long userId){
        return userService.getUser(userId);
    }
}

