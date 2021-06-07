package chat;

import com.jfoenix.controls.JFXTextArea;
import home.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatClientController implements Runnable, Initializable {
    private static String name = Controller.getUsername();
    private static String id;

    private static Socket s;
    private static BufferedReader brServerIn;
    private static PrintWriter pw;
    private static Thread server;
    private int portNumber = 3308;
    private static ArrayList<String> clientsName;

    @FXML
    private JFXTextArea messageArea;

    @FXML
    private JFXTextArea onlineUserArea;

    @FXML
    private TextField chatField;

    @FXML
    void enterChat(ActionEvent event) throws IOException {
        String chat = chatField.getText();
        pw.println(name + " > " + chat);
        chatField.clear();
    }

    @FXML
    public void onEnter(ActionEvent event) {
        String chat = chatField.getText();
        pw.println(name + " > " + chat);
        chatField.clear();
    }

    @Override
    public void run() {
        try {
            pw = new PrintWriter(s.getOutputStream(), true);
            brServerIn = new BufferedReader(new InputStreamReader(s.getInputStream()));

            setId(brServerIn.readLine());
            setUpChatData();

            while (!s.isClosed()) {
                if (brServerIn.ready()) {
                    String input = brServerIn.readLine();
                    if (input != null) {
                        if (input.equals("user name")) {
                            getClientsNameList();
                        } else {
                            messageArea.appendText(input + "\n");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                s.close();
                server.stop();
                pw.close();
                brServerIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getChatClientConnection();
    }

    public void setUpChatData() {
        pw.println("A new user is online");
        pw.println(name);
        pw.println(id);
        pw.println("get online user name");
    }

    public static void closeClient() {
        try {
            pw.println("closing socket");
            pw.println(id);
            s.close();
            pw.close();
            brServerIn.close();
            server.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getChatClientConnection() {
        try {
            this.s = new Socket("127.0.0.1", portNumber);
            //Thread.sleep(1000);
            server = new Thread(this);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getClientsNameList() {
        String input;
        clientsName = new ArrayList<>();
        try {
            while (!(input = brServerIn.readLine()).equals("end")) {
                clientsName.add(input);
            }

            onlineUserArea.clear();
            onlineUserArea.appendText("User: " + Controller.getUsername() + "\n\n");
            for (String name : clientsName) {
                if (!name.equals(Controller.getUsername())) {
                    onlineUserArea.appendText(name + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setId(String id) {
        ChatClientController.id = id;
    }
}
