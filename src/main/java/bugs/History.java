package bugs;

import java.util.ArrayList;

public class History {
    private ArrayList<Project_History> project_histories;
    private ArrayList<Issue_History> issue_histories;
    private ArrayList<Comment_History> comment_histories;

    public History() {
    }

    public History(ArrayList<Project_History> project_histories, ArrayList<Issue_History> issue_histories, ArrayList<Comment_History> comment_histories) {
        this.project_histories = project_histories;
        this.issue_histories = issue_histories;
        this.comment_histories = comment_histories;
    }

    public ArrayList<Project_History> getProject_histories() {
        return project_histories;
    }

    public void setProject_histories(ArrayList<Project_History> project_histories) {
        this.project_histories = project_histories;
    }

    public ArrayList<Issue_History> getIssue_histories() {
        return issue_histories;
    }

    public void setIssue_histories(ArrayList<Issue_History> issue_histories) {
        this.issue_histories = issue_histories;
    }

    public ArrayList<Comment_History> getComment_histories() {
        return comment_histories;
    }

    public void setComment_histories(ArrayList<Comment_History> comment_histories) {
        this.comment_histories = comment_histories;
    }
}
