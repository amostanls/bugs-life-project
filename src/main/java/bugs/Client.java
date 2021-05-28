package bugs;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Scanner sc = new Scanner(System.in);
    private static Socket s = null;
    private static DataInputStream input = null;
    private static DataOutputStream out = null;

    public static void main(String[] args) throws IOException {
        s = new Socket("localhost", 3308);



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
    public static void sendOutputToServer(Socket s) throws IOException {
        System.out.println("Enter string (Type Over to end typing):");
        // takes input from terminal
        input  = new DataInputStream(System.in);

        // sends output to the socket
        out    = new DataOutputStream(s.getOutputStream());

        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("Over"))
        {
            try
            {
                line = input.readLine();
                out.writeUTF(line);
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        }
    }

    public static String getInputFromServer(Socket s) throws IOException {
        input = new DataInputStream(new BufferedInputStream(s.getInputStream()));
        String line = "";
        StringBuilder sb = new StringBuilder();

        // reads message from server until "Over" is sent
        while (!line.equals("Over")) {
            try {
                line = input.readUTF();
                if (line.equals("Over")) {
                    break;
                }else {
                    sb.append(line + "\n");
                }
            }
            catch(IOException i) {
                System.out.println(i);
            }
        }
        return sb.toString();
    }
}
