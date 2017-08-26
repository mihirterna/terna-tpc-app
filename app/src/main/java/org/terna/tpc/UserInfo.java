package org.terna.tpc;

public class UserInfo {

    public String name,email,id,dob,station,choice;

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
}
