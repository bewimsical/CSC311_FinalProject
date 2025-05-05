package edu.farmingdale.csc311_finalproject;

public class UserDto {
    private Long userId;
    private String username;
    private String fName;
    private String lName;
    private String email;
    private String profilePicUrl;
    private String userPassword;

    // No-arg constructor (needed for Gson or Jackson)
    public UserDto() {}

    // Getters
    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public String getUserPassword() {
        return userPassword;
    }

    // Setters (optional but helpful)
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
