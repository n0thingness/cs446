package io.chatr.chatr.data.model;

import java.util.List;

public class Location {
    private int id;
    private String gid;
    private String name;
    private String address;
    private String phoneNumber;
    private List<Integer> locationTypes;
    private int priceLevel;
    private float rating;

    //(place_id_str, place_name, place_address, place_phone_no, place_types, place_price_level, place_rating)
    public Location(int id, String gid, String name, String address, String phoneNumber, List<Integer> locationTypes, int priceLevel, float rating) {
        this.id = id;
        this.gid = gid;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.locationTypes = locationTypes;
        this.priceLevel = priceLevel;
        this.rating = rating;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getGid() {
        return gid;
    }
    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public List<Integer> getLocationTypes(){
        return locationTypes;
    }
    public void setLocationTypes(List<Integer>locationTypes){
        this.locationTypes = locationTypes;
    }

    public int getPriceLevel(){
        return priceLevel;
    }
    public void setPriceLevel(int priceLevel){
        this.priceLevel = priceLevel;
    }

    public float getRating(){
        return rating;
    }
    public void setRating(float rating){
        this.rating = rating;
    }
}
