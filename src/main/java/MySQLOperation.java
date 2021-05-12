
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*  OPERATION FOR MYSQL
SHOW DATABASES;
USE sql6411496;
SHOW TABLES;
SELECT * FROM projects;
SELECT * FROM issues;
SELECT * FROM comments;
SELECT * FROM react;
SELECT * FROM users;

DROP TABLE projects;
DROP TABLE issues;
DROP TABLE comments;
DROP TABLE react;
DROP TABLE users;

CREATE TABLE projects (
project_id INT, 
name VARCHAR(20));

CREATE TABLE issues (
project_id INT, 
issue_id INT, 
title VARCHAR(50), 
priority INT, 
status VARCHAR(20), 
tag VARCHAR(20), 
descriptionText VARCHAR(500), 
createdBy VARCHAR(20), 
assignee VARCHAR(20), 
issue_timestamp TIMESTAMP);

CREATE TABLE comments (
project_id INT, 
issue_id INT, 
comment_id INT, 
text VARCHAR(250), 
comment_timestamp TIMESTAMP, 
user VARCHAR(25));

CREATE TABLE react (
project_id INT, 
issue_id INT, 
comment_id INT, 
reaction VARCHAR(10), 
count INT);

CREATE TABLE users (
userid INT,
username VARCHAR(25),
password VARCHAR(25)
);

ALTER TABLE users ADD UNIQUE(userid);

 */
public class MySQLOperation {

    private static Connection getConnection() throws Exception {
        final String user = "sql6411496";
        final String pass = "eIXUjnTvTP";
        final String path = "jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6411496?zeroDateTimeBehavior=CONVERT_TO_NULL";
        final String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        Connection myConn = DriverManager.getConnection(path, user, pass);
        return myConn;
    }

