/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author tanweilok
 */
public class Comment implements Serializable {
    private int comment_id;
    private String text;
    private List<React> react;
    private Timestamp timestamp;
    private String user;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<React> getReact() {
        return react;
    }

    public void setReact(List<React> react) {
        this.react = react;
    }
    
    
}
