package bugs;/*
 * To change this license header, choose License Headers in bugs.Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

/**
 *
 * @author tanweilok
 */
public class React implements Serializable, Comparable<React> {
    private String reaction;
    private int count;

    public React(String reaction, int count) {
        this.reaction = reaction;
        this.count = count;
    }

    public void inc() {
        count++;
    }

    public void dec() {
        count--;
    }

    @Override
    public int compareTo(React o) {
        if(o.getCount()>count)return -1;
        if(o.getCount()==count)return 0;
        return 1;
    }

    @Override
    public String toString() {
        return String.format("%d %s %s", count, "people react with",  reaction);
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    
}
