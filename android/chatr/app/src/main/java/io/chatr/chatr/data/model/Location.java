package io.chatr.chatr.data.model;

/**
 * Created by Daniel on 2018-03-25.
 */

public class Location {
    private int id;
    private String gid;
    private String name;
    private String address;
    private int priceLevel;
    private int rating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }


}
