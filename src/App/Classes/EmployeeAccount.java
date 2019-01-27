//TODO: Documentation
package App.Classes;
public class EmployeeAccount extends Account {

    private int employeeID;
    private String employeeType;
    private Location loc;
    private boolean manager; //true if manager, false if operator CHANGE ACCOUNT CLASS AND ADD METHODS IN

    public EmployeeAccount() {
        super();
    }

    public EmployeeAccount(int employeeID, String employeeType, int userID, String username, String name, String email, String phoneNumber, Location loc, String accType) {
        super(userID, username, name, email, phoneNumber, accType);
        this.employeeID = employeeID;
        this.employeeType = employeeType;
        this.loc = loc;
    }

    public int getEmployeeID() {
        return this.employeeID;
    }

    public String getEmployeeType() {
        return this.employeeType;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }
}