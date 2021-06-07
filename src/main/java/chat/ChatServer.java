package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    static int portNumber = 3308;
    static ServerSocket ss_static;
    private static int id;
    private static ArrayList<ClientThread> clients;

    public static void acceptClients(ServerSocket ss) {
        clients  =  new ArrayList<ClientThread>();
        id = 0;
        while(true) {
            try {
                Socket s = ss.accept();
                System.out.println("Connection Successful\n");
                ClientThread client = new ClientThread(s, clients, id);
                client.getPw().println(id);
                id++;
                Thread t = new Thread(client);
                clients.add(client);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            ss_static = new ServerSocket(portNumber);
            acceptClients(ss_static);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getPortNumber() {
        return portNumber;
    }
}
