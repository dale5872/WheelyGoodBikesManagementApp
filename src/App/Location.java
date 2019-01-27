package App;

public class Location {
    private int locationID;
    private String name;

    public Location(int locationID, String name) {
        this.locationID = locationID;
        this.name = name;
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
}
