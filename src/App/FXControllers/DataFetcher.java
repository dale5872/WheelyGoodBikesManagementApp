/** TODO: Implement fetching rentals */
/** TODO: Make memory more efficient, dont store a NEW location object per field. */
package App.FXControllers;

import App.Classes.*;
import DatabaseConnector.InsertFailedException;
import DatabaseConnector.Query;
import DatabaseConnector.Results;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class DataFetcher {

    /**
     * Uses the parameter string to fetch the data from the SQL server and
     * creates many EmployeeAccount objects and parses the data
     * into these objects. Then returns the list of these objects for
     * display into the table
     * @param params The HTTP POST params to send
     * @return A list of Accounts filled with the data in each account
     * Scope: Package-private (No modifier)
     */
    static ObservableList<EmployeeAccount> getEmployeeAccounts(String params) throws EmptyDatasetException{
        ObservableList<EmployeeAccount> accounts = FXCollections.observableArrayList();

        Query q = new Query("read", "fetchEmployeeAccounts", params);
        Results res = q.executeQuery();

        //check we have results
        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("Empty dataset", false);
        } else {
            //add data
            for(int r = 0; r < res.getRows(); r++) {
                EmployeeAccount acc = new EmployeeAccount();

                acc.setEmployeeID(Integer.parseInt((String)res.getElement(r,"employeeID")));
                acc.setUsername((String)res.getElement(r,"username"));
                acc.setFirstName((String)res.getElement(r,"firstName"));
                acc.setLastName((String)res.getElement(r,"lastName"));
                acc.setEmail((String)res.getElement(r,"workEmail"));
                acc.setPhoneNumber((String)res.getElement(r,"workTel"));
                acc.setAccType((String)res.getElement(r,"type"));
                Location loc = new Location(Integer.parseInt((String)res.getElement(r,"locationID")), (String)res.getElement(r,"locationName"));
                acc.setLocation(loc);
                acc.setUserID(Integer.parseInt((String)res.getElement(r,"userID")));

                accounts.add(acc);
            }
        }

        return accounts;
    }

    /**
     * Uses the parameter string to fetch the data from the SQL server and
     * creates many EmployeeAccount objects and parses the data
     * into these objects. Then returns the list of these objects for
     * display into the table
     * @param params The HTTP POST params to send
     * @return A list of Accounts filled with the data in each account
     * Scope: Package-private (No modifier)
     */
    static ObservableList<Account> getUserAccounts(String params) throws EmptyDatasetException {
        ObservableList<Account> accounts = FXCollections.observableArrayList();

        Query q = new Query("read", "fetchUserAccounts", params);
        Results res = q.executeQuery();

        //check we have results
        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("Empty dataset", false);
        } else {
            //add data
            for(int r = 0; r < res.getRows(); r++) {
                Account acc = new Account();

                acc.setUserID(Integer.parseInt((String)res.getElement(r,"userID")));
                acc.setUsername((String)res.getElement(r,"username"));
                acc.setFirstName((String)res.getElement(r,"firstName"));
                acc.setLastName((String)res.getElement(r,"lastName"));
                acc.setEmail((String)res.getElement(r,"email"));
                acc.setPhoneNumber((String)res.getElement(r,"telNumber"));
                acc.setAccType("User");
                accounts.add(acc);
            }
        }

        return accounts;
    }

    /**
     * Adds a new user to the database through the data from the @param acc object,
     * then returns the account's user id once added to the database and returns
     * that value
     * @param acc the Account object of the new user to be added into the database
     * @throws InsertFailedException Error Codes DF01-DF04
     * Scope: Package-private (No modifier)
     */
    static void addAccount(Account acc, String password, int accountType) throws InsertFailedException{
        if(acc instanceof EmployeeAccount) {
            String params = "username=" + acc.getUsername()
                    + "&password=" + password
                    + "&account_type=" + accountType
                    + "&location_id=" + ((EmployeeAccount) acc).getLocationID()
                    + "&first_name=" + acc.getFirstName()
                    + "&last_name=" + acc.getLastName()
                    + "&email=" + acc.getEmail()
                    + "&phone=" + acc.getPhoneNumber();
            Query q = new Query("create", "addEmployeeAccount", params);
            if(!q.insertQuery()) {
                throw new InsertFailedException("Failed to add user: " + acc.getUsername());
            }
        } else if(acc instanceof Account) {
            //standard user account
            String params = "username=" + acc.getUsername()
                    + "&password=" + password
                    + "&first_name=" + acc.getFirstName()
                    + "&last_name=" + acc.getLastName()
                    + "&email=" + acc.getEmail()
                    + "&phone=" + acc.getPhoneNumber();
            Query q = new Query("create", "addUserAccount", params);
            if(!q.insertQuery()) {
                throw new InsertFailedException("Failed to add user: " + acc.getUsername());
            }
        }
    }

    /**
     * Uses the @param newAcc to update the values in the database for the given user
     * @param oldAcc the account being replaced
     * @param newAcc the nre account
     * @param accountType account type
     * @throws InsertFailedException exception if failure to update the account
     * Scope: Package-private (No modifier)
     */
    static void updateAccount(Account oldAcc, Account newAcc, int accountType) throws InsertFailedException {
        //check if account info is the same
        //update user table for any account
        int userID = oldAcc.getUserID();
        //check if accounts are employee accounts
        if(newAcc instanceof EmployeeAccount) {
            //we have an employee account object
            //update employees
            String params = "username=" + newAcc.getUsername()
                    + "&account_type=" + accountType
                    + "&location_id=" + ((EmployeeAccount) newAcc).getLocationID()
                    + "&first_name=" + newAcc.getFirstName()
                    + "&last_name=" + newAcc.getLastName()
                    + "&email=" + newAcc.getEmail()
                    + "&phone=" + newAcc.getPhoneNumber()
                    + "&employee_id=" + ((EmployeeAccount)oldAcc).getEmployeeID()
                    + "&user_id=" + oldAcc.getUserID();
            Query q = new Query("update", "updateEmployeeAccount", params);
            if(!q.insertQuery()) {
                throw new InsertFailedException("Failed to update user: " + newAcc.getUsername());
            }

        } else if(newAcc instanceof Account) {
            //standard user account

            String params = "username=" + newAcc.getUsername()
                    + "&first_name=" + newAcc.getFirstName()
                    + "&last_name=" + newAcc.getLastName()
                    + "&email=" + newAcc.getEmail()
                    + "&phone=" + newAcc.getPhoneNumber()
                    + "&user_id=" + oldAcc.getUserID();
            Query q = new Query("update", "updateUserAccount", params);
            if(!q.insertQuery()) {
                throw new InsertFailedException("Failed to update user: " + newAcc.getUsername());
            }
        }
    }

    /**
     * Deletes a given user with the user ID and employeeID provided
     * @param userID ID of the user account
     * @param employeeID the employee ID
     * @throws InsertFailedException if failure to delete the user account and all of it's data
     * Scope: Package-private (No modifier)
     */
    static void deleteAccount(int userID, int employeeID) throws InsertFailedException {

        if(employeeID == -1) {
            //user account
            Query q = new Query("delete", "deleteUserAccount", "user_id=" + userID);
            if(!q.insertQuery()) {
                throw new InsertFailedException("Deletion of " + userID + " failed. Error code DF10");
            }
        } else if(employeeID >= 0) {
            Query q = new Query("delete", "deleteEmployeeAccount", "user_id=" + userID + "&employee_id=" + employeeID);
            if(!q.insertQuery()) {
                throw new InsertFailedException("Deletion of " + userID + " failed. Error code DF10");
            }
        } else {
            throw new InsertFailedException("Deletion failed. Error code DF11");

        }
    }

    /**
     * Fetches all the bikes from the database and returns this data as a list
     * @param managerLoc Location of the manager
     * @param searchParameters Any search parameters
     * @return List of Equipment objects (as bikes)
     * @throws EmptyDatasetException if empty
     */
    static ObservableList<Equipment> getBikes(Location managerLoc, String searchParameters) throws EmptyDatasetException {
        ObservableList<Equipment> equipment = FXCollections.observableArrayList();

        Query q = new Query();

        if(managerLoc == null) {
            q.updateQuery("read", "fetchBikes", searchParameters);
        } else {
            //get query based on location
            q.updateQuery("read", "fetchBikes", "location_id=" + managerLoc.getLocationID() + "&" + searchParameters);
        }

        Results res = q.executeQuery();

        //check we have results
        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("Empty Dataset: No equipment to return.", false);
        } else {
            for(int r = 0; r < res.getRows(); r++) {
                Equipment e = new Equipment();
                e.setID(Integer.parseInt((String) res.getElement(r, "bikeID")));
                e.setTypeID(Integer.parseInt((String)res.getElement(r,"bikeType")));
                e.setTypeName((String)res.getElement(r,"bikeName"));
                Location loc = new Location(Integer.parseInt((String)res.getElement(r,"location")), (String)res.getElement(r,"name"));
                e.setLocation(loc);
                e.setStatus((String)res.getElement(r,"bikeStatus"));
                e.setPrice(Float.parseFloat((String)res.getElement(r, "pricePerHour")));
                e.setCategory("Bike");
                equipment.add(e);
            }
        }

        return equipment;
    }

    /**
     * Adds a new bike with the specified location to the database
     * @param e Equipment object to be added
     * @throws InsertFailedException if failed to be added to the database
     * Scope: Package-private (No modifier)
     */
    static void addBike(Equipment e) throws InsertFailedException, EmptyDatasetException {
        if(e.getCategory().equals("Bike")) {
            Query q = new Query("create", "addBike", "bike_type=" + e.getTypeID() +
                    "&location_id=" + e.getLocationID() +
                    "&status=" + e.getStatus());

            if (q.insertQuery()) {
                //success
            } else {
                throw new InsertFailedException("Failed to add new equipment of type " + e.getTypeName());
            }
        } else {
            throw new InsertFailedException("Failed to add new equipment of type " + e.getTypeName());
        }
    }

    /**
     * Updates the bike in the database
     * @param e Equipment class to update in the database
     * @throws InsertFailedException if failed to update
     * Scope: Package-private (No modifier)
     */
    static void updateBike(Equipment e) throws InsertFailedException {
        if(e.getCategory().equals("Bike")) {
            Query q = new Query("update", "updateBike", "bike_type=" + e.getTypeID() +
                    "&location_id=" + e.getLocationID() +
                    "&status=" + e.getStatus() +
                    "&equipment_id=" + e.getID());

            if (!q.insertQuery()) {
                throw new InsertFailedException("Failed to change getEquipment " + e.getID());
            }
        } else {
            throw new InsertFailedException("Failed to add new getEquipment of type " + e.getTypeName());
        }
    }

    /**
     * Deletes the selected equipment from the database
     * @param e Equipment object to delete
     * @throws InsertFailedException if failed to delete
     */
    static void deleteBike(Equipment e) throws InsertFailedException {
        if(e.getCategory().equals("Bike")) {
            Query q = new Query("delete", "deleteBike", "bike_id=" + e.getID());

            if (!q.insertQuery()) {
                throw new InsertFailedException("Deleting getEquipment " + e.getID() + " failed." +
                        "\n" +
                        "Hints: There may still be an active rental using this " + e.getTypeName() + "" +
                        "\nWait for rental to complete, or mark rental as complete." +
                        "\nCannot remove until solved.");
            }
        } else {
            throw new InsertFailedException("Failed to add new getEquipment of type " + e.getTypeName());
        }
    }


    /**
     * Return all the equipment data from the database with the given query
     * @param managerLoc returns the dataset based on the managers location, null if operator requesting
     * @return list of Equipment objects
     * @throws EmptyDatasetException if empty
     * Scope: Package-private (No modifier)
     */
    static ObservableList<Equipment> getEquipment(Location managerLoc, String searchParameters) throws EmptyDatasetException {
        ObservableList<Equipment> equipment = FXCollections.observableArrayList();

        Query q = new Query();

        if(managerLoc == null) {
            q.updateQuery("read", "fetchEquipment", searchParameters);
        } else {
            //get query based on location
            q.updateQuery("read", "fetchEquipment", "location_id=" + managerLoc.getLocationID() + "&" + searchParameters);
        }

        Results res = q.executeQuery();

        //check we have results
        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("Empty Dataset: No getEquipment to return.", false);
        } else {
            for(int r = 0; r < res.getRows(); r++) {
                Equipment e = new Equipment();
                e.setID(Integer.parseInt((String) res.getElement(r, "equipmentID")));
                e.setTypeID(Integer.parseInt((String)res.getElement(r,"equipmentType")));
                e.setTypeName((String)res.getElement(r,"equipmentName"));
                Location loc = new Location(Integer.parseInt((String)res.getElement(r,"location")), (String)res.getElement(r,"name"));
                e.setLocation(loc);
                e.setStatus((String)res.getElement(r,"equipmentStatus"));
                e.setPrice(Float.parseFloat((String)res.getElement(r, "pricePerHour")));
                e.setCategory("Equipment");
                equipment.add(e);
            }
        }

        return equipment;
    }


    /**
     * Adds a new equipment with the specified location to the database
     * @param e Equipment object to be added
     * @throws InsertFailedException if failed to be added to the database
     * Scope: Package-private (No modifier)
     */
    static void addEquipment(Equipment e) throws InsertFailedException, EmptyDatasetException {
        if(e.getCategory().equals("Equipment")) {
            Query q = new Query("create", "addEquipment", "equipment_type=" + e.getTypeID() +
                    "&location_id=" + e.getLocationID() +
                    "&status=" + e.getStatus());

            if (q.insertQuery()) {
                //success
            } else {
                throw new InsertFailedException("Failed to add new getEquipment of type " + e.getTypeName());
            }
        }
    }

    /**
     * Updates the equipment in the database
     * @param e Equipment class to update in the database
     * @throws InsertFailedException if failed to update
     * Scope: Package-private (No modifier)
     */
    static void updateEquipment(Equipment e) throws InsertFailedException {
        if(e.getCategory().equals("Equipment")) {
            Query q = new Query("update", "updateEquipment", "equipment_type=" + e.getTypeID() +
                    "&location_id=" + e.getLocationID() +
                    "&status=" + e.getStatus() +
                    "&equipment_id=" + e.getID());

            if (!q.insertQuery()) {
                throw new InsertFailedException("Failed to change equipment " + e.getID());
            }
        } else {
            throw new InsertFailedException("Failed to add new equipment of type " + e.getTypeName());
        }
    }

    /**
     * Deletes the selected equipment from the database
     * @param e Equipment object to delete
     * @throws InsertFailedException if failed to delete
     */
    static void deleteEquipment(Equipment e) throws InsertFailedException {
        if(e.getCategory().equals("Equipment")) {
            Query q = new Query("delete", "deleteEquipment", "equipment_id=" + e.getID());

            if (!q.insertQuery()) {
                throw new InsertFailedException("Deleting equipment " + e.getID() + " failed." +
                        "\n" +
                        "Hints: There may still be an active rental using this " + e.getTypeName() + "" +
                        "\nWait for rental to complete, or mark rental as complete." +
                        "\nCannot remove until solved.");
            }
        } else {
            throw new InsertFailedException("Failed to add new equipment of type " + e.getTypeName());
        }
    }

    /**
     * Return all of the locations and its data that is in the database
     * @return the list of Location objects
     * Scope: Package-private (No modifier)
     */
    static ObservableList<Location> getLocations(String searchParameters) throws EmptyDatasetException{
        ObservableList<Location> locations = FXCollections.observableArrayList();


        Query q = new Query("read", "fetchLocations", searchParameters);
        Results res = q.executeQuery();

        //check we have results
        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("Empty Dataset: Could not list of getLocations", false);
        } else {
            for(int r = 0; r < res.getRows(); r++) {
                locations.add(new Location(Integer.parseInt((String)res.getElement(r, "locationID")), (String)res.getElement(r,"name")));
            }
        }

        return locations;
    }

    /**
     * Adds a new location to the database
     * @param locationName name of the new location
     * @return true if succeed, false if failed
     * @throws InsertFailedException if failure and shows message to user
     * Scope: Package-private (No modifier)
     */
    static boolean addLocation(String locationName) throws InsertFailedException {
        Query q = new Query("create", "addLocation", "location_name=" + locationName);

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
     * Scope: Package-private (No modifier)
     */
    static boolean editLocation(Location newLoc) throws InsertFailedException {
        Query q = new Query("update", "updateLocation", "location_name=" + newLoc.getName() + "&location_id=" + newLoc.getLocationID());

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
    static boolean deleteLocation(Location loc) throws InsertFailedException {
        Query q = new Query("delete", "deleteLocation", "location_id=" + loc.getLocationID());

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


    /**
     *
     * TODO Implement retrieving rentals
     */
    protected static ObservableList<Rental> rentals() {
        return null;
    }

    /**
     * Returns a HashMap of the values needed for the dropdown values in the
     * add / edit forms
     * @param dropdown "accountTypes" or "locations" are acceptable inputs
     * @return HashMap of the names of each dropdown values and their corresponding
     * ID numbers in the database (ID, Name)
     * Scope: Package-private (No modifier)
     */
    static HashMap<String, String> getDropdownValues(String dropdown) {


        HashMap<String, String> accountTypes = new HashMap<>();

        Query q = new Query();
        Results res;
        String id;
        String name;

        switch (dropdown) {
            case "accountTypes":
                q.updateQuery("read", "fetchAccountTypes", "");
                id = "accountTypeID";
                name = "type";
                break;
            case "getLocations":
                q.updateQuery("read", "fetchLocations", "");
                id = "locationID";
                name = "name";
                break;
            case "equipmentTypes":
                q.updateQuery("read", "fetchEquipmentTypes", "");
                id = "equipmentTypeID";
                name = "equipmentType";
                break;
            case "bikeTypes":
                q.updateQuery("read", "fetchBikeTypes", "");
                id = "bikeTypeID";
                name = "bikeType";
                break;
            default:
                return null;
        }

        res = q.executeQuery();

        //check if we have results
        if(!res.isEmpty()) {
            for(int r = 0; r < res.getRows(); r++) {
                accountTypes.put((String)res.getElement(r, name), (String)res.getElement(r, id));
            }
        }

        return accountTypes;
    }
}