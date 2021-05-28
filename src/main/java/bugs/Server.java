package bugs;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(3308);
        Socket s = ss.accept();
        System.out.println("Connection Successful");

        //getting data from client
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader br = new BufferedReader(in);

        String str = br.readLine();
        System.out.println("Client: " + str);

        //sending data to client
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("sending data to client");
        pr.flush();
    }
}
