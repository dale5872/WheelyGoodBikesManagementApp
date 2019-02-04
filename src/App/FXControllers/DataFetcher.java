//TODO: Implement fetching rentals
//TODO: Catch SQL Exceptions for constraint violations and duplicate entries
//TODO: String Sanitization to prevent SQL Injection
package App.FXControllers;

import App.Classes.*;
import DatabaseConnector.InsertFailedException;
import DatabaseConnector.Query;
import DatabaseConnector.Results;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.logging.Logger;

public class DataFetcher {

    /**
     * Uses the query string to fetch the data from the SQL server and
     * creates many Account / EmployeeAccount objects and parses the data
     * into these objects. Then returns the list of these objects for
     * display into the table
     * @param queryString Query to execute on the SQL Server
     * @return A list of Accounts filled with the data in each account
     */
    protected static ObservableList<EmployeeAccount> accounts(String queryString) {
        ObservableList<EmployeeAccount> accounts = FXCollections.observableArrayList();

        Results res = query(queryString);

        //check we have results
        if(!res.isEmpty()) {
            //add data
            /*
            TABLE STRUCTURE:
            (employeeID, username, firstName, lastName, email, phone, accountType, location)
             */
            for(int r = 0; r < res.getRows(); r++) {
                EmployeeAccount acc = new EmployeeAccount();

                acc.setEmployeeID((int)res.getElement(r,0));
                acc.setUsername((String)res.getElement(r,1));
                acc.setFirstName((String)res.getElement(r,2));
                acc.setLastName((String)res.getElement(r,3));
                acc.setEmail((String)res.getElement(r,4));
                acc.setPhoneNumber((String)res.getElement(r,5));
                acc.setAccType((String)res.getElement(r,6));
                Location loc = new Location((int)res.getElement(r,7), (String)res.getElement(r,8));
                acc.setLocation(loc);
                acc.setUserID((int)res.getElement(r,9));

                accounts.add(acc);
            }
        } else {
            //throw an exception
            //TODO: Empty Set Exception
        }

        return accounts;
    }

    /**
     * Adds a new user to the database through the data from the @param acc object,
     * then returns the account's user id once added to the database and returns
     * that value
     * @param acc the Account object of the new user to be added into the database
     * @return the user ID of the new account
     * @throws InsertFailedException Error Codes DF01-DF04
     */
    protected static void addAccount(Account acc, String password, int accountType) throws InsertFailedException{
        int userID = -1;
        String queryString = "INSERT INTO user VALUES" +
                " (null," +
                " '" + accountType + "'," +
                " '" + acc.getUsername() + "'," +
                " '" + password +"');";
        Query q = new Query(queryString);

        //check it executed properly
        if(q.insertQuery()) {
            //get the user id
            queryString = "SELECT user.userID FROM user WHERE" +
                    " username='" + acc.getUsername() + "' AND password='" + password + "';";
            q.updateQuery(queryString);
            Results res = q.executeQuery();
            //will be an integer result
            userID = (int)res.getElement(0,0);

            if(userID <= 0 ) {
                throw new InsertFailedException("Could not create user " + acc.getUsername() + "."
                        + " Error Code: DF01");
            }

            acc.setUserID(userID);
        } else {
            throw new InsertFailedException("Could not create user " + acc.getUsername() + "."
                    + " Error Code: DF02");
        }

        if(acc instanceof EmployeeAccount) {
            //we have an EmployeeAccount object
            queryString = "INSERT INTO employees VALUES" +
                    " (null," +
                    " '" + acc.getUserID() + "'," +
                    " '" + ((EmployeeAccount) acc).getLocationID() + "');";
            q.updateQuery(queryString);
            if(!q.insertQuery()) {
                throw new InsertFailedException("Could not create user " + acc.getUsername() + "."
                        + " Error Code: DF03");
            }

            queryString = "SELECT employees.employeeID FROM employees WHERE employees.userID = '" + acc.getUserID() + "';";
            Results res = q.executeQuery(queryString);
            ((EmployeeAccount) acc).setEmployeeID((int)res.getElement(0,0));

            queryString = "INSERT INTO employee_info VALUES" +
                    " ('" + ((EmployeeAccount) acc).getEmployeeID() + "'," +
                    " '" + acc.getFirstName() + "'," +
                    " '" + acc.getLastName() + "'," +
                    " '" + acc.getEmail() + "'," +
                    " '" + acc.getPhoneNumber() + "');";

            q.updateQuery(queryString);
            if(!q.insertQuery()) {
                throw new InsertFailedException("Could not create user " + acc.getUsername() + "."
                        + " Error Code: DF04");
            }
        } else if(acc instanceof Account) {
            //standard user account
            //TODO: Implement adding a standard user account
        }
    }

