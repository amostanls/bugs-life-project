package bugs;

import java.util.ArrayList;

/**
 * @author Tay Qi Xiang on 4/6/2021
 */
public class MyRunnableUsers implements Runnable {
    private ArrayList<User> users;

    public MyRunnableUsers() {
        this.users = new ArrayList<>();
    }

    @Override
    public void run() {
        this.users = MySQLOperation.getUserList(MySQLOperation.getConnection());
    }

    public ArrayList<User> getUsers() {
        return users;
    }

}
