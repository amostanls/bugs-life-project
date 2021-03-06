package bugs;

import java.util.ArrayList;

public class MyRunnable implements Runnable{
    private ArrayList<Project> projects;

    public MyRunnable() {
        this.projects = new ArrayList<>();
    }

    @Override
    public void run() {
        this.projects = MySQLOperation.getProjectList(MySQLOperation.getConnection());
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }
}
