package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread implements Runnable {
    private Socket s;
    private PrintWriter pw;
    private BufferedReader br;
    private ArrayList<ClientThread> clients;
    private String name;
    private int id;

    public ClientThread(Socket s, ArrayList<ClientThread> clients, int id) {
        try {
            this.s = s;
            this.clients = clients;
            this.pw = new PrintWriter(s.getOutputStream(), true);
            this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.id = id;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            while (!s.isClosed()) {
                String input = br.readLine();

                if (input != null) {
                    if (input.equals("get online user name")) {
                        sendClientsName();
                    } else if (input.equals("A new user is online")) {
                        String tempName = br.readLine();
                        int tempId = Integer.parseInt(br.readLine());

                        for (int i = 0; i < clients.size(); i++) {
                            if (clients.get(i).getId() == tempId) {
                                clients.get(i).setName(tempName);
                            }
                        }
                    } else if (input.equals("closing socket")) {
                        removeClient();
                        sendClientsName();
                    } else {
                        for (ClientThread client : clients) {
                            client.pw.println(input);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                s.close();
                pw.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendClientsName() {
        for (ClientThread client : clients) {
            client.pw.println("user name");
        }
        for (ClientThread client : clients) {
            for (int i = 0; i < clients.size(); i++) {
                client.pw.println(clients.get(i).getName());
            }
        }
        for (ClientThread client : clients) {
            client.pw.println("end");
        }
    }

    public void removeClient() {
        String client_id = null;
        int toBeRemoved = -1;
        try {
            client_id = br.readLine();
            for (int i = 0; i < clients.size(); i++) {
                if (String.valueOf(clients.get(i).getId()).equals(client_id)) {
                    toBeRemoved = i;
                }
            }

            if (toBeRemoved > -1) {
                clients.remove(toBeRemoved);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public PrintWriter getPw() {
        return pw;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
