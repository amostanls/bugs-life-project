/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

/**
 *
 * @author tanweilok
 */

public class User implements Serializable {
    private int userid;
    private String username;
    private String password;
    private String name;
    private boolean admin;
    
    public User(int userid, String username, String password, String name, boolean admin) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.name = name;
        this.admin = admin;
    }

    public User(int userid, String username, String password, boolean admin) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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
    
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public boolean isAdmin() { return admin; }

    public void setAdmin(boolean admin) { this.admin = admin; }
}
