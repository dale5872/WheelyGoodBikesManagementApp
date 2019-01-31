//TODO: Insert an EmptyDataSetException
//TODO: Implement fetching rentals
package App.FXControllers;

import App.Classes.EmployeeAccount;
import App.Classes.Equipment;
import App.Classes.Location;
import App.Classes.Rental;
import DatabaseConnector.Query;
import DatabaseConnector.Results;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

                accounts.add(acc);
            }
        } else {
            //throw an exception
        }

        return accounts;
    }

    protected static ObservableList<Equipment> equipment(String queryString) {
        ObservableList<Equipment> equipment = FXCollections.observableArrayList();

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
                e.setStatus((String)res.getElement(r,4));
                e.setPrice((float)res.getElement(r, 5));

                equipment.add(e);
            }
        } else {
            //throw an exception
        }

        return equipment;
    }

    protected static ObservableList<Location> locations(String queryString) {
        ObservableList<Location> locations = FXCollections.observableArrayList();

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

    private static Results query(String queryString) {
        Query q = new Query(queryString);
        return q.executeQuery();
    }
}
