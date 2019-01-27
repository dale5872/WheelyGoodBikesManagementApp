//TODO: Create system for retrieving user accounts
// i.e. create class that allows for database querying certain user information
package App.Classes;

public class Account {
    private int userID;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private String accType;

    public Account(int userID, String username, String name, String email, String phoneNumber, String accType) {
        this.userID = userID;
        this.username = username;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Account() {}

    public int getUserID () {
        return this.userID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail () {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getAccType() {
        return this.accType;
    }

    public void setUserID (int userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

}