    /**
     * Uses the @param newAcc to update the values in the database for the given user
     * @param oldAcc the account being replaced
     * @param newAcc the nre account
     * @param accountType account type
     * @throws InsertFailedException exception if failure to update the account
     */
    protected static void updateAccount(Account oldAcc, Account newAcc, int accountType) throws InsertFailedException {
        //check if account info is the same
        //update user table for any account
        int userID = oldAcc.getUserID();
        Query q = new Query();

        String queryString = "UPDATE user " +
                "SET accountTypeID='" + accountType + "', " +
                "username='" + newAcc.getUsername() + "' " +
                "WHERE user.userID='" + userID + "';";

        q.updateQuery(queryString);
        if(!q.insertQuery()) {
            throw new InsertFailedException("Could not update user " + oldAcc.getUsername() + ". Error Code: DF07");
        }

        //check if accounts are employee accounts
        if(newAcc instanceof EmployeeAccount) {
            //we have an employee account object
            //update employees
            queryString = "UPDATE employees SET " +
                    "location='" + ((EmployeeAccount)newAcc).getLocationID() + "' " +
                    "WHERE userID='" + newAcc.getUserID() + "';";

            q.updateQuery(queryString);
            if(!q.insertQuery()) {
                throw new InsertFailedException("Could not update user " + oldAcc.getUsername() + ". Error Code: DF08");
            }

            //update employee_info
            queryString = "UPDATE employee_info SET " +
                    "firstName='" + newAcc.getFirstName() + "', " +
                    "lastName='" + newAcc.getLastName() + "', " +
                    "workEmail='" + newAcc.getEmail() + "', " +
                    "workTel='" + newAcc.getPhoneNumber() + "' " +
                    "WHERE employeeID='" + ((EmployeeAccount) newAcc).getEmployeeID() + "';";
            q.updateQuery(queryString);
            if(!q.insertQuery()) {
                throw new InsertFailedException("Could not update user " + oldAcc.getUsername() + ". Error Code: DF09");
            }

        } else if(newAcc instanceof Account) {
            //standard user account
            //TODO: Implement standard user account

        }
    }

    /**
     * Deletes a given user with the user ID and employeeID provided
     * @param userID ID of the user account
     * @param employeeID the employee ID
     * @throws InsertFailedException if failure to delete the user account and all of it's data
     */
    protected static void deleteAccount(int userID, int employeeID) throws InsertFailedException {
        String queryString = "DELETE FROM employee_info WHERE employeeID = '" + employeeID + "';";

        Query q = new Query(queryString);
        if(!q.insertQuery()) {
            throw new InsertFailedException("Deletion of " + userID + " failed. Error code DF10");
        }
        queryString = "DELETE FROM employees WHERE employeeID = '" + employeeID + "' AND userID = '" + userID + "';";
        if(!q.insertQuery(queryString)) {
            throw new InsertFailedException("Deletion of " + userID + " failed. Error code DF11");
        }
        queryString = "DELETE FROM user WHERE userID = '" + userID + "';";
        if(!q.insertQuery(queryString)) {
            throw new InsertFailedException("Deletion of " + userID + " failed. Error code DF12");
        }

    }

    /**
     * Return all the equipment data from the database with the given query
     * @return list of Equipment objects
     */
    protected static ObservableList<Equipment> equipment() {
        ObservableList<Equipment> equipment = FXCollections.observableArrayList();

        //TODO: Change Query String, returning empty set
        String queryString = "SELECT equipment_stock.equipmentID, equipment_stock.equipmentType, equipment_stock.location, location.name,\n" +
                "       equipment_stock.equipmentStatus, equipment_type.pricePerHour\n" +
                "FROM equipment_stock\n" +
                "INNER JOIN location ON equipment_stock.location = location.locationID\n" +
                "INNER JOIN equipment_type ON equipment_stock.equipmentType = equipment_type.equipmentType;";


        Results res = query(queryString);

        //check we have results
        if(!res.isEmpty()) {
           /* TABLE STRUCTURE
           (EquipmentID, EquipmentName, LocationID, LocationName, EquipmentStatus, Price)
            */
            for(int r = 0; r < res.getRows(); r++) {
                Equipment e = new Equipment();
                e.setID((int) res.getElement(r, 0));
                e.setType((String)res.getElement(r,1));
                e.setLocation(getSingleLocation((int)res.getElement(r,2)));
                e.setStatus((String)res.getElement(r,4));
                e.setPrice((float)res.getElement(r, 5));
                equipment.add(e);
            }
        } else {
            //TODO: throw empty data set exception
        }

        return equipment;
    }

    protected static void addEquipment(Equipment eq) throws InsertFailedException {
        String queryString = "INSERT INTO equipment_stock VALUES" +
                " (null," +
                " '" + eq.getType() + "'," +
                " '" + eq.getLocationID() + "'," +
                " '" + eq.getStatus() + "');";
        Query q = new Query(queryString);

        if(q.insertQuery()) {
            //success
        } else {
            throw new InsertFailedException("Failed to add new equipment of type " + eq.getType());
        }

    }

