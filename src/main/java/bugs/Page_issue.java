package bugs;

import java.sql.Timestamp;
import java.util.*;
import java.io.*;
import static bugs.util.*;
import static bugs.Page_comment.*;
import static bugs.Page_main.*;
import static bugs.Page_react.*;

public class Page_issue {

    public static void issue_page() {
        String c=null;
        do {
            c = ask(0, "Enter 'c' to comment\nor 'r' to react\nor 's' to search\nor '0' to go back.\nor 'help' for more commands: ", issue.toString(), true);
            if(c.equals("r")) {
                //react, select which comment to react first
                if(issue.getComments().isEmpty()) {
                    System.out.println("There is no comments found in this issue.\nCreate one before reacting.");
                    prompt_any();
                    continue;
                }
                System.out.println("Enter selected comment ID to react.\nor '0' to go back.");
                int choice = choice0(issue.getComments().size(), false, false);
                if(choice>0)Reacting(choice);
            }else if(c.equals("c")) {
                createComment();
            }else if(c.equals("s")){
                if(issue.getComments().isEmpty()) {
                    System.out.println("There is no comments found in this issue.\nCreate one before searching.");
                    prompt_any();
                    continue;
                }
                System.out.print("Search Text: ");
                String x = new Scanner(System.in).nextLine();
                ArrayList<Integer> indices = search(x);
                System.out.println(issue.commentstoString(indices));
                prompt_any();
            }else if(c.equals("help")) {

            }else {
                //confirm this is 0
                return;
            }
        }while(true);
    }

    public static ArrayList search(String word) {
        ArrayList<Integer>indices = new ArrayList<>();
        ArrayList<Comment>comments = issue.getComments();
        for(int i=0; i<comments.size(); i++) {
            Comment tmp = comments.get(i);
            if(match(word,tmp.getText())) {
                indices.add(i);
                continue;
            }
            if(match(word,tmp.getUser())) {
                indices.add(i);
                continue;
            }
        }
        return indices;
    }

    //priority from 1-9
    private static String status[]={"Resolved", "Closed", "Open", "In Progress"};

    public static void claimIssue() {
        //only admin can do? assignee part?
    }

    public static void createIssue() {
        Scanner in = new Scanner(System.in);
        System.out.print("Title: ");
        String title = in.nextLine();

        System.out.println("Status: ");
        addChoices(status);
        int status_id = choice1(status.length, true);

        System.out.println("Priority (1-9), The higher the value, the higher the priority of this issue is.");
        int priority = choice1(9, false);

        String tag = specialchoice(); //there's self-defined tag, which is special

        String name = user.getName();

//        System.out.println("(Enter \"*****\" at the last line of text to stop writing and leave a space if you want the line to be blank by putting \" \")");
        System.out.println("Description text: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String text="";
        String line = in.nextLine();
        //please handle blank spaces in GUI, I cannot find way to do it.
        text = line;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Issue issue = new Issue(1,issueenum++, title, priority, status[status_id], new String[]{tag},  text, name, "None", new Timestamp(timestamp.getTime()));
        addIssue(issue);
        return;
    }

    public static void addIssue(Issue tmp) {
        project.addIssue(tmp);
        project.sortIssue();
    }
}
