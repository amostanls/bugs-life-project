package bugs;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import static bugs.Page_project.*;
import static bugs.util.*;
import static bugs.authentication.*;

public class Page_main {
    // this one use for database, inc one when there's new registration / project / issue / comment
    public static int userenum = 0;
    public static int projectenum = 1;
    public static int issueenum = 0;
    public static int commentenum = 0;

    public static ArrayList<User>users = new ArrayList<>();
    public static ArrayList<Project>projects = new ArrayList<>();

    public static int userid = -1;  //current user id
    public static int projid = -1;  //current project to view
    public static int issueid = -1; //current issue to view
    public static User user;
    public static Project project;
    public static Issue issue;
    public static HashMap<String, Integer>username_id = new HashMap<>();

    private static String[] start_page={"Terminate the program", "Log In", "Registration", "Registration (Admin Only)"};

    public static void main(String[] args) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        Comment comment1 = new Comment(2, "nice one, it's helpful", new Timestamp(timestamp.getTime()), "amos");
//        comment1.addReact("happy");
//        Issue issue = new Issue(2, "cannot launch app", 2, "In Progress", new String[]{(new String("Frontend"))}, "tak boleh open", "ben", "josh", new Timestamp(timestamp.getTime()));
//        issue.addComment(comment1);
//        Project a = new Project(1,"TableView Widget", timestamp);
//        a.addIssue(issue);
//        addProject(a);
//        System.out.println(comment.toString());
//        System.out.println(issue.toString());
//        System.out.println("\n");
//        System.out.println(a.issueBoard());
//        System.out.println(projects.size());
//        login loginPage = new login();
        //need to load all things from database
        //all enum as well
        int id = -1;
        while(true) {
            addChoices(start_page);
            int c = choice0(3, true, true);
            if (c == 0) break;
            else if (c == 1) id = login();
            else if(c==2) id = register();
            else id = registerAdmin();
            if(id!=-1) {
                user = (User) users.get(id);
                main_page();
            }
        }
    }

    public static void main_page(){
        String c;
        do {
            c = ask(projects.size(),"Enter selected project ID to check project \nor '0' to log out\nor 'c' to create project: ", projectBoard(), true);
            if(isInteger(c)) {
                int choice = Integer.parseInt(c);
                if(choice==0)return;
                else {
                    setProjid(choice - 1);
                    project = (Project) projects.get(choice - 1);
                    proj_page();
                }
            }else {
                //create Project
                createProject();
            }
        }while(true);
    }

    public static int getUserid() {
        return userid;
    }

    public static void setUserid(int userid) {
        Page_main.userid = userid;
    }

    public static int getProjid() {
        return projid;
    }

    public static void setProjid(int projid) {
        Page_main.projid = projid;
    }

    public static int getIssueid() {
        return issueid;
    }

    public static void setIssueid(int issueid) {
        Page_main.issueid = issueid;
    }
}
