package org.terna.tpc;

public class UserInfo {
    public String name,email,id,dob,gender,yr,br;

    public UserInfo () {
    }


    public UserInfo(String name, String email, String id, String dob, String gender, String yr, String br) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.dob = dob;
        this.gender = gender;
        this.yr = yr;
        this.br = br;
    }

    public String getName() {
        return name;
    }

    public String getEmail() { return email;
    }

    public String getId() { return id; }

    public String getDob() {
        return dob;
    }

    public String getGender() { return gender; }

    public String getYr() {
        return yr;
    }

    public String getBr() {
        return br;
    }
}

