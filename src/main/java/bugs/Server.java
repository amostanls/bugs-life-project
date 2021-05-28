package bugs;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.List;

import static bugs.MySQLOperation.*;

public class Server {
    private static Socket s = null;
    private static DataInputStream input = null;
    private static DataOutputStream out = null;

    //all process including data storage, data processing, data retrieval, data query, and so on should be done by server
    public static void main(String[] args) throws IOException {

        //Establish connection to mysql and socket
        Connection myConn = getConnection();
        ServerSocket ss = new ServerSocket(3308);
        Socket s = ss.accept();
        System.out.println("Connection Successful\n");

        //Declare variable need for operation
        String command = "";

        //program
        command = getInputFromClient(s);
        while(!command.equalsIgnoreCase("exit")) {
            switch(command) {
                case "print issue":
                    List<Issue> list = getIssueListByPriority(myConn,1);
                    sendInstructionToClient(s, displayIssue(list));
                    break;
            }
            command = getInputFromClient(s);
        }

        //close
        System.out.println("Closing connection");
        s.close();
        input.close();
    }

    public static String getInputFromClient(Socket s) throws IOException {
        input = new DataInputStream(new BufferedInputStream(s.getInputStream()));
        String line = "";
        StringBuilder sb = new StringBuilder();
        line = input.readUTF();
        sb.append(line);

//        boolean moreThanOneLine = false;
//
//        // reads message from client until "Over" is sent
//        while (!line.equals("Over")) {
//            try {
//                line = input.readUTF();
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
//            catch(IOException i) {
//                System.out.println(i);
//            }
//        }
        return sb.toString();
    }

    public static void sendInstructionToClient(Socket s, String instruction) throws IOException {
        out = new DataOutputStream(s.getOutputStream());
        out.writeUTF(instruction);
        out.writeUTF("Over");
    }
}
