package bugs;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    //all process including data storage, data processing, data retrieval, data query, and so on should be done by server
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(3308);
        Socket s = ss.accept();
        System.out.println("Connection Successful\n");

        String command = getCommandFromClient(s);
        while(!command.equalsIgnoreCase("exit")) {
            sendOutputToClient(s, "Enter anything u want to print: ");
            System.out.println(getInputFromClient(s));
            command = getCommandFromClient(s);
        }
    }

    private static String getInputFromClient(Socket s) throws IOException {
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader br = new BufferedReader(in);

        String str = br.readLine();
        return str;
    }

    private static void sendOutputToClient(Socket s, String output) throws IOException {
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(output);
        pr.flush();
    }

    private static String getCommandFromClient(Socket s) throws IOException {
        sendOutputToClient(s,"Enter command: ");
        return getInputFromClient(s);
    }
}
