package bugs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 3308);

        //sending data to server
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("sending data to server");
        pr.flush();

        //getting data from server
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader br = new BufferedReader(in);

        String str = br.readLine();
        System.out.println("Server: " + str);

        pr.close();
        s.close();
    }
}
