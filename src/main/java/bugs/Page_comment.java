package bugs;

import java.util.Scanner;

import static bugs.Page_issue.*;
import static bugs.Page_main.*;
import static bugs.Page_project.*;

public class Page_comment {
    public static void createComment() {
        int id = commentenum++;
//        System.out.println("(Enter \"*****\" at the last line of text to stop writing and leave a space if you want the line to be blank by putting \" \")");
        System.out.print("Comment: ");
        Scanner in = new Scanner(System.in);
        String text = in.nextLine();
        //please handle blank spaces in GUI, I cannot find way to do it.
        Comment tmp = new Comment(id, text, user.getName());
        issue.addComment(tmp);
    }
}
