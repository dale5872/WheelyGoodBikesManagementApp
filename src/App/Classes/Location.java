package App.Classes;
import DatabaseConnector.Query;
import DatabaseConnector.Results;


public class Location {
    private int locationID;
    private String name;


    public Location(int locationID, String name) {
        this.locationID = locationID;
        this.name = name;
    }

    public Location(String name) {
        this.name = name;
        /**
         * TODO: Replace with PHP equivalent
         */
        Query q = new Query("SELECT location.locationID FROM location WHERE location.name = '" + this.name + "';");
        Results res = q.executeQuery();
        this.locationID = (int)res.getElement(0,0);
        /** TODO: null result checking */
    }

    public Location () {}

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

}
