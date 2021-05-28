package bugs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 3308);

        String command = sendCommandToServer(s);
        while(!command.equalsIgnoreCase("exit")) {
            getInputFromServer(s);
            sendOutputToServer(s);
            command = sendCommandToServer(s);
        }
    }

    private static String sendOutputToServer(Socket s) throws IOException {
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        String output = sc.nextLine();
        pr.println(output);
        pr.flush();
        return output;
    }

    private static String getInputFromServer(Socket s) throws IOException {
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader br = new BufferedReader(in);

        String str = br.readLine();
        System.out.print("Server: " + str);
        return str;
    }

    private static String sendCommandToServer(Socket s) throws IOException {
        getInputFromServer(s);
        return sendOutputToServer(s);
    }
}
