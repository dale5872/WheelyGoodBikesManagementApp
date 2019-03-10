package App.Classes;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Rental {

    private int id;
    private float cost;
    private Date startTime;
    private Date returnTime;
    private String status;
    private Account user;
    private Equipment equipment;

    public void setID(int id) {
        this.id = id;
    }

    public void setCost(float cost) {
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

    public float getCost() {
        return this.cost;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public Date getReturnTime() {
        return this.returnTime;
    }

    public String getStatus() {
        return this.getStatus();
    }

    public Account getUser() {
        return this.user;
    }

    public Equipment getEquipment() {
        return this.equipment;
    }

}
