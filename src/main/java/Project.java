/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tanweilok
 */
public class Project implements Serializable {

    private int id;
    private String name;
    private ArrayList<Issue> issues;

    public Project(int id, String name, List<Issue> issues) {
        this.id = id;
        this.name = name;
        this.issues = (ArrayList<Issue>) issues;
    }

    //Issueboard
    public String issueBoard() {
        String res = "";

        return res;
    }

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

    public ArrayList<Issue> getIssues() {
        return issues;
    }

    public void setIssues(ArrayList<Issue> issues) {
        this.issues = issues;
    }
}
