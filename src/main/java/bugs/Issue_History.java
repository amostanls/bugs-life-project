package bugs;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Issue_History {
    private int project_id;
    private int id;
    private int version_id;
    private String title;
    private int priority;
    private String status;
    private String[] tag;
    private String descriptionText;
    private String createdBy, assignee;
    private Timestamp timestamp;
    private ArrayList<Comment_History> comment_historyArrayList;

    public Issue_History(int project_id, int id, int version_id, String title, int priority, String status, String[] tag, String descriptionText, String createdBy, String assignee, Timestamp timestamp, ArrayList<Comment_History> comment_historyArrayList) {
        this.project_id = project_id;
        this.id = id;
        this.version_id = version_id;
        this.title = title;
        this.priority = priority;
        this.status = status;
        this.tag = tag;
        this.descriptionText = descriptionText;
        this.createdBy = createdBy;
        this.assignee = assignee;
        this.timestamp = timestamp;
        this.comment_historyArrayList = comment_historyArrayList;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion_id() {
        return version_id;
    }

    public void setVersion_id(int version_id) {
        this.version_id = version_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Comment_History> getComment_historyArrayList() {
        return comment_historyArrayList;
    }

    public void setComment_historyArrayList(ArrayList<Comment_History> comment_historyArrayList) {
        this.comment_historyArrayList = comment_historyArrayList;
    }
}
