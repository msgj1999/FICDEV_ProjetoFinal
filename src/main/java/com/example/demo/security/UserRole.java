package com.example.demo.security;

public enum UserRole {
    ADMIN("admin"),
    GESTOR("gestor"),
    TECNICO("tecnico");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
