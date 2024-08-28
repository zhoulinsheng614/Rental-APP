package com.example.forum;

import java.io.Serializable;

/**
 * This class defines what properties should be included in each house
 *
 * @author All Members
 */
public class House implements Serializable {
    private String id;//key(timestamp)
    private String city;//value[0](CBR)
    private String suburb;//value[1](Acton)
    private String street;//value[2](Daley Road)
    private String streetNumber;//value[3]
    private String unit;//Value[4](165)
    private int price;//value[5](Fortnight fees)
    private int xbxb;//value[6](Sizes)
    private String email;//value[7](Contactor E-mail)
    private int likes;//value[8](Likes received from users)

    public int height;//Helper to balance AVL tree
    public House left;// Left child

    public House right;// Right child

    public House(String id, String city, String suburb, String street, String streetNumber, String unit, int price, int xbxb, String email, int likes) {
        this.id = id;
        this.city = city;
        this.suburb = suburb;
        this.street = street;
        this.streetNumber = streetNumber;
        this.unit = unit;
        this.price = price;
        this.xbxb = xbxb;
        this.email = email;
        this.height = 1;
        this.left = null;
        this.right = null;
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getUnit() {
        return unit;
    }

    public int getPrice() {
        return price;
    }

    public int getXbxb() {
        return xbxb;
    }

    public String getEmail() {
        return email;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public House getLeft() {
        return left;
    }

    public void setLeft(House left) {
        this.left = left;
    }

    public House getRight() {
        return right;
    }

    public void setRight(House right) {
        this.right = right;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String toString() {
        return id + ";" + city + ";" + suburb + ";$" + price + ";B" + xbxb;
    }
}