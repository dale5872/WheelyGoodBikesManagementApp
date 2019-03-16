package DatabaseConnector;

import App.Classes.EmployeeAccount;
import java.util.logging.Level;
import java.util.logging.Logger;
import App.Classes.Location;

public class Authenticate {

    public static EmployeeAccount authorize(String username, String password) throws LoginFailedException {
        /**
         * TODO: Integrate password Encryption
         * BODY: Encrypt passwords before sending to the PHP script, where it is then dealt with
         */

        Logger lgr = Logger.getLogger(Query.class.getName());

        //Create a new query
        lgr.log(Level.INFO, "Authorizing user: " + username);
        Query q = new Query("read", "authenticateEmployee", "username=" + username + "&password=" + password);
        Results results = q.executeQuery();

        if(!results.isEmpty()) {
            //Should only be one result
            EmployeeAccount acc = new EmployeeAccount();
            acc.setUserID(Integer.parseInt((String)results.getElement(0, "userID")));
            acc.setUsername((String) results.getElement(0, "username"));
            acc.setEmployeeID(Integer.parseInt((String)results.getElement(0, "employeeID")));
            acc.setLocation(new Location(Integer.parseInt((String)results.getElement(0, "locationID")), (String) results.getElement(0, "location")));
            acc.setFirstName((String) results.getElement(0, "firstName"));
            acc.setLastName((String) results.getElement(0, "lastName"));
            acc.setEmail((String) results.getElement(0, "workEmail"));
            acc.setPhoneNumber((String) results.getElement(0, "workTel"));
            acc.setAccType((String) results.getElement(0, "type"));
            acc.setProfilePicture((String) results.getElement(0, "profilePicture"));
            return acc;

        } else {
            throw new LoginFailedException("Invalid login details!");
        }
    }
}
