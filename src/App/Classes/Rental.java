package App.Classes;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rental {

    private int id;
    private double cost;
    private Date startTime;
    private Date returnTime;
    private String status;
    private Account user;
    private Equipment equipment;

    public void setID(int id) {
        this.id = id;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void calculateCost() {
        long diff = returnTime.getTime() - startTime.getTime();
        int differentHours = (int) (diff / (60 * 60 * 1000));
        this.cost = differentHours * this.equipment.getPrice();
    }

    public void setStartTime(Date time) {
        this.startTime = time;
    }

    public void setReturnTime(Date time) {
        this.returnTime = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public int getID() {
        return this.id;
    }

    public double getCost() { return this.cost; }

    public String getFormattedCost() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(this.cost);
    }

    public String getStartTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(this.startTime);
    }

    public String getReturnTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(this.returnTime);
    }

    public String getStatus() {
        return this.status;
    }

    public Account getUser() {
        return this.user;
    }

    public Equipment getEquipment() {
        return this.equipment;
    }

    public int getEquipmentID() { return this.equipment.getID(); }

    public String getEquipmentName() { return this.equipment.getType().getName(); }

    public String getEquipmentPrice() { return this.equipment.getFormattedPrice(); }

    public String getLocationName() { return this.equipment.getLocation().getName(); }


}
