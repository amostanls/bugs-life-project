package bugs;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Comment_History {
    private int comment_id;
    private int version_id;
    private String text;
    private Timestamp timestamp;
    private String user;

    public Comment_History(int comment_id, int version_id, String text, Timestamp timestamp, String user) {
        this.comment_id = comment_id;
        this.version_id = version_id;
        this.text = text;
        this.timestamp = timestamp;
        this.user = user;
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

    public ArrayList<React> getReact() {
        return react;
    }

    public void setReact(ArrayList<React> react) {
        this.react = react;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
