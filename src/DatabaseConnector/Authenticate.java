package DatabaseConnector;

import App.Classes.EmployeeAccount;
import java.util.logging.Level;
import java.util.logging.Logger;
import App.Classes.Location;

public class Authenticate {

    /**
     * @param username - The users' username
     * @param password - The users' password
     * @return an EmployeeAccount object holding all the users information
     * @exception NotManagerException when user authenticates but is not a manager
     * Connects to the MySQL server and checks the inputs for a match in the database
     * If exists, new EmployeeAccount object created with the users information
     * and
     */
    public static EmployeeAccount authenticate (String username, String password) throws NotManagerException {
        //Create logger object
        Logger lgr = Logger.getLogger(Authenticate.class.getName());

        //All we want is the userID so we can then fetch all the information
        //So grab the first column of the first row

        //Connect to database
        SQLConnector con = new SQLConnector();
        con.connect();

        //Create a new Query
        lgr.log(Level.INFO, "Authenticating user: " + username);
        Query q = new Query("SELECT user.userID FROM user WHERE username = '" + username + "' AND password = '" + password + "';");
        // Query q = new Query("SELECT * FROM user;");
        Results results = q.executeQuery();

        if (results.getCols() == 1 && results.getRows() == 1) {
            //user exists, username and password exists
            //authorize user and fetch data
            lgr.log(Level.INFO, "Authentication succeed. Fetching user's data");
            EmployeeAccount acc = authorize((int)results.getElement(0,0), lgr);
            if(acc == null) {
                throw new NotManagerException("User is not a manager or operator");
            }
            return acc;
        } else {
            //user does not exist, username and password do not match
            lgr.log(Level.WARNING, "Authentication failed for user: " + username);
        }
        return null;
    }

    private static EmployeeAccount authorize(int userID, Logger lgr) {
        //Create a new query
        lgr.log(Level.INFO, "Authorizing userID: " + userID);
        Query q = new Query("SELECT user.userID, user.username, employees.employeeID, location.locationID, location.name AS 'location', employee_info.firstName,\n" +
                "       employee_info.lastName, employee_info.workEmail, employee_info.workTel, account_types.type\n" +
                "FROM user\n" +
                "INNER JOIN employees ON user.userID = employees.userID\n" +
                "INNER JOIN employee_info ON employees.employeeID = employee_info.employeeID\n" +
                "INNER JOIN account_types ON user.accountTypeID = account_types.accountTypeID\n" +
                "INNER JOIN location ON employees.location = location.locationID\n" +
                "WHERE user.userID = " + userID + ";\n");
        Results results = q.executeQuery();

        if(!results.isEmpty()) {
            //Should only be one result
            EmployeeAccount acc = new EmployeeAccount();
            acc.setUserID((int) results.getElement(0, 0));
            acc.setUsername((String) results.getElement(0, 1));
            acc.setEmployeeID((int) results.getElement(0, 2));
            acc.setLocation(new Location((int) results.getElement(0, 3), (String) results.getElement(0, 4)));
            acc.setFirstName((String) results.getElement(0, 5));
            acc.setLastName((String) results.getElement(0, 6));
            acc.setEmail((String) results.getElement(0, 7));
            acc.setPhoneNumber((String) results.getElement(0, 8));
            acc.setAccType((String) results.getElement(0, 9));
            return acc;

        } else {
            lgr.log(Level.WARNING, "Username and password is correct, however user is not a manager");
            return null;
        }
    }
}
