package App.Classes;

public class Equipment {
    private int id;
    private String status;
    private Location location;
    private String category; //Bike or Equipment
    private Type type;

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

    public void setID(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    /**
     * DO NOT DELETE
     * Legacy accessor from before Location list was fully implemented.
     * This accessor is still used to display the location name in tables.
     * @return
     */
    @Deprecated
    public String getLocationName() {
        return this.location.getName();
    }

    /**
     * DO NOT DELETE
     * This accessor is still used to display the formatted price in tables.
     * @return
     */
    @Deprecated
    public String getFormattedPrice(){
        return this.type.getFormattedPrice();
    }

    public void setType(Type type){
        this.type = type;
    }

    public Type getType(){
        return this.type;
    }
}

