package bugs;

import static bugs.Page_main.*;
import static bugs.util.*;

import java.util.Scanner;

public class authentication {
    public static int login() {
        Scanner in = new Scanner(System.in);
        String username, password;
        System.out.println("~~~ Login ~~~");
        System.out.print("Username: ");
        username = in.nextLine();
        System.out.print("Password: ");
        password = in.nextLine();
        if(username_id.containsKey(username)) {
            int userid = (int) username_id.get(username);
            User user = (User) users.get(userid);
            if(password.equals(user.getPassword()))return userid;
            else {
                System.out.println("Incorrect password.");
                prompt_any();
                return -1;
            }
        }else {
            System.out.println("Username is not found.");
            prompt_any();
            return -1;
        }
    }

    public static int register() {
        Scanner in = new Scanner(System.in);
        System.out.println("~~~ Register ~~~");
        System.out.println("Please ensure your password length is at least 8 characters long.");
        System.out.print("Name: ");
        String name = in.nextLine();
        System.out.print("Username: ");
        String username = in.nextLine();
        System.out.print("Password: ");
        String password = in.nextLine();
        System.out.print("Re-entered Password: ");
        String password2 = in.nextLine();

        if(username_id.containsKey(username)) {
            System.out.println("This username has been taken");
            prompt_any();
            return -1;
        }else if(!password_length(password)||!password.equals(password2)){
            System.out.println("Passwords has less than 8 characters or passwords do no match.");
            prompt_any();
            return -1;
        }else {
            //register
            User user = new User(userenum++, username, password, name, false);
            users.add(user);
            username_id.put(username, user.getUserid());
            return user.getUserid();
        }
    }

    private static String SECRET = "Bugs";

    public static int registerAdmin() {
        Scanner in = new Scanner(System.in);
        System.out.println("~~~ Register ~~~");
        System.out.println("Please ensure your password length is at least 8 characters long.");
        System.out.print("Name: ");
        String name = in.nextLine();
        System.out.print("Username: ");
        String username = in.nextLine();
        System.out.print("Password: ");
        String password = in.nextLine();
        System.out.print("Re-entered Password: ");
        String password2 = in.nextLine();
        System.out.print("Secret Key: ");
        String secret = in.nextLine();

        if(username_id.containsKey(username)) {
            System.out.println("This username has been taken");
            prompt_any();
            return -1;
        }else if(!secret.equals(SECRET)){
            System.out.println("Secret Key does not match");
            prompt_any();
            return -1;
        }else if(!password_length(password)||!password.equals(password2)){
            System.out.println("Passwords has less than 8 characters or passwords do no match.");
            prompt_any();
            return -1;
        }else {
            User user = new User(userenum++, username, password, name, true);
            users.add(user);
            username_id.put(username, user.getUserid());
            return user.getUserid();
       }
    }

    public static boolean password_length(String s){
        return (s.length()>=8);
    }
}
