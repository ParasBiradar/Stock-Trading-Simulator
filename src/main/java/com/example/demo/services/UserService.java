package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String name, double initialDeposit){
        User user = new User();
        user.setName(name);
        user.setAccountBalance(initialDeposit);
        return userRepository.save(user);
    }

    public User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User buyStock(Long userId, String stockSymbol, int quantity, double pricePerShare){
        User user = getUser(userId);
        double totalCost = quantity * pricePerShare;

        if(user.getAccountBalance() < totalCost){
            throw new RuntimeException("Insufficient balance");
        }

        user.setAccountBalance(user.getAccountBalance() - totalCost);
        user.getPortfolio().merge(stockSymbol, quantity, Integer::sum);
        return user;
    }

    @Transactional
    public User sellStock(Long userId, String stockSymbol, int quantity, double pricePerShare){
        User user = getUser(userId);

        if(!user.getPortfolio().containsKey(stockSymbol) || user.getPortfolio().get(stockSymbol) < quantity){
            throw new RuntimeException("Not enough stock to sell");
        }

        user.setAccountBalance(user.getAccountBalance() + quantity * pricePerShare);
        user.getPortfolio().put(stockSymbol, user.getPortfolio().get(stockSymbol)-quantity);

        if(user.getPortfolio().get(stockSymbol) == 0){
            user.getPortfolio().remove(stockSymbol);
        }
        return user;
    }
}

