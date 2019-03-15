package App.Classes;

import java.text.NumberFormat;

public class Type{
    private int id;
    private String name;
    private double price;
    private String image;

    public Type(){}

    public Type(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setImage(String url) { this.image = url; }

    public int getID(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public double getPrice(){
        return this.price;
    }

    public String getFormattedPrice(){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(this.price);
    }

    public String getImage() { return this.image; }
}
