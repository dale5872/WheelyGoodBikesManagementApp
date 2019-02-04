//TODO: Documentation
package App.Classes;

public class Equipment {

    private int id;
    private String type;
    private String status;
    private float price;
    private String image;
    private Location location;

    public Equipment(int id, String type, String status, float price, String image, Location location) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.price = price;
        this.image = image;
        this.location = location;
    }

    public Equipment(Object[] row) {
        this.id = (int)row[0];
        this.type = (String)row[1];
        this.status = (String)row[2];
        this.price = (float)row[3];
        this.image = (String)row[4];
        //TODO: get location
    }

    public Equipment() {}

    public int getID() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getStatus() {
        return this.status;
    }

    public float getPrice() {
        return this.price;
    }

    public String getImage() {
        return this.image;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getLocationName() {
        return this.location.getName();
    }

    public int getLocationID() {
        return this.location.getLocationID();
    }

}

