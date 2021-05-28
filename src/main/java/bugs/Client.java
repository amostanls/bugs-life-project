package bugs;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import static bugs.MySQLOperation.displayIssue;
import static bugs.MySQLOperation.getIssueListByPriority;

public class Client {
    private static Scanner sc = new Scanner(System.in);
    private static Socket s = null;
    private static DataInputStream input = null;
    private static DataOutputStream out = null;

    public static void main(String[] args) throws IOException {
        s = new Socket("localhost", 3308);

        //Declare variable need for operation
        String command = "";

        //program
        command = sendOutputToServer(s);
        while(!command.equalsIgnoreCase("exit")) {
            switch(command) {
                case "print issue":
                    System.out.println(getInputFromServer(s));
                    break;
            }
            command = sendOutputToServer(s);
        }

        try {
            input.close();
            out.close();
            s.close();
        }
        catch(IOException i) {
            System.out.println(i);
        }
    }

    //user enter value
    public static String sendOutputToServer(Socket s) throws IOException {
        System.out.println("Enter string (Type Over to end typing):");

        StringBuilder sb = new StringBuilder();

        // takes input from terminal
        input  = new DataInputStream(System.in);

        // sends output to the socket
        out    = new DataOutputStream(s.getOutputStream());

        String line = "";
        line = input.readLine();
        out.writeUTF(line);
        sb.append(line);
//        boolean moreThanOneLine = false;
//
//        // keep reading until "Over" is input
//        while (!line.equals("Over"))
//        {
//            try
//            {
//                line = input.readLine();
//                out.writeUTF(line);
//
//                if (moreThanOneLine == true) {
//                    if (line.equals("Over")) {
//                        break;
//                    } else {
//                        sb.append(line + "\n");
//                    }
//                }
//                else {
//                    sb.append(line);
//                }
//
//                moreThanOneLine = true;
//            }
//            catch(IOException i)
//            {
//                System.out.println(i);
//            }
//        }

        return sb.toString();
    }

    public static String sendMultipleOutputToServer(Socket s) throws IOException {
        System.out.println("Enter string (Type Over to end typing):");

        StringBuilder sb = new StringBuilder();

        // takes input from terminal
        input  = new DataInputStream(System.in);

        // sends output to the socket
        out    = new DataOutputStream(s.getOutputStream());

        String line = "";
        boolean moreThanOneLine = false;

        // keep reading until "Over" is input
        while (!line.equals("Over"))
        {
            try
            {
                line = input.readLine();
                out.writeUTF(line);

                if (moreThanOneLine == true) {
                    if (line.equals("Over")) {
                        break;
                    } else {
                        sb.append(line + "\n");
                    }
                }
                else {
                    sb.append(line);
                }

                moreThanOneLine = true;
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        }

        return sb.toString();
    }

    public static String getInputFromServer(Socket s) throws IOException {
        input = new DataInputStream(new BufferedInputStream(s.getInputStream()));
        String line = "";
        StringBuilder sb = new StringBuilder();
        boolean moreThanOneLine = false;

        // reads message from client until "Over" is sent
        while (!line.equals("Over")) {
            try {
                line = input.readUTF();
                if (moreThanOneLine == true) {
                    if (line.equals("Over")) {
                        break;
                    } else {
                        sb.append(line + "\n");
                    }
                }
                else {
                    sb.append(line);
                }

                moreThanOneLine = true;
            }
            catch(IOException i) {
                System.out.println(i);
            }
        }
        return sb.toString();
    }
}
