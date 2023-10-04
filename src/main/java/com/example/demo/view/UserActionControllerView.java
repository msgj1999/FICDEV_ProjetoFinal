package com.example.demo.view;

import com.example.demo.entities.UserAction;
import com.example.demo.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user-actions")
public class UserActionControllerView {

    private final UserActionService userActionService;

    @Autowired
    public UserActionControllerView(UserActionService userActionService) {
        this.userActionService = userActionService;
    }

    @GetMapping
    public List<UserAction> getAllUserActions() {
        return userActionService.getAllUserActions();
    }
    
    @GetMapping("/recent-actions")
    public List<UserAction> getRecentUserActions(@RequestParam(name = "limit", defaultValue = "5") int limit) {
        List<UserAction> recentUserActions = userActionService.getRecentUserActions(limit);
        
        return recentUserActions;
    }

}