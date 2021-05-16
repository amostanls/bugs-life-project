package bugs;

import bugs.Comment;

import java.sql.Timestamp;
import java.util.ArrayList;
import static bugs.util.*;

public class main_class {
    private static int userenum = -1; // this one use for database, inc one when there's registration

    private static ArrayList<User>users = new ArrayList<>();
    private static ArrayList<Project>projects = new ArrayList<>();
    private static int len_id = 2;
    private static int len_name = 12;
    private static int len_issues = 6;

    private static int userid = -1;  //current user id
    private static int projid = -1;  //current project to view
    private static int issueid = -1; //current issue to view

    public static void main(String[] args) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Comment comment = new Comment(2, "nice one, it's helpful", new Timestamp(timestamp.getTime()), "amos");
        comment.addReact("happy");
        //System.out.println(comment.toString());
        Issue issue = new Issue(2, "cannot launch app", 2, "In Progress", new String[]{(new String("Frontend"))}, "tak boleh open", "ben", "josh", new Timestamp(timestamp.getTime()));
        issue.addComment(comment);
        //System.out.println(issue.toString());
        //login loginPage = new login();
        //System.out.println("\n");
        Project a = new Project(1,"TableView Widget");
        a.addIssue(issue);
        //System.out.println(a.issueBoard());
        addProject(a);
        //System.out.println(projects.size());
        main_page();
    }

    public static void addProject(Project tmp) {
        projects.add(tmp);

        len_id=Math.max(len_id, Integer.toString(projects.size()).length());
        len_name=Math.max(len_name, tmp.getName().length());
        len_issues=Math.max(len_issues, Integer.toString(tmp.getIssues().size()).length());
    }

    public static void main_page() {
        int c = 0;
        do {
            System.out.println(projectBoard());
            c = ask0(projects.size(),"Enter 0 to quit. \nEnter selected project ID to check project: ");
            setProjid(c-1);
            proj_page();
        }while(c!=0);
    }

    public static void proj_page() {
        Project project = (Project) projects.get(projid);
        String c=null;
        do {
            System.out.println(project.issueBoard());
            c = ask(project.getIssues().size(), "Enter 0 to quit. \nEnter selected issue ID to check issue\nor 's' to search\nor 'c' to create issue: ");
            if(c.equals("s")) {
                //search
                System.out.println("search");
            }else if(c.equals("c")) {
                //create
                System.out.println("create");
            }else {
                int choice = Integer.parseInt(c);
                setIssueid(choice-1);
                issue_page();
            }
        }while(c!="0");
    }

    public static void issue_page() {
        Issue issue = projects.get(projid).getIssues().get(issueid);
        System.out.println(issue.toString());
    }

    public static String projectBoard() {
        String res = "+";
        res += minus_plus(len_id+2);
        res += minus_plus(len_name+2);
        res += minus_plus(len_issues+2);
        res += "\n";

        res += "|";
        res += centre(len_id,"ID");
        res += centre(len_name, "Project Name");
        res += centre(len_issues, "Issues");
        res += "\n";

        res += "+";
        res += minus_plus(len_id+2);
        res += minus_plus(len_name+2);
        res += minus_plus(len_issues+2);
        res += "\n";

        for(int i=0; i<projects.size(); i++) {
            String tmp = "|";
            Project cur = projects.get(i);
            tmp += right(len_id, cur.getId());
            tmp += left(len_name, cur.getName());
            tmp += right(len_issues, cur.getIssues().size());
            tmp += "\n";
            res += tmp;
        }

        res += "+";
        res += minus_plus(len_id+2);
        res += minus_plus(len_name+2);
        res += minus_plus(len_issues+2);
        res += "\n";

        return  res;
    }

    public static int getUserid() {
        return userid;
    }

    public static void setUserid(int userid) {
        main_class.userid = userid;
    }

    public static int getProjid() {
        return projid;
    }

    public static void setProjid(int projid) {
        main_class.projid = projid;
    }

    public static int getIssueid() {
        return issueid;
    }

    public static void setIssueid(int issueid) {
        main_class.issueid = issueid;
    }
}
