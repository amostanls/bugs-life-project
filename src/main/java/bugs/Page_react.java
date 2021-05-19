package bugs;

import java.util.ArrayList;
import java.util.Scanner;

import static bugs.Page_main.*;
import static bugs.util.*;

public class Page_react {

    private static final String[] reactions = {"Go Back","thumbsUp", "thumbsDown", "happy", "sad", "angry"};

    public static void Reacting(int comment_ind) {
        comment_ind--;
        Comment comment = (Comment) issue.getComments().get(comment_ind);
        int commentid = comment.getComment_id();
        //check what user has reacted
        while(true) {
            String reacted = user.getReaction(commentid);
            System.out.println("~~~ Reacting ~~~");
            System.out.println(comment.getText());
            String[] toreact = new String[6];
            if(reacted!=null)System.out.println("You have reacted with " + reacted);
            for (int i = 0; i < reactions.length; i++) {
                if (reactions[i].equals(reacted)) toreact[i] = "Remove " + reactions[i];
                else toreact[i] = reactions[i];
            }
            addChoices(toreact);
            int choice = choice0(toreact.length, true, true);
            if(choice==0)break;
            else {
                //remove old
                if(reacted!=null) {
                    user.delReaction(commentid);
                    comment.delReact(reacted);
                }
                //add new
                if(reacted==null){
                    user.addReaction(commentid, reactions[choice]);
                    comment.addReact(reactions[choice]);
                }else if(!reacted.equals(reactions[choice])){
                    user.addReaction(commentid, reactions[choice]);
                    comment.addReact(reactions[choice]);
                }
            }
        }
    }
}
