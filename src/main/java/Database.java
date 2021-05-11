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

    private List<Projects> projects;
    private List<Users> users;

    public Database() {
    }
    
    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public List<Projects> getProjects() {
        return projects;
    }

    public void setProjects(List<Projects> input) {
        this.projects = input;
    }

}