    private static void updateDatabaseFromUrl(Connection myConn, String url) throws SQLException, MalformedURLException, IOException, ParseException {
        URL jsonUrl = new URL(url);
        JsonNode node = Json.parse(jsonUrl);

        String INSERT_PROJECT = "INSERT INTO projects (project_id, name) VALUE (?,?)";
        String INSERT_ISSUE = "INSERT INTO issues (project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; //10
        String INSERT_COMMENT = "INSERT INTO comments (project_id, issue_id, comment_id, text, comment_timestamp, user) VALUES (?, ?, ?, ?, ?, ?)";  //6
        String INSERT_REACT = "INSERT INTO react (project_id, issue_id, comment_id, reaction, count) VALUES (?, ?, ?, ?, ?)"; //2
        String INSERT_USER = "INSERT INTO users (userid, username, password) VALUES (?, ?, ?)";

        PreparedStatement updateProject = myConn.prepareStatement(INSERT_PROJECT, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement updateIssue = myConn.prepareStatement(INSERT_ISSUE, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement updateComment = myConn.prepareStatement(INSERT_COMMENT, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement updateReact = myConn.prepareStatement(INSERT_REACT, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement updateUser = myConn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);

        //add projects
        for (int i = 0; i < node.get("projects").size(); i++) {
            updateProject.setInt(1, node.get("projects").get(i).get("id").asInt());
            updateProject.setString(2, node.get("projects").get(i).get("name").asText());
            updateProject.addBatch();

            //add issues
            for (int j = 0; j < node.get("projects").get(i).get("issues").size(); j++) {
                updateIssue.setInt(1, node.get("projects").get(i).get("id").asInt());
                updateIssue.setInt(2, node.get("projects").get(i).get("issues").get(j).get("id").asInt());
                updateIssue.setString(3, node.get("projects").get(i).get("issues").get(j).get("title").asText());
                updateIssue.setInt(4, node.get("projects").get(i).get("issues").get(j).get("priority").asInt());
                updateIssue.setString(5, node.get("projects").get(i).get("issues").get(j).get("status").asText());
                updateIssue.setString(6, node.get("projects").get(i).get("issues").get(j).withArray("tag").get(0).asText());
                updateIssue.setString(7, node.get("projects").get(i).get("issues").get(j).get("descriptionText").asText());
                updateIssue.setString(8, node.get("projects").get(i).get("issues").get(j).get("createdBy").asText());
                updateIssue.setString(9, node.get("projects").get(i).get("issues").get(j).get("assignee").asText());

                //convert string timestamp to timestamp
                Timestamp newTs = convertStringTimestamp(node.get("projects").get(i).get("issues").get(j).get("timestamp").asText());
                updateIssue.setTimestamp(10, newTs);
                updateIssue.addBatch();

                //add comments
                for (int k = 0; k < node.get("projects").get(i).get("issues").get(j).get("comments").size(); k++) {
                    updateComment.setInt(1, node.get("projects").get(i).get("id").asInt());
                    updateComment.setInt(2, node.get("projects").get(i).get("issues").get(j).get("id").asInt());
                    updateComment.setInt(3, node.get("projects").get(i).get("issues").get(j).get("comments").get(k).get("comment_id").asInt());
                    updateComment.setString(4, node.get("projects").get(i).get("issues").get(j).get("comments").get(k).get("text").asText());
                    
                    newTs = convertStringTimestamp(node.get("projects").get(i).get("issues").get(j).get("timestamp").asText());
                    updateComment.setTimestamp(5, newTs);
                    updateComment.setString(6, node.get("projects").get(i).get("issues").get(j).get("comments").get(k).get("user").asText());
                    updateComment.addBatch();

                    //add react
                    for (int l = 0; l < node.get("projects").get(i).get("issues").get(j).get("comments").get(k).get("react").size(); l++) {
                        updateReact.setInt(1, node.get("projects").get(i).get("id").asInt());
                        updateReact.setInt(2, node.get("projects").get(i).get("issues").get(j).get("id").asInt());
                        updateReact.setInt(3, node.get("projects").get(i).get("issues").get(j).get("comments").get(k).get("comment_id").asInt());
                        updateReact.setString(4, node.get("projects").get(i).get("issues").get(j).get("comments").get(k).get("react").get(l).get("reaction").asText());
                        updateReact.setInt(5, node.get("projects").get(i).get("issues").get(j).get("comments").get(k).get("react").get(l).get("count").asInt());
                        updateReact.addBatch();

                        if (i == node.get("projects").size() - 1) {
                            updateProject.executeBatch();
                            updateComment.executeBatch();
                            updateIssue.executeBatch();
                            updateReact.executeBatch();
                        }
                    }
                }
            }
        }

        //add user information
        for (int i = 0; i < node.get("users").size(); i++) {
            updateUser.setInt(1, node.get("users").get(i).get("userid").asInt());
            updateUser.setString(2, node.get("users").get(i).get("username").asText());
            updateUser.setString(3, node.get("users").get(i).get("password").asText());
            updateUser.addBatch();

            if (i == node.get("users").size() - 1) {
                updateUser.executeBatch();
            }
        }
    }

    private static Timestamp convertStringTimestamp(String str) {
        String time = str + "000";
        SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        Date date = new Date(Long.parseLong(time));
        long longtime = date.getTime();
        Timestamp newTs = new Timestamp(longtime);
        return newTs;
    }

    private static void initializedDatabase() {
        Connection myConn = null;
        try {
            myConn = getConnection();
            myConn.setAutoCommit(false);
            updateDatabaseFromUrl(myConn, "https://jiuntian.com/data.json");
            myConn.commit();
        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void reactHappy(int project_id, int issue_id, int comment_id) throws Exception {
        Connection myConn = getConnection();
        String incHappyCount = "UPDATE react SET count = count + 1 WHERE project_id = ? AND issue_id = ? AND comment_id = ? AND reaction = 'happy'";
        PreparedStatement updateCount = myConn.prepareStatement(incHappyCount, Statement.RETURN_GENERATED_KEYS);
        updateCount.setInt(1, project_id);
        updateCount.setInt(2, issue_id);
        updateCount.setInt(3, comment_id);
        updateCount.execute();
    }

    public static void reactAngry(int project_id, int issue_id, int comment_id) throws Exception {
        Connection myConn = getConnection();
        String incHappyCount = "UPDATE react SET count = count + 1 WHERE project_id = ? AND issue_id = ? AND comment_id = ? AND reaction = 'angry'";
        PreparedStatement updateCount = myConn.prepareStatement(incHappyCount, Statement.RETURN_GENERATED_KEYS);
        updateCount.setInt(1, project_id);
        updateCount.setInt(2, issue_id);
        updateCount.setInt(3, comment_id);
        updateCount.execute();
    }

    //get particular issue using id
    public static Issue getIssue(int project_id, int issue_id) {
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
            myConn = getConnection();
            String SQL_SEARCH = "SELECT * FROM issues WHERE project_id = ? AND issue_id = ?";
            pstmt = myConn.prepareStatement(SQL_SEARCH);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            myRs = pstmt.executeQuery();

            //get parameter for creating issue object
            myRs.next();
            String title = myRs.getString("title");
            int priority = myRs.getInt("priority");
            String status = myRs.getString("status");
            String[] tag = {myRs.getString("tag")};
            String descriptionText = myRs.getString("descriptionText");
            String createdBy = myRs.getString("createdBy");
            String asignee = myRs.getString("assignee");
            Timestamp issue_timestamp = myRs.getTimestamp("issue_timestamp");
            List<Comment> comments = null;

            return new Issue(issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments);

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static List<Project> getProjectList() {
        Connection myConn = null;
        Statement stmt = null;
        ResultSet myRs = null;
        List<Project> projectList = new ArrayList<>();

        try {
            myConn = getConnection();
            String SQL_GET_PROJECT_LIST = "SELECT * FROM projects;";
            stmt = myConn.createStatement();

            myRs = stmt.executeQuery(SQL_GET_PROJECT_LIST);

            //get parameter for creating issue object
            while (myRs.next()) {
                int id = myRs.getInt("project_id");
                String name = myRs.getString("name");
                List<Issue> issueList = getIssueList(id);
                Project newProject = new Project(id, name, issueList);
                projectList.add(newProject);
            }

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return projectList;
    }

    //get particular issue list for  project
    public static List<Issue> getIssueList(int project_id) {
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        List<Issue> issueList = new ArrayList<>();

        try {
            myConn = getConnection();
            String SQL_GET_ISSUE_LIST = "SELECT * FROM issues WHERE project_id = ?";
            pstmt = myConn.prepareStatement(SQL_GET_ISSUE_LIST);
            pstmt.setInt(1, project_id);
            myRs = pstmt.executeQuery();
            //get parameter for creating issue object
            while (myRs.next()) {
                int issue_id = myRs.getInt("issue_id");
                String title = myRs.getString("title");
                int priority = myRs.getInt("priority");
                String status = myRs.getString("status");
                String[] tag = {myRs.getString("tag")};
                String descriptionText = myRs.getString("descriptionText");
                String createdBy = myRs.getString("createdBy");
                String asignee = myRs.getString("assignee");
                Timestamp issue_timestamp = myRs.getTimestamp("issue_timestamp");
                List<Comment> comments = getCommentList(project_id, issue_id);
                Issue newIssue = new Issue(issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments);
                issueList.add(newIssue);
            }

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return issueList;
    }

    public static List<Issue> getAllIssueList() {
        Connection myConn = null;
        Statement stmt = null;
        ResultSet myRs = null;
        List<Issue> issueList = new ArrayList<>();

        try {
            myConn = getConnection();
            String SQL_GET_ALL_ISSUE_LIST = "SELECT * FROM issues";
            stmt = myConn.createStatement();

            myRs = stmt.executeQuery(SQL_GET_ALL_ISSUE_LIST);
            //get parameter for creating issue object
            while (myRs.next()) {
                int project_id = myRs.getInt("project_id");
                int issue_id = myRs.getInt("issue_id");
                String title = myRs.getString("title");
                int priority = myRs.getInt("priority");
                String status = myRs.getString("status");
                String[] tag = {myRs.getString("tag")};
                String descriptionText = myRs.getString("descriptionText");
                String createdBy = myRs.getString("createdBy");
                String asignee = myRs.getString("assignee");
                Timestamp issue_timestamp = myRs.getTimestamp("issue_timestamp");
                List<Comment> comments = getCommentList(project_id, issue_id);
                Issue newIssue = new Issue(issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments);
                issueList.add(newIssue);
            }

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return issueList;
    }

    public static List<Comment> getCommentList(int project_id, int issue_id) {
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        List<Comment> commentList = new ArrayList<>();

        try {
            myConn = getConnection();
            String SQL_GET_COMMENT_LIST = "SELECT * FROM comments WHERE project_id = ? AND issue_id = ?";
            pstmt = myConn.prepareStatement(SQL_GET_COMMENT_LIST);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            myRs = pstmt.executeQuery();
            //get parameter for creating issue object
            while (myRs.next()) {
                int comment_id = myRs.getInt("comment_id");
                String text = myRs.getString("text");
                List<React> react = getReactList(project_id, issue_id, comment_id);
                Timestamp timestamp = myRs.getTimestamp("timestamp");
                String user = myRs.getString("user");
                Comment newComment = new Comment(comment_id, text, null, timestamp, user);
                commentList.add(newComment);
            }

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return commentList;
    }

    public static List<React> getReactList(int project_id, int issue_id, int comment_id) {
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        List<React> reactList = new ArrayList<>();

        try {
            myConn = getConnection();
            String SQL_GET_REACT_LIST = "SELECT * FROM react WHERE project_id = ? AND issue_id = ? AND comment_id = ?";
            pstmt = myConn.prepareStatement(SQL_GET_REACT_LIST);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            pstmt.setInt(3, comment_id);
            myRs = pstmt.executeQuery();
            //get parameter for creating issue object
            while (myRs.next()) {
                String reaction = myRs.getString("reaction");
                int count = myRs.getInt("count");
                React newReact = new React(reaction, count);
                reactList.add(newReact);
            }

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return reactList;
    }

    public static void showProjectDashboard() {
        List<Project> projectList = getProjectList();
        System.out.printf("\n%s\n%-3s %-20s %-20s\n", "Project board", "ID", "Project Name", "Issue");

        for (int i = 0; i < projectList.size(); i++) {
            System.out.printf("%-3d %-20s %-20d\n", projectList.get(i).getId(), projectList.get(i).getName(), projectList.get(i).getIssues().size());
        }

        System.out.println("Enter selected issue ID to check project: ");
    }

    public static void showIssueDashboard(int project_id) {
        List<Issue> issueList = getIssueList(project_id);
        System.out.printf("\n%s\n%-3s %-40s %-20s %-15s %-15s %-22s %-20s %-20s\n", "Issue board", "ID", "Title", "Status", "Tag", "Priority", "Time", "Assignee", "createdBy");

        for (int i = 0; i < issueList.size(); i++) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(issueList.get(i).getTimestamp());
            System.out.printf("%-3d %-40s %-20s %-15s %-15d %-22s %-20s %-20s\n", issueList.get(i).getId(), issueList.get(i).getTitle(), issueList.get(i).getStatus(), issueList.get(i).getTag()[0], issueList.get(i).getPriority(), timeStamp, issueList.get(i).getAssignee(), issueList.get(i).getCreatedBy());
        }

        System.out.println("\nEnter selected issue ID to check issue\n" +
                "or 's' to search\n" +
                "or 'c' to create issue");
    }

//    public static void main(String[] args) {
//        initializedDatabase();
//        showProjectDashboard();
//        showIssueDashboard(1);
//    }
}
