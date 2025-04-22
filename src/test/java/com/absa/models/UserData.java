// src/test/java/com/absa/models/UserData.java
package com.absa.models;

public class UserData {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String company;
    private String role;
    private String email;
    private String mobilePhone;

    // No-args constructor for Cucumber + setters usage
    public UserData() {
    }

    public UserData(String firstName, String lastName, String username, String password,
                    String company, String role, String email, String mobilePhone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.company = company;
        this.role = role;
        this.email = email;
        this.mobilePhone = mobilePhone;
    }

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobilePhone() { return mobilePhone; }
    public void setMobilePhone(String mobilePhone) { this.mobilePhone = mobilePhone; }
}
