package bugs;/*
 * To change this license header, choose License Headers in bugs.Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 *
 * @author tanweilok
 */

public class Comment implements Serializable {
    private int comment_id;
    private String text;
    private ArrayList<React> react;
    private Timestamp timestamp;
    private String user;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Comment(int comment_id, String text, String user) {
        this.comment_id = comment_id;
        this.text = text;
        this.user = user;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.timestamp = timestamp;
        react = new ArrayList<>();
    }

    public Comment(int comment_id, String text, Timestamp timestamp, String user) {
        this.comment_id = comment_id;
        this.text = text;
        this.react = new ArrayList<>();
        this.timestamp = timestamp;
        this.user = user;
    }

    public Comment(int comment_id, String text, ArrayList<React> react, Timestamp timestamp, String user) {
        this.comment_id = comment_id;
        this.text = text;
        this.react = react;
        this.timestamp = timestamp;
        this.user = user;
    }

    @Override
    public String toString() {
        String res = "";
        res += String.format("%s %s \t\t\t %s %s\n", "Created On:", sdf.format(timestamp), "By:", user);
        res += String.format("%s\n", text);
        for(int i=0; i<react.size(); i++)
            res += String.format("$$ %s\n", react.get(i).toString());// this line no output
        res += "$$\n";
        return res;
    }

    public void addReact(String reaction) {
        int ind = getIndex(reaction);
        if(ind==-1) {
            react.add(new React(reaction, 1));
        }else {
            react.get(ind).inc();
        }
        sorting();
        return;
    }

    public void delReact(String reaction) {
        int ind = getIndex(reaction);
        if(ind==-1) {
            //impossible
        }else {
            react.get(ind).dec();
            if(react.get(ind).getCount()==0)react.remove(ind);
        }
        sorting();
        return;
    }

    public void sorting() {
        //most react emoji will display first
        Collections.sort(react);
    }

    public int getIndex(String reaction) {
        if(reaction==null)return -1;
        for(int i=0; i<react.size(); i++) {
            String curr = react.get(i).getReaction();
            if(curr.equals(reaction))return i;
        }
        //new reaction
        return -1;
    }

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

    public ArrayList<React> getReact() {
        return react;
    }

    public void setReact(ArrayList<React> react) {
        this.react = react;
    }
}
