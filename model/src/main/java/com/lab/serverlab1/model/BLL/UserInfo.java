package com.lab.serverlab1.model.BLL;

public class UserInfo {
    private String username;
    private String password;
    private String name;
    private int age;
    public UserInfo(){
        username = "";
        password = "";
        name = "";
        age = 0;
    }
    public UserInfo(String username, String password, String name, int age){
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
