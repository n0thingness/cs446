package io.chatr.chatr.data.model;

import java.util.List;

/**
 * Created by lohit.talasila on 2018-03-25.
 */

public class Location {
    private String id;
    private String location_name;
    private String location_phone_no;
    private List<Integer> location_types;
    private int location_price_level;
    private float location_rating;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getLocationName(){
        return location_name;
    }
    public void setLocationName(String location_name){
        this.location_name = location_name;
    }

    public String getLocationPhoneNo(){
        return location_phone_no;
    }
    public void setLocationPhoneNo(String location_phone_no){
        this.location_phone_no = location_phone_no;
    }

    public List<Integer> getLocationTypes(){
        return location_types;
    }
    public void setLocationTypes(List<Integer>location_types){
        this.location_types = location_types;
    }

    public int getLocationPriceLevel(){
        return location_price_level;
    }
    public void setLocationPriceLevel(int location_price_level){
        this.location_price_level = location_price_level;
    }

    public float getLocationRating(){
        return location_rating;
    }
    public void setLocationRating(float location_rating){
        this.location_rating = location_rating;
    }

}
