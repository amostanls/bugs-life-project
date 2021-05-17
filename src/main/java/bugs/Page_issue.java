package bugs;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Scanner;
import java.io.*;
import static bugs.util.*;
import static bugs.Page_main.*;

public class Page_issue {

    public static void issue_page() {
        String c=null;
        do {
            c = ask(0, "Enter 0 to go back. \nEnter 'r' to react\nor 'c' to comment\nor 'help' for more commands: ", issue.toString());
            if(c.equals("r")) {
                //react
                System.out.println("react");
            }else if(c.equals("c")) {
                //comment
                System.out.println("comment");
            }else if(c.equals("help")){

            }else {
                //confirm this is 0
                return;
            }
        }while(true);
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

        System.out.println("(Enter \"*****\" at the last line of text to stop writing)");
        System.out.println("Description text: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String text="";
        String line;
        try {
            while ((line = br.readLine()) != "*****\n")
            {
                String[] tokens = line.split("\\s");
                text+=(Arrays.toString(tokens));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Issue issue = new Issue(issueenum++, title, priority, status[status_id], new String[]{tag},  text, name, "None", new Timestamp(timestamp.getTime()));
        addIssue(issue);
        return;
    }

    public static void addIssue(Issue tmp) {
        project.addIssue(tmp);
    }

    private static String[] reactions = {"happy", "sad", "angry"};

    //choose comment to react
    //if no comment
    //there is comment, check reacted with respectively comment id
    public static void react_page() {
        int c = -1;
        int size = issue.getComments().size();
        if(size==0) {
            System.out.println("There is no comment to react.");
            return;
        }
        do {
            c = ask0(size,"Enter 0 to not react. \nEnter selected comment ID to react", issue.toString());
            if(c>0) {
                //check if user has reacted this comment

            }
        }while(c!=0);
    }

}
