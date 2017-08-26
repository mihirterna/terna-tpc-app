package org.terna.tpc;

public class UserInfo {

    public String name,email,id,pswd,dob,station,choice;

    public UserInfo(String name, String email, String id, String pswd, String dob, String station, String choice) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.pswd=pswd;
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

    public String getPswd() {
        return pswd;
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
