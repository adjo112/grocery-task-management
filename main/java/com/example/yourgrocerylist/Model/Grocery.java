package com.example.yourgrocerylist.Model;

public class Grocery {
    private String name;
    private String quantity;
    private String dateitemadded;
    private int id;

    public Grocery() {

    }

    public Grocery(String name, String quantity, String dateitemadded, int id) {
        this.name = name;
        this.quantity = quantity;
        this.dateitemadded = dateitemadded;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDateitemadded() {
        return dateitemadded;
    }

    public void setDateitemadded(String dateitemadded) {
        this.dateitemadded = dateitemadded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
