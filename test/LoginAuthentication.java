import App.Classes.EmployeeAccount;
import DatabaseConnector.Authenticate;
import DatabaseConnector.LoginFailedException;

public class LoginAuthentication {

    public static void main(String[] args) {
        try {
            EmployeeAccount employee = Authenticate.authorize("admin", "admin");
            if (employee.getEmployeeID() != 0) {
                System.out.println("User authenticated");
                System.out.println("User ID: " + employee.getUserID());
                System.out.println("Employee ID: " + employee.getEmployeeID());
                System.out.println("Username: " + employee.getUsername());
                System.out.println("FirstName: " + employee.getFirstName());
                System.out.println("LastName: " + employee.getLastName());
                System.out.println("Email: " + employee.getEmail());
                System.out.println("Phone No.: " + employee.getPhoneNumber());
                System.out.println("Acc. Type: " + employee.getAccType());
            }
        } catch (LoginFailedException e) {
            System.out.println(e.getMessage());
        }
    }
}
