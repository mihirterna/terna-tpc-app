package org.terna.tpc;

public class UserInfo {
    private String name,email,id,dob,station,choice;

    public UserInfo(){}

    public UserInfo(String name, String email, String id, String dob, String station, String choice) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.dob = dob;
        this.station = station;
        this.choice = choice;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getDob() {
        return dob;
    }

    public String getStation() {
        return station;
    }

    public String getChoice() {
        return choice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
