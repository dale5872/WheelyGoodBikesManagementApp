//TODO: Documentation
package App.Classes;

public class Equipment {

    private int id;
    private int typeID;
    private String typeName;
    private String status;
    private float price;
    private String image;
    private Location location;

    public Equipment(int id, int typeID, String typeName, String status, float price, String image, Location location) {
        this.id = id;
        this.typeID = typeID;
        this.typeName = typeName;
        this.status = status;
        this.price = price;
        this.image = image;
        this.location = location;
    }

    /**
     * TODO get equipment location
     * @param row
     */
    public Equipment(Object[] row) {
        this.id = (int)row[0];
        this.typeID = (int)row[1];
        this.typeName = (String)row[2];
        this.status = (String)row[3];
        this.price = (float)row[4];
        this.image = (String)row[5];
    }

    public Equipment() {}

    public int getID() {
        return this.id;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public int getTypeID() { return this.typeID; }

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

    public void setTypeName(String type) {
        this.typeName = type;
    }

    public void setTypeID(int id) { this.typeID = id; }

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

