//TODO: Documentation
package App.Classes;
public class EmployeeAccount extends Account {

    private int employeeID;
    private Location loc;

    public EmployeeAccount() {
        super();
    }

    public EmployeeAccount(int employeeID, int userID, String username, String firstName, String lastName, String email, String phoneNumber, Location loc, String accType) {
        super(userID, username, firstName, lastName, email, phoneNumber, accType);
        this.employeeID = employeeID;
        this.loc = loc;
    }

    public int getEmployeeID() {
        return this.employeeID;
    }


    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }


    public void setLocation(Location loc) {
        this.loc = loc;
    }

    public void setLocation(String name) {
        this.loc = new Location(name);
    }

    public String getLocationName() {
        return this.loc.getName();
    }

    public int getLocationID() {
        return this.loc.getLocationID();
    }

    public Location getLocation() { return this.loc; }


    public void close() {
        this.employeeID = 0;
        this.loc = null;
    }
}
