package App.Classes;

import java.text.NumberFormat;

public class Type{
    private String id;
    private String name;
    private float price;

    public Type(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPrice(float price){
        this.price = price;
    }

    public String getID(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public float getPrice(){
        return this.price;
    }

    public String getFormattedPrice(){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(this.price);
    }
}
