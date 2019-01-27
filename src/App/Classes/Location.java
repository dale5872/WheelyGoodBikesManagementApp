//TODO: Get all equipment available (not bikes) for certain locations
//TODO: Memory saving so object is not always holding database data
package App.Classes;
import DatabaseConnector.Query;
import DatabaseConnector.Results;
import DatabaseConnector.InsertFailedException;

import java.util.ArrayList;
import java.util.List;


public class Location {
    private int locationID;
    private String name;

    private List<Equipment> bikes = new ArrayList<>();
    private int totalBikes;
    private int availableBikes;
    private int bookedBikes;
    private int damagedBikes;


    public Location(int locationID, String name) {
        this.locationID = locationID;
        this.name = name;
        this.totalBikes = 0;
        this.availableBikes = 0;
        this.bookedBikes = 0;
        this.damagedBikes = 0;
    }

    public int getLocationID() {
        return this.locationID;
    }

    public String getName() {
        return this.name;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Equipment> forceGetBikes() {
        bikes = new ArrayList<Equipment>();
        retrieveBikesFromDatabase();
        return bikes;
    }

    public List<Equipment> getBikes() {
        if(bikes.size() == 0)
            retrieveBikesFromDatabase();
        return bikes;
    }

    /**
     * Method loads in the bikes that are in this location, stores them into a list
     * that can be accessed through the above getter methods.
     */
    private void retrieveBikesFromDatabase() {
        String q = "SELECT bike_stock.bikeID, bike_stock.bikeType, bike_stock.bikeStatus, bike_type.pricePerHour, bike_type.image\n" +
                "FROM bike_stock\n" +
                "INNER JOIN bike_type ON bike_stock.bikeType = bike_type.bikeType\n" +
                "WHERE location = "+this.locationID+";";
        Query query = new Query(q);
        Results res = query.executeQuery();

        // cols should be 4
        // bikeID, bikeType, bikeStatus, pricePerHour, image
        int rows = res.getRows();

        Object[] row;
        for(int r = 0; r < rows; r++) {
            row = res.getNextRow();
            bikes.add(new Equipment(row));
            switch((String)row[2]) {
                case "Available":
                    availableBikes++;
                    break;
                case "Booked":
                    bookedBikes++;
                    break;
                case "Damaged":
                    damagedBikes++;
                    break;
            }
            totalBikes++;
        }
    }

    /**
     * @param b - Equipment object that holds 'some' details about the new bike, many details will be added
     *          on insert into the database.
     * Adds new bike into the database and then calls forceGetBikes() to reload
     * the bike data for this object / location
     */
    public void addNewBike(Equipment b) throws InsertFailedException {
        //insert into database
        String q = "INSERT INTO `project`.`bike_stock` (`bikeType`, `location`, `bikeStatus`)" +
                " VALUES ('" + b.getType() + "', " + this.locationID + ", '"+ b.getStatus() +"')";
        Query query = new Query(q);

        if(query.insertQuery()) {
            //update the bikes list with all the relevant information
            forceGetBikes();
        } else {
            throw new InsertFailedException("Insert Failed");
        }
    }
}
