package bugs;

import java.sql.Timestamp;

public class Issue_History {
    private int project_id;
    private int issue_id;
    private int version_id;
    private String title;
    private int priority;
    private String status;
    private String[] tag;
    private String tags;
    private String descriptionText;
    private String createdBy, assignee;
    private Timestamp issue_timestamp;
    private String url;

    public Issue_History(int project_id, int issue_id, int version_id, String title, int priority, String status, String[] tag, String descriptionText, String createdBy, String assignee, Timestamp issue_timestamp, String url) {
        this.project_id = project_id;
        this.issue_id = issue_id;
        this.version_id = version_id;
        this.title = title;
        this.priority = priority;
        this.status = status;
        this.tag = tag;
        this.descriptionText = descriptionText;
        this.createdBy = createdBy;
        this.assignee = assignee;
        this.issue_timestamp = issue_timestamp;
        this.url = url;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(int issue_id) {
        this.issue_id = issue_id;
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

    public String getTags() {
        if(tag==null) return null;
        return tag[0];
    }

    public void setTags(String tags) {
        this.tag[0] = tags;
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

    public Timestamp getIssue_timestamp() {
        return issue_timestamp;
    }

    public void setIssue_timestamp(Timestamp issue_timestamp) {
        this.issue_timestamp = issue_timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
