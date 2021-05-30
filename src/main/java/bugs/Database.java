package bugs;/*
 * To change this license header, choose License Headers in bugs.Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.List;

/**
 *
 * @author tanweilok
 */
public class Database implements Serializable{

    private List<Project> projects;
    private List<User> users;
    private History histories;

    public Database(List<Project> projects, List<User> users, History histories) {
        this.projects = projects;
        this.users = users;
        this.histories = histories;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUser(List<User> user) {
        this.users = user;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> input) {
        this.projects = input;
    }

    public History getHistories() {
        return histories;
    }

    public void setHistories(History histories) {
        this.histories = histories;
    }
}
