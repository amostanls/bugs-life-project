package bugs;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Project_History {
    private int project_id;
    private int version_id;
    private String name;
    private Timestamp originalTime;

    public Project_History(int project_id, int version_id, String name, Timestamp originalTime) {
        this.project_id = project_id;
        this.version_id = version_id;
        this.name = name;
        this.originalTime = originalTime;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getVersion_id() {
        return version_id;
    }

    public void setVersion_id(int version_id) {
        this.version_id = version_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getOriginalTime() {
        return originalTime;
    }

    public void setOriginalTime(Timestamp originalTime) {
        this.originalTime = originalTime;
    }
}
