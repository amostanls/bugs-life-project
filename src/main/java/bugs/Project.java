package bugs;/*
 * To change this license header, choose License Headers in bugs.Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static bugs.util.*;

/**
 *
 * @author tanweilok
 */
public class Project implements Serializable {

    private int id;
    private String name;
    private ArrayList<Issue> issues;
    private int len_id = 2;
    private int len_title = 5;
    private int len_status = 6;
    private int len_tag = 3;
    private int len_priority = 8;
    private int len_time = 4;
    private int len_assignee = 8;
    private int len_createdby = 9;

    public Project(int id, String name) {
        this.id = id;
        this.name = name;
        this.issues = new ArrayList<>();
    }

    public Project(int id, String name, List<Issue> issues) {
        this.id = id;
        this.name = name;
        this.issues = (ArrayList<Issue>) issues;
    }

    public void addIssue(Issue o) {
        System.out.println("len id: "+len_id);
        System.out.println("len title: "+len_title);
        System.out.println("len status: "+len_status);
        System.out.println("len tag: "+len_tag);
        issues.add(o);
        len_id = Math.max(len_id,(int)Integer.toString(issues.size()).length());
        len_title = Math.max(len_title,(int)o.getTitle().length());
        len_status = Math.max(len_status,(int)o.getStatus().length());
        len_tag = Math.max(len_tag,(int)o.getTags().length());
        len_priority = Math.max(len_priority,(int)Integer.toString(o.getPriority()).length());
        len_time = Math.max(len_time,(int)o.getTime().length());
        len_assignee = Math.max(len_assignee,(int)o.getAssignee().length());
        len_createdby = Math.max(len_createdby,(int)o.getCreatedBy().length());
        System.out.println("len id: "+len_id);
        System.out.println("len title: "+len_title);
        System.out.println("len status: "+len_status);
        System.out.println("len tag: "+len_tag);
    }

    //Issueboard
    public String issueBoard() {

        String res = "bugs.Issue board\n";
        res += "+";
        for(int i=0; i<len_id+2; i++)res+="-"; res+="+"; //id
        for(int i=0; i<len_title+2; i++)res+="-"; res+="+"; //title
        for(int i=0; i<len_status+2; i++)res+="-"; res+="+"; //status
        for(int i=0; i<len_tag+2; i++)res+="-"; res+="+"; //tag
        for(int i=0; i<len_priority+2; i++)res+="-"; res+="+"; //priority
        for(int i=0; i<len_time+2; i++)res+="-"; res+="+"; //time
        for(int i=0; i<len_assignee+2; i++)res+="-"; res+="+"; //assignee
        for(int i=0; i<len_createdby+2; i++)res+="-"; res+="+\n"; //createdby

        res += "|";
        res += centre(len_id,"ID");
        res += centre(len_title,"Title");
        res += centre(len_status,"Status");
        res += centre(len_tag, "Tag");
        res += centre(len_priority, "Priority");
        res += centre(len_time, "Time");
        res += centre(len_assignee, "Assignee");
        res += centre(len_createdby, "CreatedBy");
        res += "\n";

        res += "+";
        for(int i=0; i<len_id+2; i++)res+="-"; res+="+"; //id
        for(int i=0; i<len_title+2; i++)res+="-"; res+="+"; //title
        for(int i=0; i<len_status+2; i++)res+="-"; res+="+"; //status
        for(int i=0; i<len_tag+2; i++)res+="-"; res+="+"; //tag
        for(int i=0; i<len_priority+2; i++)res+="-"; res+="+"; //priority
        for(int i=0; i<len_time+2; i++)res+="-"; res+="+"; //time
        for(int i=0; i<len_assignee+2; i++)res+="-"; res+="+"; //assignee
        for(int i=0; i<len_createdby+2; i++)res+="-"; res+="+\n"; //createdby

        for(int i=0; i<issues.size(); i++) {
            Issue cur = issues.get(i);
            String tmp = "|";
            tmp += right(len_id,(i+1));
            tmp += left(len_title,cur.getTitle());
            tmp += left(len_status,cur.getStatus());
            tmp += left(len_tag, cur.getTags());
            tmp += right(len_priority, cur.getPriority());
            tmp += centre(len_time, cur.getTime());
            tmp += left(len_assignee, cur.getAssignee());
            tmp += left(len_createdby,cur.getCreatedBy());
            tmp += "\n";
            res += tmp;
        }

        res += "+";
        for(int i=0; i<len_id+2; i++)res+="-"; res+="+"; //id
        for(int i=0; i<len_title+2; i++)res+="-"; res+="+"; //title
        for(int i=0; i<len_status+2; i++)res+="-"; res+="+"; //status
        for(int i=0; i<len_tag+2; i++)res+="-"; res+="+"; //tag
        for(int i=0; i<len_priority+2; i++)res+="-"; res+="+"; //priority
        for(int i=0; i<len_time+2; i++)res+="-"; res+="+"; //time
        for(int i=0; i<len_assignee+2; i++)res+="-"; res+="+"; //assignee
        for(int i=0; i<len_createdby+2; i++)res+="-"; res+="+\n"; //createdby

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

    public int getLen_id() {
        return len_id;
    }

    public void setLen_id(int len_id) {
        this.len_id = len_id;
    }

    public int getLen_title() {
        return len_title;
    }

    public void setLen_title(int len_title) {
        this.len_title = len_title;
    }

    public int getLen_status() {
        return len_status;
    }

    public void setLen_status(int len_status) {
        this.len_status = len_status;
    }

    public int getLen_tag() {
        return len_tag;
    }

    public void setLen_tag(int len_tag) {
        this.len_tag = len_tag;
    }

    public int getLen_priority() {
        return len_priority;
    }

    public void setLen_priority(int len_priority) {
        this.len_priority = len_priority;
    }

    public int getLen_time() {
        return len_time;
    }

    public void setLen_time(int len_time) {
        this.len_time = len_time;
    }

    public int getLen_assignee() {
        return len_assignee;
    }

    public void setLen_assignee(int len_assignee) {
        this.len_assignee = len_assignee;
    }

    public int getLen_createdby() {
        return len_createdby;
    }

    public void setLen_createdby(int len_createdby) {
        this.len_createdby = len_createdby;
    }
}
