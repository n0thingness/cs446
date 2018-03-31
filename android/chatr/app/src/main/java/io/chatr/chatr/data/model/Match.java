package io.chatr.chatr.data.model;

/**
 * Created by Daniel on 2018-03-31.
 */

public class Match {

    private int id;

    private String name;
    private String surname;

    private String interests;

    private String selfMessage;
    private String otherMessage;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getSelfMessage() {
        return selfMessage;
    }

    public void setSelfMessage(String selfMessage) {
        this.selfMessage = selfMessage;
    }

    public String getOtherMessage() {
        return otherMessage;
    }

    public void setOtherMessage(String otherMessage) {
        this.otherMessage = otherMessage;
    }
}
