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
public class Database implements Serializable{

    private List<Project> projects;
    private List<User> users;
    
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

}
