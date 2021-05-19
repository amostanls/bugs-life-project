package bugs;

import java.util.ArrayList;
import java.util.Scanner;

public class util {
    private static ArrayList<ArrayList<String>> choices = new ArrayList<>(); //2d ArrayList

    public static String ask(int len, String command, String Choices, boolean search) {
        //for project page
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println(Choices);
            System.out.print(command);
            String s = in.nextLine();
            if (isInteger(s)) {
                Integer res = Integer.parseInt(s);
                if(0<=res&&res<=len) {
                    return res.toString();
                }else {
                    System.out.println("Invalid Input");
                    prompt_any();
                }
            }else{
                if(search&&s.equals("s"))return s;
                else if(s.equals("r"))return s;
                if(s.equals("c"))return s;
                System.out.println("Invalid input");
                prompt_any();
            }
        }
    }

    public static int ask0(int len, String command, String Choices) {
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println(Choices);
            System.out.print(command);
            String s = in.nextLine();
            if (isInteger(s)) {
                int res = Integer.parseInt(s);
                if(0<=res&&res<=len) {
                    return res;
                }else {
                    System.out.println("Invalid Input");
                    prompt_any();
                }
            }else {
                System.out.println("Invalid input");
                prompt_any();
            }
        }
    }

    public static int ask1(int len, String command, String Choices) {
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println(Choices);
            System.out.print(command);
            String s = in.nextLine();
            if (isInteger(s)) {
                int res = Integer.parseInt(s);
                if(1<=res&&res<=len) {
                    return res;
                }else {
                    System.out.println("Invalid Input");
                    prompt_any();
                }
            }else {
                System.out.println("Invalid input");
                prompt_any();
            }
        }
    }

    private final String[] emojis={"happy", "sad", "angry"};
//    public String reacting(String reacted){
//        String res = null;
//        ArrayList<String>tmp = new ArrayList<>();
//        for(int i=0; i<emojis.length; i++)if(!emojis[i].equals(reacted))tmp.add(emojis[i]);
//        tmp.add("0. Go Back");
//        for(int i=0; i<tmp.size(); i++) {
//            tmp.add(String.format("%d. %s", i+1, tmp.get(i)));
//        }
//        int ind = choice0(tmp.size());
//        if(ind>0) {
//            res = choices.get(choices.size()-1).get(ind);
//        }
//        choices.remove(choices.size()-1);
//        return res;
//    };

    public void react() {

    }

    public static int choice0(int len, boolean withchoices) {
        Scanner in = new Scanner(System.in);
        while(true){
            if(withchoices)displayChoices();
            System.out.printf("%s %d %s %d %s ","Enter Your Choice between",0,"and",len,":");
            String s = in.nextLine();
            if (isInteger(s)) {
                int res = Integer.parseInt(s);
                if(0<=res&&res<=len) {
                    if(withchoices)choices.remove(choices.size()-1);
                    return res;
                }else {
                    System.out.println("Invalid Input");
                    prompt_any();
                }
            }else {
                System.out.println("Invalid input");
                prompt_any();
            }
        }
    }

    public static int choice1(int len, boolean withchoices) {
        Scanner in = new Scanner(System.in);
        while(true){
            if(withchoices)displayChoices();
            System.out.printf("%s %d %s %d %s ","Enter Your Choice between",1,"and",len,":");
            String s = in.nextLine();
            if (isInteger(s)) {
                int res = Integer.parseInt(s);
                if(1<=res&&res<=len) {
                    if(withchoices)choices.remove(choices.size()-1);
                    return res;
                }else {
                    System.out.println("Invalid Input");
                    prompt_any();
                }
            }else {
                System.out.println("Invalid input");
                prompt_any();
            }
        }
    }

    private static String tags[]={"Frontend", "Backend", "Others"};
    //create issue's tag
    public static String specialchoice(){
        Scanner in = new Scanner(System.in);
        int len = tags.length;
        addChoices(tags);
        while(true){
            displayChoices();
            System.out.printf("%s %d %s %d %s ","Enter Your Choice between",1,"and",len,":");
            String s = in.nextLine();
            if (isInteger(s)) {
                int res = Integer.parseInt(s);
                if(1<=res&&res<len) {
                    choices.remove(choices.size()-1);
                    return tags[res-1];
                }else if(res==len){
                    choices.remove(choices.size()-1);
                    System.out.print("Others: ");
                    s = in.nextLine();
                    return s;
                }else{
                    System.out.println("Invalid Input");
                    prompt_any();
                }
            }else {
                System.out.println("Invalid input");
                prompt_any();
            }
        }
    }

    public static void displayChoices() {
        int last = choices.size()-1;
        for(int i = 0; i< choices.get(last).size(); i++)
            System.out.printf("%d. %s\n",i+1,choices.get(last).get(i));
    }

    public static boolean isInteger(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static boolean checkEnd(String s) {
        return s.equals("*****");
    }

    public String[] getEmojis() {
        return emojis;
    }

    public static void addChoices(String[] Choices) {
        ArrayList<String>tmp = new ArrayList<>();
        for(int i=0; i<Choices.length; i++)tmp.add(Choices[i]);
        choices.add(tmp);
    }

    public static String centre(int len, Object tmp) {
        String cur = (String) tmp.toString();
        String res = " ";
        int right = (len-cur.length())/2;
        int left = len-cur.length()-right;
        for(int i = 0; i < left; i++) res += " ";
        res += tmp;
        for(int i=0; i<right; i++)res+=" ";
        res += " |";
        return res;
    }

    public static String left(int len, Object tmp) {
        String cur = (String) tmp.toString();
        String res = " ";
        int right = len - cur.length();
        res += tmp;
        for(int i=0; i<right; i++)res+=" ";
        res += " |";
        return res;
    }

    public static String right(int len, Object tmp) {
        String cur = (String) tmp.toString();
        String res = " ";
        int left = len - cur.length();
        for(int i=0; i<left; i++)res+=" ";
        res += tmp;
        res += " |";
        return res;
    }

    public static String minus_plus(int len) {
        String res = "";
        for(int i=0; i<len; i++)res+="-";
        res +="+";
        return res;
    }

    public static void prompt_any(){
        Scanner in = new Scanner(System.in);
        System.out.println("Press any key to continue.");
        in.nextLine();
    }
}
