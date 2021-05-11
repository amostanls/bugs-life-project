/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.List;

/**
 *
 * @author tanweilok
 */
public class Projects implements Serializable {

    private int id;
    private String name;
    private List<Issues> issues;

    public int getId() {
        return id;
    }

    public void setId(int input) {
        this.id = input;
    }

    public String getName() {
        return name;
    }

    public void setName(String input) {
        this.name = input;
    }

    public List<Issues> getIssues() {
        return issues;
    }

    public void setIssues(List<Issues> input) {
        this.issues = input;
    }

}
