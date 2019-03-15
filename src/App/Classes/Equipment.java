package App.Classes;

import java.text.NumberFormat;

public class Equipment {

    private int id;
    private String status;
    private float price;
    private String image;
    private Location location;
    private String category; //Bike or Equipment
    private Type type;

    public Equipment(int id, String status, float price, String image, Location location) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.image = image;
        this.location = location;
    }

    public Equipment() {}

    public int getID() {
        return this.id;
    }

    /**
     * DO NOT DELETE
     * Legacy accessor from before Type object was used.
     * This accessor is still used to display the type name in tables.
     * @return
     */
    @Deprecated
    public String getTypeName() {
       // return this.typeName;
        return this.type.getName();
    }
    /**
     * DO NOT DELETE
     * Legacy accessor from before Type object was used.
     * This accessor is still used to display the type ID in tables.
     * @return
     */
    @Deprecated
    public int getTypeID() { return this.type.getID(); }

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

    public String getCategory() { return this.category; }

    public void setCategory(String category) { this.category = category; }

    public String getFormattedPrice(){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(this.price);
    }

    public void setType(Type type){
        this.type = type;
    }

    public Type getType(){
        return this.type;
    }
}

