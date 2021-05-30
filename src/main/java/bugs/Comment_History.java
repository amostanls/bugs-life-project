package bugs;

import java.sql.Timestamp;

public class Comment_History {
    private int project_id;
    private int issue_id;
    private int comment_id;
    private int version_id;
    private String text;
    private Timestamp comment_timestamp;
    private String user;

    public Comment_History(int comment_id, int version_id, String text, Timestamp comment_timestamp, String user) {
        this.comment_id = comment_id;
        this.version_id = version_id;
        this.text = text;
        this.comment_timestamp = comment_timestamp;
        this.user = user;
    }

    public Comment_History(int project_id, int issue_id, int comment_id, int version_id, String text, Timestamp comment_timestamp, String user) {
        this.project_id = project_id;
        this.issue_id = issue_id;
        this.comment_id = comment_id;
        this.version_id = version_id;
        this.text = text;
        this.comment_timestamp = comment_timestamp;
        this.user = user;
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

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getVersion_id() {
        return version_id;
    }

    public void setVersion_id(int version_id) {
        this.version_id = version_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getComment_timestamp() {
        return comment_timestamp;
    }

    public void setComment_timestamp(Timestamp comment_timestamp) {
        this.comment_timestamp = comment_timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
