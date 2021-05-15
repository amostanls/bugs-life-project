package bugs;

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
import java.util.*;
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
password VARCHAR(25),
admin boolean
);

ALTER TABLE users ADD UNIQUE(userid);
ALTER TABLE users ADD UNIQUE(username);
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
        String INSERT_USER = "INSERT INTO users (userid, username, password, admin) VALUES (?, ?, ?, ?)";

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
            updateUser.setBoolean(4,false);
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

    public static List<Project> getProjectList() {
        Connection myConn = null;
        Statement stmt = null;
        ResultSet myRs = null;
        List<Project> projectList = new ArrayList<>();

        try {
            myConn = getConnection();
            String SQL_GET_PROJECT_LIST = "SELECT * FROM projects ORDER BY project_id";
            stmt = myConn.createStatement();
            myRs = stmt.executeQuery(SQL_GET_PROJECT_LIST);
            //get parameter for creating issue object
            while (myRs.next()) {
                int id = myRs.getInt("project_id");
                String name = myRs.getString("name");
                List<Issue> issues = getIssueListByPriority(id);
                projectList.add(new Project(id, name, issues));
            }

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return projectList;
    }

    private static int getLastProjectID() {
        Connection myConn = null;
        Statement stmt = null;
        ResultSet myRs = null;

        try {
            myConn = getConnection();
            String SQL_GET_LAST_PROJECT_ID = "SELECT * FROM projects ORDER BY project_id DESC LIMIT 1";
            stmt = myConn.createStatement();
            myRs = stmt.executeQuery(SQL_GET_LAST_PROJECT_ID);

            //get parameter for creating issue object
            myRs.next();
            return myRs.getInt("project_id");

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    private static boolean containProjectID(List<Project> allProjects, int project_id) {
        for (Project p : allProjects) {
            if (p.getId() == project_id) {
                return true;
            }
        }
        return false;
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
                ArrayList<Comment> comments = (ArrayList<Comment>) getCommentList(project_id, issue_id);
                Issue newIssue = new Issue(issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments);
                issueList.add(newIssue);
            }

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (myRs != null) {
                try {
                    myRs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (myConn != null) {
                try {
                    myConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return issueList;
        }
    }

    //get particular issue list for  project
    public static List<Issue> getIssueListByPriority(int project_id) {
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        List<Issue> issueList = new ArrayList<>();

        try {
            myConn = getConnection();
            String SQL_GET_ISSUE_LIST = "SELECT * FROM issues WHERE project_id = ? ORDER BY priority DESC";
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
                ArrayList<Comment> comments = (ArrayList<Comment>) getCommentList(project_id, issue_id);
                Issue newIssue = new Issue(issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments);
                issueList.add(newIssue);
            }

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (myRs != null) {
                try {
                    myRs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (myConn != null) {
                try {
                    myConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return issueList;
        }
    }

    public static List<Issue> getIssueListByTimestamp(int project_id) {
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        List<Issue> issueList = new ArrayList<>();

        try {
            myConn = getConnection();
            String SQL_GET_ISSUE_LIST = "SELECT * FROM issues WHERE project_id = ? ORDER BY issue_timestamp DESC";
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
                ArrayList<Comment> comments = (ArrayList<Comment>) getCommentList(project_id, issue_id);
                Issue newIssue = new Issue(issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments);
                issueList.add(newIssue);
            }

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return issueList;
    }

    private static int getLastIssueID(int project_id) {
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
            myConn = getConnection();
            String SQL_GET_LAST_ISSUE_ID = "SELECT * FROM issues WHERE project_id = ? ORDER BY issue_id DESC LIMIT 1";
            pstmt = myConn.prepareStatement(SQL_GET_LAST_ISSUE_ID);
            pstmt.setInt(1, project_id);
            myRs = pstmt.executeQuery();

            //get parameter for creating issue object
            myRs.next();
            return myRs.getInt("issue_id");

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    //get particular issue using id
    public static Issue selectIssue(int project_id, int issue_id) {
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
            ArrayList<Comment> comments = (ArrayList<Comment>) null;

            return new Issue(issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments);

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private static String getKeyWord(String str) {
        String[] strArr = str.split(" ");
        if (strArr.length == 1) {
            return str;
        }
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < strArr.length; i++) {
            builder.append(strArr[i]);
            if (i != strArr.length - 1) {
                builder.append("|");
            }
        }
        return builder.toString();
    }

    private static List<Issue> searchIssueFromTitleAndDescription(int project_id, String str) {
        String searchStr = getKeyWord(str);
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        List<Issue> issueList = new ArrayList<>();

        try {
            myConn = getConnection();
            String SQL_GET_ISSUE_LIST = "SELECT * FROM issues WHERE project_id = ? AND (title = '\"+str+\"' OR title REGEXP ? OR descriptionText REGEXP ?)";
            pstmt = myConn.prepareStatement(SQL_GET_ISSUE_LIST);
            pstmt.setInt(1, project_id);
            pstmt.setString(2, searchStr);
            pstmt.setString(3, searchStr);

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
                ArrayList<Comment> comments = (ArrayList<Comment>) getCommentList(project_id, issue_id);
                Issue newIssue = new Issue(issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments);
                issueList.add(newIssue);
            }

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return issueList;
    }

    private static List<Issue> searchIssueFromComment(int project_id, String str) {
        List<Issue> issueList = getIssueListByPriority(project_id);
        List<Issue> newIssueList = new ArrayList<>();

        for (int i = 0; i < issueList.size(); i++) {

            String searchStr = getKeyWord(str);
            Connection myConn = null;
            PreparedStatement pstmt = null;
            ResultSet myRs = null;

            try {
                myConn = getConnection();
                String SQL_GET_COMMENT_LIST = "SELECT * FROM comments WHERE project_id = ? AND issue_id = ? AND text REGEXP ?";
                pstmt = myConn.prepareStatement(SQL_GET_COMMENT_LIST);
                pstmt.setInt(1, project_id);
                pstmt.setInt(2, issueList.get(i).getId());
                pstmt.setString(3, searchStr);
                myRs = pstmt.executeQuery();

                //get parameter for creating issue object
                while (myRs.next()) {
                    int issue_id = myRs.getInt("issue_id");
                    Issue newIssue = selectIssue(project_id, issue_id);
                    newIssueList.add(newIssue);
                }

            } catch (Exception ex) {
                Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return newIssueList;
    }

    public static List<Issue> searchIssue(int project_id, String str) {
        List<Issue> issueListFromTextAndDescription = searchIssueFromTitleAndDescription(project_id, str);
        List<Issue> issueListFromComment = searchIssueFromComment(project_id, str);
        List<Issue> newIssueList = issueListFromTextAndDescription;

        for (int i = 0; i < issueListFromComment.size(); i++) {
            if (!newIssueList.contains(issueListFromComment.get(i))) {
                newIssueList.add(issueListFromComment.get(i));
            }
        }

        return newIssueList;
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
                Timestamp timestamp = myRs.getTimestamp("comment_timestamp");
                String user = myRs.getString("user");
                Comment newComment = new Comment(comment_id, text, timestamp, user);
                commentList.add(newComment);
            }

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (myRs != null) {
                try {
                    myRs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (myConn != null) {
                try {
                    myConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return commentList;
        }
    }

    private static int getLastCommentID(int project_id, int issue_id) {
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
            myConn = getConnection();
            String SQL_GET_LAST_COMMENT_ID = "SELECT * FROM comments WHERE project_id = ? AND issue_id = ? ORDER BY comment_id DESC LIMIT 1";
            pstmt = myConn.prepareStatement(SQL_GET_LAST_COMMENT_ID);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            myRs = pstmt.executeQuery();

            //get parameter for creating issue object
            myRs.next();
            return myRs.getInt("comment_id");

        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
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
        } finally {
            if (myRs != null) {
                try {
                    myRs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (myConn != null) {
                try {
                    myConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return reactList;
        }
    }

    public static int showProjectDashboard() {
        boolean inputIsInt = false;
        Scanner sc = new Scanner(System.in);
        List<Project> projectList = getProjectList();
        System.out.printf("\n%s\n%-3s %-20s %-20s\n", "Project board", "ID", "Project Name", "Issue");

        for (int i = 0; i < projectList.size(); i++) {
            System.out.printf("%-3d %-20s %-20d\n", projectList.get(i).getId(), projectList.get(i).getName(), projectList.get(i).getIssues().size());
        }

        while (!inputIsInt) {
            try {
                System.out.print("\nEnter selected project ID to check project: ");
                int selection = Integer.parseInt(sc.nextLine());
                if (containProjectID(projectList, selection)) {
                    return selection;
                } else {
                    System.out.println("No such project, please enter again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("WARNING: Only integer is allowed.");
            }
        }

        return -1;
    }

    private static boolean isInt(String str) {
        if (str == null) {
            return false;
        }

        try {
            int integer = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    public static void showIssueDashboard(int project_id, String sortedBy, String username) {
        boolean correctInput = false;
        Scanner sc = new Scanner(System.in);
        List<Issue> issueList = new ArrayList<>();

        //get different order of issue list order from timestamp or priority
        if (sortedBy.equalsIgnoreCase("Timestamp")) {
            issueList = getIssueListByTimestamp(project_id);
        } else if (sortedBy.equalsIgnoreCase("Priority")){
            issueList = getIssueListByPriority(project_id);
        }

        displayIssue(issueList);

        while (!correctInput) {
            System.out.print("\nEnter selected issue ID to check issue\n" +
                    "or 's' to search\n" +
                    "or 'c' to create issue");
            String input = sc.nextLine();

            if (isInt(input)) {
                correctInput = true;
                selectIssue(project_id, Integer.parseInt(input));
            } else if (input.equals("s")) {
                correctInput = true;
                System.out.print("Enter keyword you would like to search: ");
                String str = sc.nextLine();
                searchIssue(project_id, str);
            } else if (input.equals("s")) {
                correctInput = true;
                createIssue(project_id, username);
            }
        }
    }

    public static void displayIssue(List<Issue> issueList) {
        System.out.printf("\n%s\n%-3s %-40s %-20s %-15s %-15s %-22s %-20s %-20s\n", "Issue board", "ID", "Title", "Status", "Tag", "Priority", "Time", "Assignee", "createdBy");

        for (int i = 0; i < issueList.size(); i++) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(issueList.get(i).getTimestamp());
            System.out.printf("%-3d %-40s %-20s %-15s %-15d %-22s %-20s %-20s\n", issueList.get(i).getId(), issueList.get(i).getTitle(), issueList.get(i).getStatus(), issueList.get(i).getTag()[0], issueList.get(i).getPriority(), timeStamp, issueList.get(i).getAssignee(), issueList.get(i).getCreatedBy());
        }
    }

    private static User login(String username, String password) {
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
            myConn = getConnection();
            myConn.setAutoCommit(false);
            String SQL_CHECK_USERNAME_AND_PASSWORD = "SELECT * FROM users WHERE username = ? AND password = ?";
            pstmt = myConn.prepareStatement(SQL_CHECK_USERNAME_AND_PASSWORD);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            myRs = pstmt.executeQuery();

            //get parameter for creating User object
            if (myRs.next()) {
                int userid = myRs.getInt("userid");
                boolean admin = myRs.getBoolean("admin");
                User newUser = new User(userid, username, password, admin);
                return newUser;
            }

            myConn.commit();
        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if(myRs!=null){
                try{
                    myRs.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(pstmt!=null){
                try{
                    pstmt.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(myConn!=null){
                try{
                    myConn.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static User showLoginPage() {
        Scanner sc = new Scanner(System.in);
        User newUser = null;

        while (newUser == null) {
            //reques user input
            System.out.print("Enter user name: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();

            //assign new user object if password and username exist
            newUser = login(username,password);

            //notification if wrong password / username
            if (newUser == null) {
                System.out.println("WARNING: Wrong user name or password, please try again.\n");
            }
        }

        return newUser;
    }

    //realated to tag
    public static void createIssue(int project_id, String username) {
        Scanner sc = new Scanner(System.in);
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        int issue_id = getLastIssueID(project_id) + 1;
        String title;
        int priority;
        String status = "In Progress";
        String[] tag = new String[5];
        String descriptionText;
        String createdBy = username, assignee;
        Timestamp issue_timestamp;
        issue_timestamp = new java.sql.Timestamp(new Date().getTime());
        List<Comment> commentList = new ArrayList<>();
        Issue newIssue = null;

        System.out.print("\nEnter title: ");
        title = sc.nextLine();
        System.out.print("Enter tag: ");
        tag[0] = sc.nextLine();
        System.out.print("Enter descriptive text: ");
        descriptionText = sc.nextLine();
        System.out.print("Enter priority: ");
        priority = Integer.parseInt(sc.nextLine());
        System.out.print("Enter assignee: ");
        assignee = sc.nextLine();

        try {
            myConn = getConnection();
            String SQL_CREATE_ISSUE = "INSERT INTO issues(project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = myConn.prepareStatement(SQL_CREATE_ISSUE);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            pstmt.setString(3, title);
            pstmt.setInt(4, priority);
            pstmt.setString(5, status);
            pstmt.setString(6, tag[0]);
            pstmt.setString(7, descriptionText);
            pstmt.setString(8, createdBy);
            pstmt.setString(9, assignee);
            pstmt.setTimestamp(10, issue_timestamp);
            pstmt.execute();
        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createComment(int project_id, int issue_id, String username) {
        Scanner sc = new Scanner(System.in);
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        int comment_id = getLastCommentID(project_id, issue_id) + 1;
        String text;
        Timestamp comment_timestamp;
        comment_timestamp = new java.sql.Timestamp(new Date().getTime());

        System.out.print("Enter text: ");
        text = sc.nextLine();

        try {
            myConn = getConnection();
            String SQL_CREATE_ISSUE = "INSERT INTO comments(project_id, issue_id, comment_id, text, comment_timestamp, user) VALUES (?,?,?,?,?,?)";
            pstmt = myConn.prepareStatement(SQL_CREATE_ISSUE);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            pstmt.setInt(3, comment_id);
            pstmt.setString(4, text);
            pstmt.setTimestamp(5,comment_timestamp);
            pstmt.setString(6,username);
            pstmt.execute();
        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void showUserInterface() {
        User currentUser = showLoginPage();
        int selectionOfProject = showProjectDashboard();
        showIssueDashboard(selectionOfProject,"priority", currentUser.getUsername());
    }

    public static void main(String[] args) {
//        initializedDatabase();
//        showUserInterface();
        showIssueDashboard(1,"priority","jhoe");
    }
}
