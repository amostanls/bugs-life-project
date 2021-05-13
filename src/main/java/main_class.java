import me.xdrop.fuzzywuzzy.FuzzySearch;

import javax.swing.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class main_class {
    public static void main(String[] args) {
        ArrayList<User>users;
        ArrayList<Project>projects;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Comment comment = new Comment(2, "nice one, it's helpful", new Timestamp(timestamp.getTime()), "amos");
        comment.addReact("happy");
        //System.out.println(comment.toString());
        Issue issue = new Issue(2, "cannot launch app", 2, "In Progress", new String[]{(new String("Frontend"))}, "tak boleh open", "ben", "josh", new Timestamp(timestamp.getTime()));
        issue.addComment(comment);
        System.out.println(issue.toString());
        //login loginPage = new login();
    }
}
