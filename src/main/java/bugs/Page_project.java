package bugs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static bugs.Page_issue.*;
import static bugs.Page_main.*;
import static bugs.util.*;

public class Page_project {
    public static void proj_page() {
        String c=null;
        do {
            c = ask(project.getIssues().size(), "Enter selected issue ID to check issue\nor 's' to search\nor 'c' to create issue\nor '0' to go back: ", project.issueBoard(), false);
            if(c.equals("s")) {
                //search issue
                if(project.getIssues().isEmpty()) {
                    System.out.println("There is no issue found in this project.\nCreate one before searching.");
                    prompt_any();
                    continue;
                }
                System.out.print("Search Text: ");
                String x = new Scanner(System.in).nextLine();
                ArrayList<Integer>indices = search(x);
                System.out.println(project.issueBoardSearch(indices));
                System.out.print("Enter selected issue ID to check issue\nor '0' to go back: ");
                int choice = choice0(indices.size(), false, true);
                if(choice==0) {
                    continue;
                }else {
                    setIssueid(indices.get(choice-1));
                    issue = (Issue) project.getIssues().get(issueid);
                    Page_issue.issue_page();
                }
            }else if(c.equals("c")) {
                //create issue
                createIssue();
            }else{
                //select id
                if(project.getIssues().isEmpty()) {
                    System.out.println("There is no issue found in this project.\nCreate one before selecting.");
                    prompt_any();
                    continue;
                }
                int choice = Integer.parseInt(c);
                if(choice==0)return;
                setIssueid(choice-1);
                issue = (Issue) project.getIssues().get(issueid);
                Page_issue.issue_page();
            }
        }while(true);
    }

    public static void createProject() {
        //only admins can do
        Scanner in = new Scanner(System.in);
        System.out.println("~~~ Create New Project ~~~");
        System.out.print("Name: ");
        String name = in.nextLine();
        Timestamp currentTimestamp = new Timestamp(new Date().getTime());
        Project current = new Project(projectenum++, name, currentTimestamp);
        addProject(current);
        return;
    }

    public static void addProject(Project tmp) {
        projects.add(tmp);

        len_id=Math.max(len_id, Integer.toString(projects.size()).length());
        len_name=Math.max(len_name, tmp.getName().length());
        len_issues=Math.max(len_issues, Integer.toString(tmp.getIssues().size()).length());
    }

    //search function
    public static ArrayList search(String word) {
        ArrayList<Integer>indices = new ArrayList<>();
        ArrayList<Issue>issues = project.getIssues();
        for(int i=0; i<issues.size(); i++) {
            Issue tmp = issues.get(i);
            if(match(word, tmp.getTitle())) {
                indices.add((Integer) i);
                continue;
            }

            if(match(word, tmp.getStatus())) {
                indices.add((Integer) i);
                continue;
            }

            if(match(word, tmp.getCreatedBy())) {
                indices.add((Integer) i);
                continue;
            }

            if(match(word, tmp.getAssignee())) {
                indices.add((Integer) i);
                continue;
            }

            if(match(word, tmp.getTags())) {
                indices.add((Integer) i);
                continue;
            }

            if(match(word, tmp.getDescriptionText())) {
                indices.add((Integer) i);
                continue;
            }
        }
        return indices;
    }

    private static int len_id = 2;
    private static int len_name = 12;
    private static int len_issues = 6;
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
            tmp += right(len_id, cur.getProject_id());
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
}
