package com.example.demo.service;

import com.example.demo.entities.UserAction;
import com.example.demo.repository.UserActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserActionService {

    private final UserActionRepository userActionRepository;

    @Autowired
    public UserActionService(UserActionRepository userActionRepository) {
        this.userActionRepository = userActionRepository;
    }

    public void registerUserAction(String username, String action) {
        UserAction userAction = new UserAction(username, action, LocalDateTime.now());
        userActionRepository.save(userAction);
    }
    
    public List<UserAction> getAllUserActions() {
        return userActionRepository.findAll();
    }
    
    public List<UserAction> getRecentUserActions(int limit) {
        return userActionRepository.findLastUserActions(limit);
    }
}
