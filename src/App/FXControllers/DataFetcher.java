package App.FXControllers;

import App.Classes.*;
import DatabaseConnector.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    static ObservableList<EmployeeAccount> getEmployeeAccounts(String params, ObservableList<Location> locations) throws EmptyDatasetException{
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

                /* Get location */
                String locationName = (String) res.getElement(r,"locationName");
                Location loc;
                try {
                    loc = OptionsList.findLocationByName(locations, locationName);
                    acc.setLocation(loc);
                }catch(ListItemNotFoundException ex){
                    new ShowMessageBox().show("An error has occurred: location " + locationName + " could not be found. Accounts could not be loaded.");
                    throw new EmptyDatasetException("Empty Dataset: No accounts to return.", true);
                }

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
     * Returns a single user account information by their ID
     * @param id userID
     * @return Account object with the users data
     * @throws EmptyDatasetException if the user cannot be found
     */
    static Account getUser(int id) throws EmptyDatasetException {
        Query q = new Query("read", "fetchUserAccounts", "user_id=" + id);
        Results res = q.executeQuery();

        //check we have results
        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("Empty dataset", false);
        } else {
            //add data
            Account acc = new Account();

            acc.setUserID(Integer.parseInt((String)res.getElement(0,"userID")));
            acc.setUsername((String)res.getElement(0,"username"));
            acc.setFirstName((String)res.getElement(0,"firstName"));
            acc.setLastName((String)res.getElement(0,"lastName"));
            acc.setEmail((String)res.getElement(0,"email"));
            acc.setPhoneNumber((String)res.getElement(0,"telNumber"));
            acc.setAccType("User");
            return acc;
        }
    }

    /**
     * Suspends a user with their ID number and supplies a reason for doing so
     * @param id users ID number
     * @param reason reason given
     * @throws InsertFailedException could not suspend user
     */
    static void suspendUser(int id, String reason) throws InsertFailedException {
        Query q = new Query("create", "addSuspendedUser", "user_id=" + id + "&reason=" + reason);

        if(!q.insertQuery()) {
            throw new InsertFailedException("Failed to suspend User");
        }
    }

    /**
     * Unsuspends user with the given id
     * @param id users ID number
     * @throws EmptyDatasetException Could not find suspended user
     */
    static void unsuspendUser(int id) throws EmptyDatasetException {
        Query q = new Query("delete", "unsuspendUser", "user_id=" + id);

        if(!q.insertQuery()) {
            throw new EmptyDatasetException("Could not find the user", true);
        }
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
                    + "&location_id=" + ((EmployeeAccount) acc).getLocation().getLocationID()
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
                    + "&location_id=" + ((EmployeeAccount) newAcc).getLocation().getLocationID()
                    + "&first_name=" + newAcc.getFirstName()
                    + "&last_name=" + newAcc.getLastName()
                    + "&email=" + newAcc.getEmail()
                    + "&phone=" + newAcc.getPhoneNumber()
                    + "&employee_id=" + ((EmployeeAccount)oldAcc).getEmployeeID()
                    + "&user_id=" + oldAcc.getUserID()
                    + "&profile_picture=" + newAcc.getProfilePicture();
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

    static void updateEmployeePassword(EmployeeAccount acc, String password) throws InsertFailedException {
        Query q = new Query("update", "updateEmployeePassword", "user_id=" + acc.getUserID() + "&password=" + password);
        if(!q.insertQuery()) {
            throw new InsertFailedException("Failed to update " + acc.getUsername() + "'s password.");
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
     * @param types A list of all types
     * @return List of Equipment objects (as bikes)
     * @throws EmptyDatasetException if empty
     */
    @SuppressWarnings("Duplicates")
    static ObservableList<Equipment> getBikes(Location managerLoc, String searchParameters,
                                              ObservableList<Type> types, ObservableList<Location> locations) throws EmptyDatasetException {
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
            throw new EmptyDatasetException("Empty Dataset: No bikes to return.", false);
        } else {
            for(int r = 0; r < res.getRows(); r++) {
                Equipment e = new Equipment();
                e.setID(Integer.parseInt((String) res.getElement(r, "bikeID")));

                /* Get type */
                String typeName = (String) res.getElement(r,"bikeName");
                Type type;
                try{
                    type = OptionsList.findTypeByName(types, typeName);
                    e.setType(type);
                }catch(ListItemNotFoundException ex){
                    new ShowMessageBox().show("An error has occurred: type " + typeName + " could not be found. Bikes could not be loaded.");
                    throw new EmptyDatasetException("Empty Dataset: No bikes to return.", true);
                }

                /* Get location */
                String locationName = (String) res.getElement(r,"name");
                Location loc;
                try {
                    loc = OptionsList.findLocationByName(locations, locationName);
                    e.setLocation(loc);
                }catch(ListItemNotFoundException ex){
                    new ShowMessageBox().show("An error has occurred: location " + locationName + " could not be found. Bikes could not be loaded.");
                    throw new EmptyDatasetException("Empty Dataset: No bikes to return.", true);
                }

                e.setStatus((String)res.getElement(r,"bikeStatus"));
                e.setCategory("Bike");
                equipment.add(e);
            }
        }

        return equipment;
    }

    /**
     * Returns a single Equipment object containing all the information of a selected bike
     * @param id the bikeID
     * @return Equipment object with all the bikes information
     * @throws EmptyDatasetException if the bike cannot be found
     */
    static Equipment getBike(int id, ObservableList<Type> types, ObservableList<Location> locations) throws EmptyDatasetException {
        Query q = new Query();

        //get query based on location
        q.updateQuery("read", "fetchBike", "bike_id=" + id);

        Results res = q.executeQuery();

        //check we have results
        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("Empty Dataset: No equipment to return.", false);
        } else {
            Equipment e = new Equipment();
            e.setID(Integer.parseInt((String) res.getElement(0, "bikeID")));

            /* Get type */
            String typeName = (String)res.getElement(0,"bikeName");
            Type type;
            try{
                type = OptionsList.findTypeByName(types, typeName);
                e.setType(type);
            }catch(ListItemNotFoundException ex){
                new ShowMessageBox().show("An error has occurred: type " + typeName + " could not be found. Bike could not be loaded.");
                throw new EmptyDatasetException("Empty Dataset: No bikes to return.", true);
            }

            /* Get location */
            String locationName = (String) res.getElement(0,"name");
            Location loc;
            try {
                loc = OptionsList.findLocationByName(locations, locationName);
                e.setLocation(loc);
            }catch(ListItemNotFoundException ex){
                new ShowMessageBox().show("An error has occurred: location " + locationName + " could not be found. Bike could not be loaded.");
                throw new EmptyDatasetException("Empty Dataset: No bikes to return.", true);
            }

            e.setStatus((String)res.getElement(0,"bikeStatus"));
            e.setCategory("Bike");
            return e;
        }

    }

    /**
     * Gets all the bike types currently in operation in the system
     * @return ObservableList of all the types
     * @throws EmptyDatasetException if there are no types in operation
     */
    static ObservableList<Type> getBikeTypes(String search) throws EmptyDatasetException {
        ObservableList<Type> type = FXCollections.observableArrayList();

        Query q = new Query();
        Results res = q.executeQuery("read", "fetchBikeTypes", "return_type=table&search=" + search);

        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("Empty Dataset: No bike types to return", false);
        } else {
            for(int r = 0; r < res.getRows(); r++) {
                int typeID = Integer.parseInt((String)res.getElement(r, "bikeTypeID"));
                Type t = new Type(typeID);
                t.setName((String)res.getElement(r, "bikeType"));
                t.setPrice(Double.parseDouble((String)res.getElement(r, "pricePerHour")));
                t.setImage((String)res.getElement(r, "image"));
                type.add(t);
            }
        }
        return type;
    }

    /**
     * Adds a new bike with the specified location to the database
     * @param e Equipment object to be added
     * @throws InsertFailedException if failed to be added to the database
     * Scope: Package-private (No modifier)
     */
    static void addBike(Equipment e) throws InsertFailedException, EmptyDatasetException {
        if(e.getCategory().equals("Bike")) {
            Query q = new Query("create", "addBike", "bike_type=" + e.getType().getID() +
                    "&location_id=" + e.getLocation().getLocationID() +
                    "&status=" + e.getStatus());

            if (q.insertQuery()) {
                //success
            } else {
                throw new InsertFailedException("Failed to add new equipment of type " + e.getType().getName());
            }
        } else {
            throw new InsertFailedException("Failed to add new equipment of type " + e.getType().getName());
        }
    }

    /**
     * Adds new bikeType with the data entered in the params parameter
     * @param type The Type to be added
     * @throws InsertFailedException if insert failed
     */
    static void addBikeType(Type type) throws InsertFailedException {
        Query q = new Query("create", "addBikeType", "bike_type=" + type.getName()
                + "&pricePerHour=" + type.getPrice()
                + "&image=" + type.getImage());

        if(!q.insertQuery()) {
            throw new InsertFailedException("Failed to create new equipment type");
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
            Query q = new Query("update", "updateBike", "bike_type=" + e.getType().getID() +
                    "&location_id=" + e.getLocation().getLocationID() +
                    "&status=" + e.getStatus() +
                    "&equipment_id=" + e.getID());

            if (!q.insertQuery()) {
                throw new InsertFailedException("Failed to change getEquipment " + e.getID());
            }
        } else {
            throw new InsertFailedException("Failed to add new getEquipment of type " + e.getType().getName());
        }
    }

    /**
     * Updates the selected bike type in the database
     * @param type The type to update
     */
    static void updateBikeType(Type type) throws InsertFailedException{
        String params = "bike_type=" + type.getName() + "&pricePerHour=" + type.getPrice() + "&image=" + type.getImage() + "&bike_type_id=" + type.getID();
        Query q = new Query("update", "updateBikeType", params);

        if(!q.insertQuery()) {
            throw new InsertFailedException("Could not update bike type with ID: " + type.getID());
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
                        "Hints: There may still be an active rental using this " + e.getType().getName() + "" +
                        "\nWait for rental to complete, or mark rental as complete." +
                        "\nCannot remove until solved.");
            }
        } else {
            throw new InsertFailedException("Failed to add new getEquipment of type " + e.getType().getName());
        }
    }

    /**
     * Deletes an Bike with the ID number passed into the function
     * @param type Type to delete
     * @throws InsertFailedException If the deletion failed
     */
    static void deleteBikeType(Type type) throws InsertFailedException {
        Query q = new Query("delete", "deleteBikeType", "bike_type="+ type.getID());

        if(!q.insertQuery()) {
            throw new InsertFailedException("Failed to delete bike type with the ID: " + type.getID());
        }
    }

    /**
     * Return all the equipment data from the database with the given query
     * @param managerLoc returns the dataset based on the managers location, null if operator requesting
     * @return list of Equipment objects
     * @throws EmptyDatasetException if empty
     * Scope: Package-private (No modifier)
     */
    @SuppressWarnings("Duplicates")
    static ObservableList<Equipment> getEquipment(Location managerLoc, String searchParameters,
                                                  ObservableList<Type> types, ObservableList<Location> locations) throws EmptyDatasetException {
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
            throw new EmptyDatasetException("Empty Dataset: No equipment to return.", false);
        } else {
            for(int r = 0; r < res.getRows(); r++) {
                Equipment e = new Equipment();
                e.setID(Integer.parseInt((String) res.getElement(r, "equipmentID")));

                /* Get type */
                String typeName = (String) res.getElement(r,"equipmentName");
                Type type;
                try{
                    type = OptionsList.findTypeByName(types, typeName);
                    e.setType(type);
                }catch(ListItemNotFoundException ex){
                    new ShowMessageBox().show("An error has occurred: type " + typeName + " could not be found. Equipment could not be loaded.");
                    throw new EmptyDatasetException("Empty Dataset: No equipment to return.", true);
                }

                /* Get location */
                String locationName = (String) res.getElement(r,"name");
                Location loc;
                try {
                    loc = OptionsList.findLocationByName(locations, locationName);
                    e.setLocation(loc);
                }catch(ListItemNotFoundException ex){
                    new ShowMessageBox().show("An error has occurred: location " + locationName + " could not be found. Equipment could not be loaded.");
                    throw new EmptyDatasetException("Empty Dataset: No equipment to return.", true);
                }

                e.setStatus((String)res.getElement(r,"equipmentStatus"));
                e.setCategory("Equipment");
                equipment.add(e);
            }
        }

        return equipment;
    }

    /**
     * Gets all the equipment types currently in operation in the system
     * @return ObservableList of all the types
     * @throws EmptyDatasetException if there are no types in operation
     */
    static ObservableList<Type> getEquipmentTypes(String search) throws EmptyDatasetException {
        ObservableList<Type> type = FXCollections.observableArrayList();

        Query q = new Query();
        Results res = q.executeQuery("read", "fetchEquipmentTypes", "return_type=table&search=" + search);

        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("Empty Dataset: No equipment types to return.", false);
        } else {
            for(int r = 0; r < res.getRows(); r++) {
                int typeID = Integer.parseInt((String)res.getElement(r, "equipmentTypeID"));
                Type t = new Type(typeID);
                t.setName((String)res.getElement(r, "equipmentType"));
                t.setPrice(Double.parseDouble((String)res.getElement(r, "pricePerHour")));
                t.setImage((String)res.getElement(r, "image"));
                type.add(t);
            }
        }
        return type;
    }

    /**
     * Returns a single Equipment object containing all the information of a selected bike
     * @param id the bikeID
     * @return Equipment object with all the bikes information
     * @throws EmptyDatasetException if the bike cannot be found
     */
    static Equipment getSingleEquipment(int id, ObservableList<Type> types, ObservableList<Location> locations) throws EmptyDatasetException {
        Query q = new Query();

        //get query based on location
        q.updateQuery("read", "fetchSingleEquipment", "equipment_id=" + id);

        Results res = q.executeQuery();

        //check we have results
        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("Empty Dataset: No equipment to return.", false);
        } else {
            Equipment e = new Equipment();
            e.setID(Integer.parseInt((String) res.getElement(0, "equipmentID")));

            /* Get type */
            String typeName = (String)res.getElement(0,"equipmentName");
            Type type;
            try{
                type = OptionsList.findTypeByName(types, typeName);
                e.setType(type);
            }catch(ListItemNotFoundException ex){
                new ShowMessageBox().show("An error has occurred: type " + typeName + " could not be found. Equipment could not be loaded.");
                throw new EmptyDatasetException("Empty Dataset: No Equipment to return.", true);
            }

            /* Get location */
            String locationName = (String) res.getElement(0,"name");
            Location loc;
            try {
                loc = OptionsList.findLocationByName(locations, locationName);
                e.setLocation(loc);
            }catch(ListItemNotFoundException ex){
                new ShowMessageBox().show("An error has occurred: location " + locationName + " could not be found. Equipment could not be loaded.");
                throw new EmptyDatasetException("Empty Dataset: No equipment to return.", true);
            }

            e.setStatus((String)res.getElement(0,"equipmentStatus"));
            e.setCategory("Equipment");
            return e;
        }

    }


    /**
     * Adds a new equipment with the specified location to the database
     * @param e Equipment object to be added
     * @throws InsertFailedException if failed to be added to the database
     * Scope: Package-private (No modifier)
     */
    static void addEquipment(Equipment e) throws InsertFailedException, EmptyDatasetException {
        if(e.getCategory().equals("Equipment")) {
            Query q = new Query("create", "addEquipment", "equipment_type=" + e.getType().getID() +
                    "&location_id=" + e.getLocation().getLocationID() +
                    "&status=" + e.getStatus());

            if (q.insertQuery()) {
                //success
            } else {
                throw new InsertFailedException("Failed to add new getEquipment of type " + e.getType().getName());
            }
        }
    }

    /**
     * Adds new equipmentType with the data entered in the params parameter
     * @param type The type to add
     * @throws InsertFailedException if insert failed
     */
    static void addEquipmentType(Type type) throws InsertFailedException {
        Query q = new Query("create", "addEquipmentType", "equipment_type=" + type.getName()
                + "&pricePerHour=" + type.getPrice()
                + "&image=" + type.getImage());

        if(!q.insertQuery()) {
            throw new InsertFailedException("Failed to create new equipment type");
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
            Query q = new Query("update", "updateEquipment", "equipment_type=" + e.getType().getID() +
                    "&location_id=" + e.getLocation().getLocationID() +
                    "&status=" + e.getStatus() +
                    "&equipment_id=" + e.getID());

            if (!q.insertQuery()) {
                throw new InsertFailedException("Failed to change equipment " + e.getID());
            }
        } else {
            throw new InsertFailedException("Failed to add new equipment of type " + e.getType().getName());
        }
    }

    /**
     * Updates the selected equipment type in the database
     * @param type The type to update
     */
    static void updateEquipmentType(Type type) throws InsertFailedException{
        String params = "equipment_type=" + type.getName() + "&pricePerHour=" + type.getPrice() + "&image=" + type.getImage() + "&equipment_type_id=" + type.getID();
        Query q = new Query("update", "updateEquipmentType", params);

        if(!q.insertQuery()) {
            throw new InsertFailedException("Could not update equipment type with ID: " + type.getID());
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
                        "Hints: There may still be an active rental using this " + e.getType().getName() + "" +
                        "\nWait for rental to complete, or mark rental as complete." +
                        "\nCannot remove until solved.");
            }
        } else {
            throw new InsertFailedException("Failed to add new equipment of type " + e.getType().getName());
        }
    }

    /**
     * Deletes an equipment with the ID number passed into the function
     * @param type The type to delete
     * @throws InsertFailedException If the deletion failed
     */
    static void deleteEquipmentType(Type type) throws InsertFailedException {
        Query q = new Query("delete", "deleteEquipmentType", "bike_type="+ type.getID());

        if(!q.insertQuery()) {
            throw new InsertFailedException("Failed to delete equipment with the ID: " + type.getID());
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
            throw new EmptyDatasetException("Empty Dataset: Could not get list of locations", false);
        } else {
            for(int r = 0; r < res.getRows(); r++) {
                Location loc = new Location();

                loc.setLocationID(Integer.parseInt((String) res.getElement(r, "locationID")));
                loc.setName((String) res.getElement(r,"name"));

                locations.add(loc);
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
     * Gets the bike rentals for the given location
     * @param managerLoc current Manager Location object
     * @param params search parameters
     * @param types List of bike types
     * @param locations list of locations
     * @return a list of Rentals
     * @throws EmptyDatasetException if there is no data to retrieve
     * @throws InvalidParametersException if the parameters for the query are invalid
     */
    static ObservableList<Rental> getBikeRentals(Location managerLoc, String params,
                                                 ObservableList<Type> types, ObservableList<Location> locations)
            throws EmptyDatasetException, InvalidParametersException {
        String searchParameters = "location_id=" + managerLoc.getLocationID() + "&search=" + params;
        Query q = new Query("read", "fetchBikeRentals", searchParameters);
        Results res = q.executeQuery();

        return fillRentalObject(res, types, locations, true);
    }

    /**
     * Gets the equipment rentals for the given location
     * @param managerLoc current Manager Location object
     * @param params search parameters
     * @param types List of equipment types
     * @param locations list of locations
     * @return a list of Rentals
     * @throws EmptyDatasetException if there is no data to retrieve
     * @throws InvalidParametersException if the parameters for the query are invalid
     */
    static ObservableList<Rental> getEquipmentRentals(Location managerLoc, String params, ObservableList<Type> types, ObservableList<Location> locations) throws EmptyDatasetException, InvalidParametersException {

        String searchParameters = "location_id=" + managerLoc.getLocationID() + "&search=" + params;
        Query q = new Query("read", "fetchEquipmentRentals", searchParameters);
        Results res = q.executeQuery();

        return fillRentalObject(res, types, locations, false);
    }

    /**
     * Fills the Rentals list with the data from the Results objects passed into the method
     * @param res results from the query of bikeRentals or equipmentRentals
     * @param types list of bike or equipment types
     * @param locations list of locations
     * @return ObservableList of Rental objects
     * @throws InvalidParametersException If the parameters are incorrect
     * @throws EmptyDatasetException If there are no data to retrieve
     */
    static ObservableList<Rental> fillRentalObject(Results res, ObservableList<Type> types, ObservableList<Location> locations, boolean isBike) throws InvalidParametersException, EmptyDatasetException {
        ObservableList<Rental> rentals = FXCollections.observableArrayList();

        //check we have results
        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("Empty Dataset: Could not get list of rentals", false);
        } else {
            for(int r = 0; r < res.getRows(); r++) {
                Rental newRental = new Rental();
                //fill data
                //check if its a bike or equipment
                if(isBike) {
                    newRental.setID(Integer.parseInt((String) res.getElement(r, "bikeRentalID")));
                    newRental.setEquipment(getBike(Integer.parseInt((String)res.getElement(r, "bikeID")), types, locations));
                } else {
                    /** IMPLEMENT getSingleEquipment **/
                    newRental.setID(Integer.parseInt((String) res.getElement(r, "equipmentRentalID")));
                    newRental.setEquipment(getSingleEquipment(Integer.parseInt((String)res.getElement(r, "equipmentID")), types, locations));
                }
                newRental.setUser(getUser(Integer.parseInt((String)res.getElement(r, "userID"))));
                newRental.setStatus((String)res.getElement(r, "status"));
                //Get start and return times
                try {
                    String date = (String) res.getElement(r, "startTime");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date start = dateFormat.parse(date);
                    newRental.setStartTime(start);
                } catch (ParseException e) {
                    throw new InvalidParametersException("Could not parse start time!");
                }

                try {
                    String date = (String) res.getElement(r, "returnTime");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date start = dateFormat.parse(date);
                    newRental.setReturnTime(start);
                } catch (ParseException e) {
                    throw new InvalidParametersException("Could not parse return time!");
                }

                //check if cost has been calculated, if so add it
                if(res.getElement(r, "cost") == null || (res.getElement(r, "cost")).equals("")) {
                    newRental.calculateCost();
                } else {
                    newRental.setCost(Double.parseDouble((String)res.getElement(r, "cost")));
                }

                //Get equipment / bike that was rented
                rentals.add(newRental);
            }
        }
        return rentals;
    }

    static Results getReport(String report, String params) throws EmptyDatasetException {
        Query q = new Query("reports", report, params);
        Results res = q.executeQuery();

        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("No report to fetch", true);
        } else {
            return res;
        }
    }

    static Results getSavedReport(String report, int location, String filename) throws EmptyDatasetException {
        Query q = new Query("reports", "fetchStoredReport", "report=" + report + "&location_id=" + location + "&filename=" + filename);
        Results res = q.executeQuery();

        if(res == null || res.isEmpty()) {
            throw new EmptyDatasetException("No report to fetch", true);
        } else {
            return res;
        }
    }

    /**
     * Returns a HashMap of account types
     * @return HashMap of the name of each account type and its corresponding
     * ID numbers in the database (ID, Name)
     * Scope: Package-private (No modifier)
     */
    static HashMap<String, String> getAccountTypes() {
        HashMap<String, String> accountTypes = new HashMap<>();

        Query q = new Query();
        q.updateQuery("read", "fetchAccountTypes", "");

        Results res;
        res = q.executeQuery();

        //check if we have results
        if(!res.isEmpty()) {
            for(int r = 0; r < res.getRows(); r++) {
                accountTypes.put((String)res.getElement(r, "type"), (String)res.getElement(r, "accountTypeID"));
            }
        }

        return accountTypes;
    }

    /**
     * Gets the dropdown values for the stored reports based on the report type
     * @param report Report type
     * @param location location to get the reports from
     * @return HashMap of the reports
     */
    static HashMap<String, String> getFilenameDropdownValues(String report, int location) {
        HashMap<String, String> filenames = new HashMap<>();

        Query q = new Query();
        Results res;
        try{
            res = q.executeQuery("reports", "fetchStoredReportFilenames", "report=" + report + "&location_id=" + location);
        }catch(Exception ex){
            return null;
        }

        String[] headers = res.getHeaders();

        if(!res.isEmpty()) {
            for(int r = 0; r < res.getCols(); r++) {
                filenames.put((String)res.getElement(0, r), headers[r]);
            }
        }

        return filenames;
    }

    /**
     * Takes the local path and fetches the file to upload, then sends
     * the file to the server script to store on the server
     * @param filePath local path to the file
     * @throws InsertFailedException if failed to upload
     */
    static String uploadFile(String filePath) throws InsertFailedException {
        if(filePath.startsWith("http:")){
            if(isValidImageUrl(filePath)){
                return filePath;
            }else{
                throw new InsertFailedException("Could not update image: " + filePath + " is not a valid URL/image.");
            }
        }else{
            try{
                //Uploads the file and sets the URL to the path on the server
                return UploadFile.uploadFile(filePath);
            }catch(FileNotFoundException | HTTPErrorException e){
                throw new InsertFailedException(e.getMessage());
            }
        }
    }

    private static boolean isValidImageUrl(String url){
        return url.endsWith(".png")
                || url.endsWith(".jpg")
                || url.endsWith(".jpeg");
    }
}