package com.example.searchify;

public class UserObj {
    private String name, user_name;

    public UserObj() {}

    public UserObj(String name, String user_name) {
        this.name = name;
        this.user_name = user_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "UserObj{" +
                "name='" + name + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