    private static Location getSingleLocation(int id) {

        String queryString = "SELECT location.locationID, location.name " +
                "FROM location " +
                "WHERE location.locationID = " +id;
        Results res = query(queryString);

        if(!res.isEmpty()) {
            Location tmp = new Location();
            tmp.setLocationID((int)res.getElement(0,0));
            tmp.setName((String)res.getElement(0,1));
            return tmp;
        }

        ///TODO: Throw a empty data set exception
        return null;
    }

    /**
     * Return all of the locations and its data that is in the database
     * @return the list of Location objects
     */
    protected static ObservableList<Location> locations() {
        ObservableList<Location> locations = FXCollections.observableArrayList();

        String queryString = "SELECT location.locationID, location.name " +
                "FROM location";
        Results res = query(queryString);

        //check we have results
        if(!res.isEmpty()) {
            for(int r = 0; r < res.getRows(); r++) {
                locations.add(new Location((int)res.getElement(r, 0), (String)res.getElement(r,1)));
            }
        } else {
            //throw an exception
        }

        return locations;
    }

    /**
     * Adds a new location to the database
     * @param locationName name of the new location
     * @return true if succeed, false if failed
     * @throws InsertFailedException if failure and shows message to user
     */
    protected static boolean addLocation(String locationName) throws InsertFailedException {
        String queryString = "INSERT INTO location VALUES (null, '" + locationName + "');";
        Query q = new Query(queryString);

        if(q.insertQuery()) {
            return true;
        } else {
            throw new InsertFailedException("Failed to insert the new location " + locationName);
        }
    }

    /**
     * Updates the data in the database with the new name
     * @param newLoc location object containing the new name of the location
     * @return true if succeed, false if failed
     * @throws InsertFailedException if failure and shows message to user
     */
    protected static boolean editLocation(Location newLoc) throws InsertFailedException {
        String queryString = "UPDATE location SET location.name = '" + newLoc.getName() + "'" +
                "WHERE location.locationID = " + newLoc.getLocationID() + ";";
        Query q = new Query(queryString);

        if(q.insertQuery()) {
            return true;
        } else {
            throw new InsertFailedException("Failed to change location name to " + newLoc.getName());
        }
    }

    /**
     * Deletes the location from the database
     * @param loc Location object of location to be deleted
     * @return true if succeed
     * @throws InsertFailedException when failed, gives user a message
     */
    protected static boolean deleteLocation(Location loc) throws InsertFailedException {
        String queryString = "DELETE FROM location WHERE locationID = '" + loc.getLocationID() + "';";
        Query q = new Query(queryString);

        if(q.insertQuery()) {
            //succeeded
            return true;
        } else {
            throw new InsertFailedException("Deleting location " + loc.getName() + " failed." +
                    "\n" +
                    "Hints: Employees are still registered as working in " + loc.getName() + "" +
                    "\n" +
                    "There are active rentals at " + loc.getName() + "" +
                    "\nCannot remove until solved.");
        }
    }









    protected static ObservableList<Rental> rentals(String queryString) {
        ObservableList<Rental> rentals = FXCollections.observableArrayList();

        Results res = query(queryString);

        //check we have results
        if(!res.isEmpty()) {
            for(int r = 0; r < res.getRows(); r++) {
                //implement rentals
            }
        } else {
            //throw exception
        }
        return rentals;
    }

    /**
     * Returns a HashMap of the values needed for the dropdown values in the
     * add / edit forms
     * @param dropdown "accountTypes" or "locations" are acceptable inputs
     * @return HashMap of the names of each dropdown values and their corresponding
     * ID numbers in the database (ID, Name)
     */
    protected static HashMap<String, String> getDropdownValues(String dropdown) {
        HashMap<String, String> accountTypes = new HashMap<>();

        Query q = new Query();

        switch (dropdown) {
            case "accountTypes":
                q.updateQuery("SELECT accountTypeID, type\n" +
                        "FROM account_types;");
                break;
            case "locations":
                q.updateQuery("SELECT locationID, name\n" +
                        "FROM location;");
                break;
            case "equipmentTypes":
                q.updateQuery("SELECT equipmentTypeID, equipmentType\n" +
                        "FROM equipment_type");
                break;
            case "bikeTypes":
                q.updateQuery("SELECT bikeTypeID, bikeType\n" +
                        "FROM bike_type");
                break;
            default:
                return null;
        }

        Results res = q.executeQuery();

        //check if we have results
        if(!res.isEmpty()) {
            for(int r = 0; r < res.getRows(); r++) {
                accountTypes.put((String)res.getElement(r, 1), Integer.toString((int)res.getElement(r, 0)));
            }
        }

        return accountTypes;

    }

    private static Results query(String queryString) {
        Query q = new Query(queryString);
        return q.executeQuery();
    }

    private boolean executeQuery(String queryString) {
        Query q = new Query(queryString);
        return q.insertQuery();
    }
}
