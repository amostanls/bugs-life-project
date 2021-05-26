package bugs;/*
 * To change this license header, choose License Headers in bugs.Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.HashMap;

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
    private String url;
    private HashMap<Integer,String> comment_reacted = new HashMap<>();
    
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

    public void addReaction(int commentid, String reaction) {
        comment_reacted.put(commentid, reaction);
        return;
    }

    public void delReaction(int commentid) {
        comment_reacted.remove(commentid);
        return;
    }

    public void modify(int commentid, String reaction) {
        delReaction(commentid);
        addReaction(commentid, reaction);
        return;
    }

    public String getReaction(int commentid) {
        if(comment_reacted.containsKey(commentid))return comment_reacted.get(commentid);
        else return null;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
