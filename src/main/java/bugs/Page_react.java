package bugs;

import java.util.ArrayList;
import java.util.Scanner;

import static bugs.Page_main.*;
import static bugs.util.*;

public class Page_react {

    private static String[] reactions = {"thumbsUp", "thumbsDown", "happy", "sad", "angry"};

    public static void Reacting(int comment_ind) {
        comment_ind--;
        Comment comment = (Comment) issue.getComments().get(comment_ind);
        int commentid = comment.getComment_id();
        //check what user has reacted
        while(true) {
            String reacted = user.getReaction(commentid);
            System.out.println("~~~ Reacting ~~~");
            System.out.println(comment.getText());
            String[] toreact = reactions;
            if(reacted!=null)System.out.println("You have reacted " + reacted);
            for (int i = 0; i < reactions.length; i++)
                if (reactions[i].equals(reacted)) toreact[i] = "Remove " + toreact[i];
            addChoices(toreact);
            System.out.println("0. Go Back");
            int choice = choice0(toreact.length, true);
            if(choice==0)break;
            else {
                choice--;
                //remove old
                user.delReaction(commentid);
                comment.delReact(reacted);
                //add new
                if(reacted!=null&&!reacted.equals(reactions[choice])){
                    user.addReaction(commentid, reactions[choice]);
                    comment.addReact(reacted);
                }
            }
        }
    }
}
