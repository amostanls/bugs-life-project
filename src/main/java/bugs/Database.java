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
    private List<Comments_Reaction> comments_reactions;

    public Database(List<Project> projects, List<User> users, History histories, List<Comments_Reaction> comments_reactions) {
        this.projects = projects;
        this.users = users;
        this.histories = histories;
        this.comments_reactions = comments_reactions;
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

    public List<Comments_Reaction> getComments_reactions() {
        return comments_reactions;
    }

    public void setComments_reactions(List<Comments_Reaction> comments_reactions) {
        this.comments_reactions = comments_reactions;
    }
}
