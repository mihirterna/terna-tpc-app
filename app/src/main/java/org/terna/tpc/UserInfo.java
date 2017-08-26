package org.terna.tpc;

public class UserInfo {

    public String name,email,id,dob,year,branch,gen;

    public UserInfo(String name, String email, String id, String dob,String gen,String year,String branch) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.dob = dob;
        this.gen=gen;
        this.year = year;
        this.branch=branch;
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

    public String getGen() { return gen;}

    public String getYear(){ return year;}

    public String getBranch() { return branch;}

}
