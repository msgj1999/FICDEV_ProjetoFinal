package com.example.demo.repository;

import com.example.demo.entities.UserAction;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserActionRepository extends JpaRepository<UserAction, Long> {

    @Query("SELECT ua FROM UserAction ua ORDER BY ua.timestamp DESC LIMIT 7")
    List<UserAction> findLastUserActions(int limit);

}
