package App.Classes;

public class Account {
    private int userID;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String accType;
    private String profilePicture;

    public Account() {}

    public int getUserID () {
        return this.userID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() { return this.lastName; }

    public String getEmail () {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getAccType() {
        return this.accType;
    }

    public String getProfilePicture() { return this.profilePicture; }

    public void setUserID (int userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {this.lastName = lastName; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

}
