package App.Classes;

public class EmployeeAccount extends Account{
    private int employeeID;
    private Location loc;

    public EmployeeAccount() {
        super();
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

    /**
     * DO NOT DELETE
     * Legacy accessor from before Location list was fully implemented.
     * This accessor is still used to display the location name in tables.
     * @return
     */
    @Deprecated
    public String getLocationName() {
        return this.loc.getName();
    }

    public Location getLocation() { return this.loc; }
}
