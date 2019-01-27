//TODO: Documentation
package App.Classes;

public class Equipment {

    private int id;
    private String type;
    private String status;
    private float price;
    private String image;

    public Equipment(int id, String type, String status, float price, String image) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.price = price;
        this.image = image;
    }

    public Equipment(Object[] row) {
        this.id = (int)row[0];
        this.type = (String)row[1];
        this.status = (String)row[2];
        this.price = (float)row[3];
        this.image = (String)row[4];
    }

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
}

