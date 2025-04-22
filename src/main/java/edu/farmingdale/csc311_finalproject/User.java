package edu.farmingdale.csc311_finalproject;

public class User {
    private Long userId;
    private String username; //should we make this unique?
    private String fName;
    private String lName;
    private String email;
    private String profilePicUrl;
    private String userPassword;

    public User() {
    }

    public User(Long userId, String username, String fName, String lName, String email, String profilePicUrl, String password) {
        this.userId = userId;
        this.username = username;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.profilePicUrl = profilePicUrl;
        this.userPassword = password;
    }

    public User(String username, String fName, String lName, String email, String profilePicUrl,String password) {
        this.username = username;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.profilePicUrl = profilePicUrl;
        this.userPassword = password;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
  
    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", username='" + username + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", email='" + email + '\'' +
                ", profilePicUrl='" + profilePicUrl + '\'' +
                '}';
    }
}
