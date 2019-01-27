/**

package App;


import DatabaseConnector.*;

public class Main {
    //Main Class for starting the management app with no JavaFX interfaces

    public static void main(String[] args) {
        EmployeeAccount employee = Authenticate.authenticate("admin", "does not really matter");
        if(employee.getEmployeeID() != 0) {
            System.out.println("User authenticated");
            System.out.println("User ID: " +employee.getUserID());
            System.out.println("Employee ID: " +employee.getEmployeeID());
            System.out.println("Username: " +employee.getUsername());
            System.out.println("Name: " +employee.getName());
            System.out.println("Email: " +employee.getEmail());
            System.out.println("Phone No.: " +employee.getPhoneNumber());
            System.out.println("Acc. Type: " +employee.getAccType());
        }
    }
}
**/