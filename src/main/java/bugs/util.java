package bugs;

import java.util.ArrayList;
import java.util.Scanner;

public class util {
    private final String[] emojis={"happy", "sad", "angry"};
    private ArrayList<ArrayList<String>> choices = new ArrayList<>();
    //2d ArrayList
    public String reacting(String reacted){
        String res = null;
        ArrayList<String>tmp = new ArrayList<>();
        for(int i=0; i<emojis.length; i++)if(!emojis[i].equals(reacted))tmp.add(emojis[i]);
        tmp.add("0. Go Back");
        for(int i=0; i<tmp.size(); i++) {
            tmp.add(String.format("%d. %s", i+1, tmp.get(i)));
        }
        int ind = choice0(tmp.size());
        if(ind>0) {
            res = choices.get(choices.size()-1).get(ind);
        }
        choices.remove(choices.size()-1);
        return res;
    };

    public static String ask(int len, String command) {
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.print(command);
            String s = in.next();
            if (isInteger(s)) {
                Integer res = Integer.parseInt(s);
                if(0<=res&&res<=len) {
                    return res.toString();
                }else {
                    System.out.println("Invalid Input");
                }
            }else if(s.equals("s")||s.equals("c")){
                return s;
            }else{
                System.out.println("Invalid input");
            }
        }
    }

    public static int ask0(int len, String command) {
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.print(command);
            String s = in.next();
            if (isInteger(s)) {
                int res = Integer.parseInt(s);
                if(0<=res&&res<=len) {
                    return res;
                }else {
                    System.out.println("Invalid Input");
                }
            }else {
                System.out.println("Invalid input");
            }
        }
    }

    public static int ask1(int len, String command) {
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.print(command);
            String s = in.next();
            if (isInteger(s)) {
                int res = Integer.parseInt(s);
                if(1<=res&&res<=len) {
                    return res;
                }else {
                    System.out.println("Invalid Input");
                }
            }else {
                System.out.println("Invalid input");
            }
        }
    }

    public int choice0(int len) {
        Scanner in = new Scanner(System.in);
        while(true){
            displayChoices();
            System.out.printf("%s %d %s %d %s ","Enter Your Choice between",0,"and",len,":");
            String s = in.next();
            if (isInteger(s)) {
                int res = Integer.parseInt(s);
                if(0<=res&&res<=len) {
                    return res;
                }else {
                    System.out.println("Invalid Input");
                }
            }else {
                System.out.println("Invalid input");
            }
        }
    }

    public int choice1(int len) {
        Scanner in = new Scanner(System.in);
        while(true){
            displayChoices();
            System.out.printf("%s %d %s %d %s ","Enter Your Choice between",1,"and",len,":");
            String s = in.next();
            if (isInteger(s)) {
                int res = Integer.parseInt(s);
                if(0<=res&&res<=len) {
                    return res;
                }else {
                    System.out.println("Invalid Input");
                }
            }else {
                System.out.println("Invalid input");
            }
        }
    }

    public void displayChoices() {
        int last = choices.size()-1;
        for(int i = 0; i< choices.get(last).size(); i++)
            System.out.println(choices.get(last).get(i));
    }

    public static boolean isInteger(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public String[] getEmojis() {
        return emojis;
    }

    public void addChoices(ArrayList<String>choices) {
        this.choices.add(choices);
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
}
