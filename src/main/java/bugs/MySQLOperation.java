package bugs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.protocol.Resultset;
import javafx.scene.control.Alert;
import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
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
USE 5peJ8pFLLQ;
SHOW TABLES;
SELECT * FROM projects;
SELECT * FROM issues;
SELECT * FROM comments;
SELECT * FROM react;
SELECT * FROM users;
SELECT * FROM projects_history;
SELECT * FROM issues_history;
SELECT * FROM comments_history;
DROP TABLE projects_history;
DROP TABLE issues_history;
DROP TABLE comments_history;
DROP TABLE projects;
DROP TABLE issues;
DROP TABLE comments;
DROP TABLE react;
DROP TABLE users;

CREATE TABLE projects (
project_id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(20) NOT NULL,
project_timestamp TIMESTAMP NOT NULL);

CREATE TABLE issues (
project_id INT NOT NULL,
issue_id INT NOT NULL,
PRIMARY KEY (project_id, issue_id),
title VARCHAR(50),
priority INT,
status VARCHAR(20),
tag VARCHAR(20),
descriptionText VARCHAR(500),
createdBy VARCHAR(20),
assignee VARCHAR(20),
issue_timestamp TIMESTAMP,
url VARCHAR(2083));

CREATE TABLE comments (
project_id INT NOT NULL,
issue_id INT NOT NULL,
comment_id INT NOT NULL,
PRIMARY KEY (project_id, issue_id, comment_id),
text VARCHAR(250),
comment_timestamp TIMESTAMP,
user VARCHAR(25));

CREATE TABLE react (
project_id INT NOT NULL,
issue_id INT NOT NULL,
comment_id INT NOT NULL,
reaction VARCHAR(10),
PRIMARY KEY (project_id, issue_id, comment_id, reaction),
count INT);

CREATE TABLE users (
userid INT,
username VARCHAR(25),
password VARCHAR(64),
admin boolean,
url VARCHAR(2083),
email VARCHAR(40)
);

ALTER TABLE users ADD UNIQUE(userid);
ALTER TABLE users ADD UNIQUE(username);

CREATE TABLE projects_history (
project_id INT NOT NULL,
version_id INT NOT NULL,
name VARCHAR(20) NOT NULL,
originalTime TIMESTAMP NOT NULL,
PRIMARY KEY (project_id, originalTime),
CONSTRAINT project_id_fk
    FOREIGN KEY profile_id_fkx (project_id)
    REFERENCES projects(project_id)
);

CREATE TABLE issues_history (
project_id INT NOT NULL,
issue_id INT NOT NULL,
version_id INT,
title VARCHAR(50),
priority INT,
status VARCHAR(20),
tag VARCHAR(20),
descriptionText VARCHAR(500),
createdBy VARCHAR(20),
assignee VARCHAR(20),
issue_timestamp TIMESTAMP,
PRIMARY KEY (project_id, issue_id, issue_timestamp),
CONSTRAINT pi_fk
    FOREIGN KEY pi_fk (project_id, issue_id)
    REFERENCES issues (project_id, issue_id)
);

CREATE TABLE comments_history (
project_id INT NOT NULL,
issue_id INT NOT NULL,
comment_id INT NOT NULL,
version_id INT NOT NULL,
text VARCHAR(250),
comment_timestamp TIMESTAMP,
user VARCHAR(25),
PRIMARY KEY (project_id, issue_id, comment_id, comment_timestamp),
CONSTRAINT pic_fk
    FOREIGN KEY pic_fkx (project_id, issue_id, comment_id)
    REFERENCES comments (project_id, issue_id, comment_id)
);
 */
public class MySQLOperation {

    public static Connection getConnection() {
        //Local Database(Local Host)
//        final String user = "root";
//        final String pass = "";
//        final String path = "jdbc:mysql://localhost:3306/bugs_life?zeroDateTimeBehavior=CONVERT_TO_NULL&allowMultiQueries=true";

        // Online Database
        final String user = "5peJ8pFLLQ";
        final String pass = "h6Tpwh3kYW";
        final String path = "jdbc:mysql://remotemysql.com:3306/5peJ8pFLLQ?zeroDateTimeBehavior=CONVERT_TO_NULL&allowMultiQueries=true";

        final String driver = "com.mysql.cj.jdbc.Driver";
        Connection myConn=null;
        try{
            Class.forName(driver);
            myConn = DriverManager.getConnection(path, user, pass);
        }catch (ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());
            //JOptionPane.showMessageDialog(null, "Connection refused!!!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Connection refused!!!");
            alert.showAndWait();
        }

        return myConn;
    }

    public static void exportJavaObjectsAsJson(Connection myConn, List objects, String fileName) {
        fileName += ".json";
        ObjectMapper om = Json.getDefaultOM();
        try {
            om.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), objects);
        } catch (IOException ex) {
            Logger.getLogger(Json.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void exportJavaObjectAsJson(Connection myConn, Object objects, String fileName) {
        fileName += ".json";
        ObjectMapper om = Json.getDefaultOM();
        try {
            om.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), objects);
        } catch (IOException ex) {
            Logger.getLogger(Json.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateDatabaseFromUrl(Connection myConn, String url) throws SQLException, MalformedURLException, IOException, ParseException {
        URL jsonUrl = new URL(url);
        JsonNode node = Json.parse(jsonUrl);

        String INSERT_PROJECT = "INSERT INTO projects (project_id, name, project_timestamp) VALUE (?,?,?)";
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

            Timestamp newTimestamp = convertStringTimestamp("1565192640");
            updateProject.setTimestamp(3, newTimestamp);
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
            updateUser.setString(3, DigestUtils.sha256Hex(node.get("users").get(i).get("password").asText()));
            updateUser.setBoolean(4, false);
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

    public static void createProject(Connection myConn, String name) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
//            project_id INT PRIMARY KEY AUTO_INCREMENT,
//                    name VARCHAR(20) NOT NULL,
//            project_timestamp TIMESTAMP NOT NULL
            String SQL_CREATE_PROJECT = "INSERT INTO projects(name, project_timestamp) VALUES (?, ?)";
            pstmt = myConn.prepareStatement(SQL_CREATE_PROJECT);
            pstmt.setString(1, name);

            Timestamp project_timestamp;
            project_timestamp = new java.sql.Timestamp(new Date().getTime());
            pstmt.setTimestamp(2, project_timestamp);

            pstmt.execute();
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
        }
    }

    public static List<Project> getProjectList(Connection myConn) {
        Statement stmt = null;
        ResultSet myRs = null;
        List<Project> projectList = new ArrayList<>();

        try {
            String SQL_GET_PROJECT_LIST = "SELECT * FROM projects ORDER BY project_id";
            stmt = myConn.createStatement();
            myRs = stmt.executeQuery(SQL_GET_PROJECT_LIST);
            //get parameter for creating issue object
            while (myRs.next()) {
                int id = myRs.getInt("project_id");
                String name = myRs.getString("name");
                List<Issue> issues = getIssueListByPriority(myConn, id);
                Timestamp ts = myRs.getTimestamp("project_timestamp");
                projectList.add(new Project(id, name, issues, ts));
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
        }
        return projectList;
    }

    public static Project getProject(Connection myConn, int project_id) {
        Statement stmt = null;
        ResultSet myRs = null;
        Project requiredProject = null;

        try {
            String SQL_GET_PROJECT_LIST = "SELECT * FROM projects WHERE project_id = '" + project_id + "'";
            stmt = myConn.createStatement();
            myRs = stmt.executeQuery(SQL_GET_PROJECT_LIST);

            myRs.next();
            String name = myRs.getString("name");
            Timestamp project_timestamp = myRs.getTimestamp("project_timestamp");
            requiredProject = new Project(project_id, name, project_timestamp);

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
        }

        return requiredProject;
    }

    private static int getLastProjectID(Connection myConn) {
        Statement stmt = null;
        ResultSet myRs = null;

        try {
            String SQL_GET_LAST_PROJECT_ID = "SELECT * FROM projects ORDER BY project_id DESC LIMIT 1";
            stmt = myConn.createStatement();
            myRs = stmt.executeQuery(SQL_GET_LAST_PROJECT_ID);

            //get parameter for creating issue object
            myRs.next();
            return myRs.getInt("project_id");

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

    public static List<Issue> getAllIssueList(Connection myConn) {
        Statement stmt = null;
        ResultSet myRs = null;
        List<Issue> issueList = new ArrayList<>();

        try {
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
                ArrayList<Comment> comments = (ArrayList<Comment>) getCommentList(myConn, project_id, issue_id);
                String url = myRs.getString("url");
                Issue newIssue = new Issue(project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments, url);
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
        }
        return issueList;
    }

    //get particular issue list for  project
    public static ArrayList<Issue> getIssueListByPriority(Connection myConn, int project_id) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        ArrayList<Issue> issueList = new ArrayList<>();

        try {
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
                ArrayList<Comment> comments = getCommentList(myConn, project_id, issue_id);
                String url = myRs.getString("url");
                Issue newIssue = new Issue(project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments, url);
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
        }

        return issueList;
    }

    public static List<Issue> getIssueListByTimestamp(Connection myConn, int project_id) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        List<Issue> issueList = new ArrayList<>();

        try {
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
                ArrayList<Comment> comments = (ArrayList<Comment>) getCommentList(myConn, project_id, issue_id);
                String url = myRs.getString("url");
                Issue newIssue = new Issue(project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments, url);
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
        }

        return issueList;
    }

    private static int getLastIssueID(Connection myConn, int project_id) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
            String SQL_GET_LAST_ISSUE_ID = "SELECT * FROM issues WHERE project_id = ? ORDER BY issue_id DESC LIMIT 1";
            pstmt = myConn.prepareStatement(SQL_GET_LAST_ISSUE_ID);
            pstmt.setInt(1, project_id);
            myRs = pstmt.executeQuery();

            //get parameter for creating issue object
            if (myRs.next()) {
                return myRs.getInt("issue_id");
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
        }

        return 0;
    }

    //get particular issue using id
    public static Issue selectIssue(Connection myConn, int project_id, int issue_id) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
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
            String assignee = myRs.getString("assignee");
            Timestamp issue_timestamp = myRs.getTimestamp("issue_timestamp");
            ArrayList<Comment> comments = getCommentList(myConn, project_id, issue_id);
            String url = myRs.getString("url");

            return new Issue(project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp, comments, url);

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

    private static ArrayList<Issue> searchIssueFromTitleAndDescription(Connection myConn, int project_id, String str) {
        String searchStr = getKeyWord(str);
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        ArrayList<Issue> issueList = new ArrayList<>();

        try {
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
                ArrayList<Comment> comments = (ArrayList<Comment>) getCommentList(myConn, project_id, issue_id);
                String url = myRs.getString("url");
                Issue newIssue = new Issue(project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments, url);
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
        }

        return issueList;
    }

    private static ArrayList<Issue> searchIssueFromComment(Connection myConn, int project_id, String str) {
        ArrayList<Issue> issueList = getIssueListByPriority(myConn, project_id);
        ArrayList<Issue> newIssueList = new ArrayList<>();

        for (int i = 0; i < issueList.size(); i++) {

            String searchStr = getKeyWord(str);
            PreparedStatement pstmt = null;
            ResultSet myRs = null;

            try {
                String SQL_GET_COMMENT_LIST = "SELECT * FROM comments WHERE project_id = ? AND issue_id = ? AND text REGEXP ?";
                pstmt = myConn.prepareStatement(SQL_GET_COMMENT_LIST);
                pstmt.setInt(1, project_id);
                pstmt.setInt(2, issueList.get(i).getId());
                pstmt.setString(3, searchStr);
                myRs = pstmt.executeQuery();

                //get parameter for creating issue object
                while (myRs.next()) {
                    int issue_id = myRs.getInt("issue_id");
                    Issue newIssue = selectIssue(myConn, project_id, issue_id);
                    boolean contain = false;
                    for (Issue issue : newIssueList) {
                        if (issue.equal(newIssue)) {
                            contain = true;
                        }
                    }
                    if (contain == false) {
                        newIssueList.add(newIssue);
                    }
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
            }
        }

        return newIssueList;
    }

    public static List<Issue> searchIssue(Connection myConn, int project_id, String str) {
        List<Issue> issueListFromTextAndDescription = searchIssueFromTitleAndDescription(myConn, project_id, str);
        List<Issue> issueListFromComment = searchIssueFromComment(myConn, project_id, str);

        issueListFromTextAndDescription.addAll(issueListFromComment);
        return issueListFromTextAndDescription;
    }

    public static ArrayList<Comment> getCommentList(Connection myConn, int project_id, int issue_id) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        ArrayList<Comment> commentList = new ArrayList<>();

        try {
            String SQL_GET_COMMENT_LIST = "SELECT * FROM comments WHERE project_id = ? AND issue_id = ?";
            pstmt = myConn.prepareStatement(SQL_GET_COMMENT_LIST);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            myRs = pstmt.executeQuery();
            //get parameter for creating issue object
            while (myRs.next()) {
                int comment_id = myRs.getInt("comment_id");
                String text = myRs.getString("text");
                ArrayList<React> react = getReactList(myConn, project_id, issue_id, comment_id);
                Timestamp timestamp = myRs.getTimestamp("comment_timestamp");
                String user = myRs.getString("user");
                Comment newComment = new Comment(comment_id, text, react, timestamp, user);
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
        }

        return commentList;

    }

    private static int getLastUserID(Connection myConn) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
            String SQL_GET_LAST_ISSUE_ID = "SELECT * FROM users ORDER BY userid DESC LIMIT 1";
            pstmt = myConn.prepareStatement(SQL_GET_LAST_ISSUE_ID);
            myRs = pstmt.executeQuery();

            //get parameter for creating issue object
            if (myRs.next()) {
                return myRs.getInt("userid");
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
        }

        return 0;
    }

    private static int getLastCommentID(Connection myConn, int project_id, int issue_id) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
            String SQL_GET_LAST_COMMENT_ID = "SELECT * FROM comments WHERE project_id = ? AND issue_id = ? ORDER BY comment_id DESC LIMIT 1";
            pstmt = myConn.prepareStatement(SQL_GET_LAST_COMMENT_ID);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            myRs = pstmt.executeQuery();

            //get parameter for creating issue object
            if (myRs.next()) {
                return myRs.getInt("comment_id");
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
        }

        return 0;
    }

    public static ArrayList<React> getReactList(Connection myConn, int project_id, int issue_id, int comment_id) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        ArrayList<React> reactList = new ArrayList<>();

        try {
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
        }
        return reactList;
    }

    public static int showProjectDashboard(Connection myConn) {
        boolean inputIsInt = false;
        Scanner sc = new Scanner(System.in);
        List<Project> projectList = getProjectList(myConn);
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

    public static void showIssueDashboard(Connection myConn, int project_id, String sortedBy, String username) {
        boolean correctInput = false;
        Scanner sc = new Scanner(System.in);
        List<Issue> issueList = new ArrayList<>();

        //get different order of issue list order from timestamp or priority
        if (sortedBy.equalsIgnoreCase("Timestamp")) {
            issueList = getIssueListByTimestamp(myConn, project_id);
        } else if (sortedBy.equalsIgnoreCase("Priority")) {
            issueList = getIssueListByPriority(myConn, project_id);
        }

        displayIssue(issueList);

        while (!correctInput) {
            System.out.print("\nEnter selected issue ID to check issue\n" +
                    "or 's' to search\n" +
                    "or 'c' to create issue");
            String input = sc.nextLine();

            if (isInt(input)) {
                correctInput = true;
                selectIssue(myConn, project_id, Integer.parseInt(input));
            } else if (input.equals("s")) {
                correctInput = true;
                System.out.print("Enter keyword you would like to search: ");
                String str = sc.nextLine();
                searchIssue(myConn, project_id, str);
            } else if (input.equals("s")) {
                correctInput = true;
                createIssue(myConn, project_id, username);
            }
        }
    }

    public static String displayIssue(List<Issue> issueList) {
//        System.out.printf("\n%s\n%-3s %-40s %-20s %-15s %-15s %-22s %-20s %-20s\n", "Issue board", "ID", "Title", "Status", "Tag", "Priority", "Time", "Assignee", "createdBy");
//
//        for (int i = 0; i < issueList.size(); i++) {
//            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(issueList.get(i).getTimestamp());
//            System.out.printf("%-3d %-40s %-20s %-15s %-15d %-22s %-20s %-20s\n", issueList.get(i).getId(), issueList.get(i).getTitle(), issueList.get(i).getStatus(), issueList.get(i).getTag()[0], issueList.get(i).getPriority(), timeStamp, issueList.get(i).getAssignee(), issueList.get(i).getCreatedBy());
//        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s\n%-3s %-40s %-20s %-15s %-15s %-22s %-20s %-20s\n", "Issue board", "ID", "Title", "Status", "Tag", "Priority", "Time", "Assignee", "createdBy"));

        for (int i = 0; i < issueList.size(); i++) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(issueList.get(i).getTimestamp());
            sb.append(String.format("%-3d %-40s %-20s %-15s %-15d %-22s %-20s %-20s\n", issueList.get(i).getId(), issueList.get(i).getTitle(), issueList.get(i).getStatus(), issueList.get(i).getTag()[0], issueList.get(i).getPriority(), timeStamp, issueList.get(i).getAssignee(), issueList.get(i).getCreatedBy()));
        }
        return sb.toString();
    }

    public static User login(Connection myConn, String username, String password) {
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
                String url = myRs.getString("url");
                String email = myRs.getString("email");
                User newUser = new User(userid, username, password, admin, url, email);
                return newUser;
            }

            myConn.commit();
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
        }

        return null;
    }

    public static void registerUser(Connection myConn, String username, String password, boolean isAdmin) {
        //Scanner sc = new Scanner(System.in);
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        int user_id = getLastUserID(myConn) + 1;

        try {
            String SQL_CREATE_USER = "INSERT INTO users(userid, username, password, admin)VALUES (?, ?, ?, ?)";
            pstmt = myConn.prepareStatement(SQL_CREATE_USER);
            pstmt.setInt(1, user_id);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setBoolean(4, isAdmin);
            pstmt.execute();
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
        }
    }

    public static void updatePassword(Connection myConn, User user, String newPassword) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
            String SQL_UPDATE_PROJECTS = "UPDATE users SET password = ? WHERE username = ? AND userid=?";

            //update table users
            pstmt = myConn.prepareStatement(SQL_UPDATE_PROJECTS);
            pstmt.setString(1, newPassword);

            pstmt.setString(2, user.getUsername());
            pstmt.setInt(3, user.getUserid());
            pstmt.execute();
            System.out.println(newPassword + " " + user.getUsername() + " " + user.getUserid());

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
        }
    }

    public static void updateUserUrl(Connection myConn, User user, String newUrl) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
            String SQL_UPDATE_PROJECTS = "UPDATE users SET url = ? WHERE username = ? AND userid=?";

            //update table users
            pstmt = myConn.prepareStatement(SQL_UPDATE_PROJECTS);
            pstmt.setString(1, newUrl);

            pstmt.setString(2, user.getUsername());
            pstmt.setInt(3, user.getUserid());
            pstmt.execute();
            System.out.println(newUrl + " " + user.getUsername() + " " + user.getUserid());

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
        }
    }

    public static User showLoginPage(Connection myConn) {
        Scanner sc = new Scanner(System.in);
        User newUser = null;

        while (newUser == null) {
            //reques user input
            System.out.print("Enter user name: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();

            //assign new user object if password and username exist
            newUser = login(myConn, username, password);

            //notification if wrong password / username
            if (newUser == null) {
                System.out.println("WARNING: Wrong user name or password, please try again.\n");
            }
        }

        return newUser;
    }

    //related to tag
    public static void createIssue(Connection myConn, int project_id, String username) {
        Scanner sc = new Scanner(System.in);
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        int issue_id = getLastIssueID(myConn, project_id) + 1;
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
        }
    }

    public static void createIssueJavaFX(Connection myConn, int project_id, String username, String tag1, int priority, String title, String assignee, String descriptionText) {
        //Scanner sc = new Scanner(System.in);
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        int issue_id = getLastIssueID(myConn, project_id) + 1;

        String status = "In Progress";
        String createdBy = username;
        Timestamp issue_timestamp;
        issue_timestamp = new java.sql.Timestamp(new Date().getTime());
        List<Comment> commentList = new ArrayList<>();
        Issue newIssue = null;

        try {
            String SQL_CREATE_ISSUE = "INSERT INTO issues(project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = myConn.prepareStatement(SQL_CREATE_ISSUE);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            pstmt.setString(3, title);
            pstmt.setInt(4, priority);
            pstmt.setString(5, status);
            pstmt.setString(6, tag1);
            pstmt.setString(7, descriptionText);
            pstmt.setString(8, createdBy);
            pstmt.setString(9, assignee);
            pstmt.setTimestamp(10, issue_timestamp);

            pstmt.execute();
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
        }
    }

    public static void createComment(Connection myConn, int project_id, int issue_id, String username, String text) {
        Scanner sc = new Scanner(System.in);
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        int comment_id = getLastCommentID(myConn, project_id, issue_id) + 1;

        Timestamp comment_timestamp;
        comment_timestamp = new java.sql.Timestamp(new Date().getTime());

        try {
            myConn = getConnection();

            //create new comment row
            String SQL_CREATE_ISSUE = "INSERT INTO comments(project_id, issue_id, comment_id, text, comment_timestamp, user) VALUES (?,?,?,?,?,?)";
            pstmt = myConn.prepareStatement(SQL_CREATE_ISSUE);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            pstmt.setInt(3, comment_id);
            pstmt.setString(4, text);
            pstmt.setTimestamp(5, comment_timestamp);
            pstmt.setString(6, username);
            pstmt.execute();

            //create new react angry row for the comment
            String SQL_CREATE_REACT_ANGRY = "INSERT INTO react(project_id, issue_id, comment_id, reaction, count) VALUES (?, ?, ?, ?, ?)";
            pstmt = myConn.prepareStatement(SQL_CREATE_REACT_ANGRY);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            pstmt.setInt(3, comment_id);
            pstmt.setString(4, "angry");
            pstmt.setInt(5, 0);
            pstmt.execute();

            //create new react happy row for the comment
            String SQL_CREATE_REACT_HAPPY = "INSERT INTO react(project_id, issue_id, comment_id, reaction, count) VALUES (?, ?, ?, ?, ?)";
            pstmt = myConn.prepareStatement(SQL_CREATE_REACT_HAPPY);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            pstmt.setInt(3, comment_id);
            pstmt.setString(4, "happy");
            pstmt.setInt(5, 0);
            pstmt.execute();

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
        }
    }

    public static void showUserInterface(Connection myConn) {
        User currentUser = showLoginPage(myConn);
        int selectionOfProject = showProjectDashboard(myConn);
        showIssueDashboard(myConn, selectionOfProject, "priority", currentUser.getUsername());
    }

    public static void updateProject(Connection myConn, int project_id, String newName) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        Project requiredProject = getProject(myConn, project_id);

        try {
            String SQL_UPDATE_PROJECTS_HISTORY = "INSERT INTO projects_history(project_id, version_id, name, originalTime) VALUES (? , ?,  ?, ?)";
            String SQL_UPDATE_PROJECTS = "UPDATE projects SET name = ?, project_timestamp = ? WHERE project_id = ?";
            String SQL_GET_LAST_VERSION_ID = "SELECT * FROM projects_history WHERE project_id = ? ORDER BY version_id DESC";

            //get last version id
            int version_id = 1;
            pstmt = myConn.prepareStatement(SQL_GET_LAST_VERSION_ID);
            pstmt.setInt(1, project_id);
            myRs = pstmt.executeQuery();

            if (myRs.next()) {
                version_id = myRs.getInt("version_id") + 1;
            }

            //update table project history
            pstmt = myConn.prepareStatement(SQL_UPDATE_PROJECTS_HISTORY);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, version_id);
            pstmt.setString(3, requiredProject.getName());
            pstmt.setTimestamp(4, requiredProject.getProject_timestamp());
            pstmt.execute();

            //update table projects
            pstmt = myConn.prepareStatement(SQL_UPDATE_PROJECTS);
            pstmt.setString(1, newName);

            Timestamp currentTimestamp = new Timestamp(new Date().getTime());
            pstmt.setTimestamp(2, currentTimestamp);

            pstmt.setInt(3, project_id);
            pstmt.execute();

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
        }
    }

    public static void updateIssue(Connection myConn, int project_id, int issue_id, String columnName, String newValue) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
            String SQL_UPDATE_ISSUES_HISTORY = "INSERT INTO issues_history(project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp) " +
                    "SELECT project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp FROM issues " +
                    "WHERE project_id = ? AND issue_id = ?";
            String SQL_UPDATE_ISSUES = "UPDATE issues SET " + columnName + " = ?, issue_timestamp = ? WHERE project_id = ? AND issue_id = ?";
            String SQL_GET_LAST_VERSION_ID = "SELECT * FROM issues_history WHERE project_id = ? AND issue_id = ? ORDER BY version_id DESC";

            //get last version id
            int version_id = 1;
            pstmt = myConn.prepareStatement(SQL_GET_LAST_VERSION_ID);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            myRs = pstmt.executeQuery();

            if (myRs.next()) {
                version_id = myRs.getInt("version_id") + 1;
            }

            //update table issues history
            pstmt = myConn.prepareStatement(SQL_UPDATE_ISSUES_HISTORY);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            pstmt.execute();

            //update table issues history version id
            pstmt = myConn.prepareStatement("UPDATE issues_history SET version_id = ? where project_id = ? AND issue_id = ?");
            pstmt.setInt(1, version_id);
            pstmt.setInt(2, project_id);
            pstmt.setInt(3, issue_id);
            pstmt.execute();

            //update table issues
            pstmt = myConn.prepareStatement(SQL_UPDATE_ISSUES);
            pstmt.setString(1, newValue);

            Timestamp currentTimestamp = new Timestamp(new Date().getTime());
            pstmt.setTimestamp(2, currentTimestamp);

            pstmt.setInt(3, project_id);
            pstmt.setInt(4, issue_id);
            pstmt.execute();

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
        }
    }

    public static void updateIssue(Connection myConn, int project_id, int issue_id, String title, int priority, String status, String tag, String descriptionText) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
            String SQL_UPDATE_ISSUES_HISTORY = "INSERT INTO issues_history(project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp) " +
                    "SELECT project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp FROM issues " +
                    "WHERE project_id = ? AND issue_id = ?";
            String SQL_UPDATE_ISSUES = "UPDATE issues SET title = ?, priority = ?, status = ?, tag = ?, descriptionText = ?, issue_timestamp = ? WHERE project_id = ? AND issue_id = ?";
            String SQL_GET_LAST_VERSION_ID = "SELECT * FROM issues_history WHERE project_id = ? AND issue_id = ? ORDER BY version_id DESC";

            //get last version id
            int version_id = 1;
            pstmt = myConn.prepareStatement(SQL_GET_LAST_VERSION_ID);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            myRs = pstmt.executeQuery();

            if (myRs.next()) {
                version_id = myRs.getInt("version_id") + 1;
            }

            //update table issues history
            pstmt = myConn.prepareStatement(SQL_UPDATE_ISSUES_HISTORY);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            pstmt.execute();

            //update table issues history version id
            pstmt = myConn.prepareStatement("UPDATE issues_history SET version_id = ? where project_id = ? AND issue_id = ?");
            pstmt.setInt(1, version_id);
            pstmt.setInt(2, project_id);
            pstmt.setInt(3, issue_id);

            //update table issues
            pstmt = myConn.prepareStatement(SQL_UPDATE_ISSUES);
            pstmt.setString(1, title);
            pstmt.setInt(2, priority);
            pstmt.setString(3, status);
            pstmt.setString(4, tag);
            pstmt.setString(5, descriptionText);

            Timestamp currentTimestamp = new Timestamp(new Date().getTime());
            pstmt.setTimestamp(6, currentTimestamp);

            pstmt.setInt(7, project_id);
            pstmt.setInt(8, issue_id);
            pstmt.execute();

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
        }
    }

    public static void updateComment(Connection myConn, User user, int project_id, int issue_id, int comment_id, String newText) {
        boolean isOwner = false;

        List<Comment> comments = getCommentList(myConn, project_id, issue_id);
        Comment requiredComment = null;
        for (Comment c : comments) {
            if (c.getComment_id() == comment_id) {
                requiredComment = c;
            }
        }

        if (requiredComment.getUser().equals(user.getUsername())) {
            isOwner = true;
        }

        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        if (isOwner) {
            try {
                String SQL_UPDATE_COMMENTS_HISTORY = "INSERT INTO comments_history(project_id, issue_id, comment_id, version_id, text, comment_timestamp, user) VALUES (?, ?, ?, ?, ?, ?, ?)";
                String SQL_UPDATE_COMMNETS = "UPDATE comments SET text = ?, comment_timestamp = ? WHERE project_id = ? AND issue_id = ? AND comment_id = ?";
                String SQL_GET_LAST_VERSION_ID = "SELECT * FROM comments_history WHERE project_id = ? AND issue_id = ? AND comment_id = ? ORDER BY version_id DESC";

                //get last version id
                int version_id = 1;
                pstmt = myConn.prepareStatement(SQL_GET_LAST_VERSION_ID);
                pstmt.setInt(1, project_id);
                pstmt.setInt(2, issue_id);
                pstmt.setInt(3, comment_id);
                myRs = pstmt.executeQuery();

                if (myRs.next()) {
                    version_id = myRs.getInt("version_id") + 1;
                }

                //update table project history
                pstmt = myConn.prepareStatement(SQL_UPDATE_COMMENTS_HISTORY);
                pstmt.setInt(1, project_id);
                pstmt.setInt(2, issue_id);
                pstmt.setInt(3, requiredComment.getComment_id());
                pstmt.setInt(4, version_id);
                pstmt.setString(5, requiredComment.getText());
                pstmt.setTimestamp(6, requiredComment.getTimestamp());
                pstmt.setString(7, requiredComment.getUser());
                pstmt.execute();

                //update table projects
                pstmt = myConn.prepareStatement(SQL_UPDATE_COMMNETS);
                pstmt.setString(1, newText);

                Timestamp currentTimestamp = new Timestamp(new Date().getTime());
                pstmt.setTimestamp(2, currentTimestamp);

                pstmt.setInt(3, project_id);
                pstmt.setInt(4, issue_id);
                pstmt.setInt(5, comment_id);
                pstmt.execute();

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
            }
        } else {
            System.out.println("You are not the owner of this comments");
        }
    }

    public static List<Project_History> getProjectHistoryList(Connection myConn, int project_id) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        List<Project_History> projectList = new ArrayList<>();

        try {
            String SQL_GET_PROJECT_LIST = "SELECT * FROM projects_history WHERE project_id = ? ORDER BY originalTime DESC";
            pstmt = myConn.prepareStatement(SQL_GET_PROJECT_LIST);
            pstmt.setInt(1, project_id);
            //stmt = myConn.createStatement();
            myRs = pstmt.executeQuery();
            //get parameter for creating issue object
            while (myRs.next()) {
                int id = myRs.getInt("project_id");
                int version_id = myRs.getInt("version_id");
                String name = myRs.getString("name");
                Timestamp originalTime = myRs.getTimestamp("originalTime");
                projectList.add(new Project_History(id, version_id, name, originalTime));
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
        }
        return projectList;
    }

    public static ArrayList<Issue_History> getIssueHistoryList(Connection myConn, int project_id, int issue_id) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        ArrayList<Issue_History> issueList = new ArrayList<>();

        try {
            String SQL_GET_ISSUE_HISTORY_LIST = "SELECT * FROM issues_history WHERE project_id = ? AND issue_id = ? ORDER BY issue_timestamp DESC";
            pstmt = myConn.prepareStatement(SQL_GET_ISSUE_HISTORY_LIST);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            myRs = pstmt.executeQuery();
            //get parameter for creating issue object
            while (myRs.next()) {
                int version_id = myRs.getInt("version_id");
                String title = myRs.getString("title");
                int priority = myRs.getInt("priority");
                String status = myRs.getString("status");
                String[] tag = {myRs.getString("tag")};
                String descriptionText = myRs.getString("descriptionText");
                String createdBy = myRs.getString("createdBy");
                String asignee = myRs.getString("assignee");
                Timestamp issue_timestamp = myRs.getTimestamp("issue_timestamp");
                Issue_History newIssueHistory = new Issue_History(project_id, issue_id, version_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp);
                issueList.add(newIssueHistory);
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
        }

        return issueList;
    }

    public static ArrayList<Comment_History> getCommentHistoryList(Connection myConn, int project_id, int issue_id, int comment_id) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        ArrayList<Comment_History> commentList = new ArrayList<>();

        try {
            String SQL_GET_COMMENT_HISTORY_LIST = "SELECT * FROM comments_history WHERE project_id = ? AND issue_id = ? AND comment_id = ? ORDER BY comment_timestamp DESC";
            pstmt = myConn.prepareStatement(SQL_GET_COMMENT_HISTORY_LIST);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            pstmt.setInt(3, comment_id);
            myRs = pstmt.executeQuery();
            //get parameter for creating issue object
            while (myRs.next()) {
                int version_id = myRs.getInt("version_id");
                String text = myRs.getString("text");
                Timestamp timestamp = myRs.getTimestamp("comment_timestamp");
                String user = myRs.getString("user");
                Comment_History newCommentHistory = new Comment_History(comment_id, version_id, text, timestamp, user);
                commentList.add(newCommentHistory);
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
        }

        return commentList;

    }

    public static Connection connectionToDatabase() {
        Connection myConnection = null;
        try {
            myConnection = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myConnection;
    }

    public static void resetDatabase(Connection myConn, String database_name) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        String query = "SHOW DATABASES;\n" +
                "USE " + database_name + ";\n" +
                "\n" +
                "CREATE TABLE projects (\n" +
                "project_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "name VARCHAR(20) NOT NULL,\n" +
                "project_timestamp TIMESTAMP NOT NULL);\n" +
                "\n" +
                "CREATE TABLE issues (\n" +
                "project_id INT NOT NULL,\n" +
                "issue_id INT NOT NULL,\n" +
                "PRIMARY KEY (project_id, issue_id),\n" +
                "title VARCHAR(50),\n" +
                "priority INT,\n" +
                "status VARCHAR(20),\n" +
                "tag VARCHAR(20),\n" +
                "descriptionText VARCHAR(500),\n" +
                "createdBy VARCHAR(20),\n" +
                "assignee VARCHAR(20),\n" +
                "issue_timestamp TIMESTAMP,\n" +
                "url VARCHAR(2083));\n" +
                "\n" +
                "CREATE TABLE comments (\n" +
                "project_id INT NOT NULL,\n" +
                "issue_id INT NOT NULL,\n" +
                "comment_id INT NOT NULL,\n" +
                "PRIMARY KEY (project_id, issue_id, comment_id),\n" +
                "text VARCHAR(250),\n" +
                "comment_timestamp TIMESTAMP,\n" +
                "user VARCHAR(25));\n" +
                "\n" +
                "CREATE TABLE react (\n" +
                "project_id INT NOT NULL,\n" +
                "issue_id INT NOT NULL,\n" +
                "comment_id INT NOT NULL,\n" +
                "reaction VARCHAR(10),\n" +
                "PRIMARY KEY (project_id, issue_id, comment_id, reaction),\n" +
                "count INT);\n" +
                "\n" +
                "CREATE TABLE users (\n" +
                "userid INT,\n" +
                "username VARCHAR(25),\n" +
                "password VARCHAR(64),\n" +
                "admin boolean,\n" +
                "url VARCHAR(2083),\n" +
                "email VARCHAR(40)\n" +
                ");\n" +
                "\n" +
                "ALTER TABLE users ADD UNIQUE(userid);\n" +
                "ALTER TABLE users ADD UNIQUE(username);\n" +
                "\n" +
                "CREATE TABLE projects_history (\n" +
                "project_id INT NOT NULL,\n" +
                "version_id INT NOT NULL,\n" +
                "name VARCHAR(20) NOT NULL,\n" +
                "originalTime TIMESTAMP NOT NULL,\n" +
                "PRIMARY KEY (project_id, originalTime),\n" +
                "CONSTRAINT project_id_fk\n" +
                "    FOREIGN KEY profile_id_fkx (project_id)\n" +
                "    REFERENCES projects(project_id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE issues_history (\n" +
                "project_id INT NOT NULL,\n" +
                "issue_id INT NOT NULL,\n" +
                "version_id INT,\n" +
                "title VARCHAR(50),\n" +
                "priority INT,\n" +
                "status VARCHAR(20),\n" +
                "tag VARCHAR(20),\n" +
                "descriptionText VARCHAR(500),\n" +
                "createdBy VARCHAR(20),\n" +
                "assignee VARCHAR(20),\n" +
                "issue_timestamp TIMESTAMP,\n" +
                "PRIMARY KEY (project_id, issue_id, issue_timestamp),\n" +
                "CONSTRAINT pi_fk\n" +
                "    FOREIGN KEY pi_fk (project_id, issue_id)\n" +
                "    REFERENCES issues (project_id, issue_id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE comments_history (\n" +
                "project_id INT NOT NULL,\n" +
                "issue_id INT NOT NULL,\n" +
                "comment_id INT NOT NULL,\n" +
                "version_id INT NOT NULL,\n" +
                "text VARCHAR(250),\n" +
                "comment_timestamp TIMESTAMP,\n" +
                "user VARCHAR(25),\n" +
                "PRIMARY KEY (project_id, issue_id, comment_id, comment_timestamp),\n" +
                "CONSTRAINT pic_fk\n" +
                "    FOREIGN KEY pic_fkx (project_id, issue_id, comment_id)\n" +
                "    REFERENCES comments (project_id, issue_id, comment_id)\n" +
                ");";

        try {
            pstmt = myConn.prepareStatement(query);

            System.out.println(database_name);
            myRs = pstmt.executeQuery();
            initializedDatabase();
        } catch (Exception ex) {
            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("SQL Error!");
            alert.showAndWait();
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
        }
    }

    public static void main(String[] args) {
        initializedDatabase();

//        Connection myConn = null;
//        try {
//            myConn = getConnection();
//            List<Issue> i = getIssueListByTimestamp(myConn,1);
//            System.out.println(i.get(0).getTimestamp());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (myConn != null) {
//                try {
//                    myConn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        showIssueDashboard(1,"priority","jhoe");
    }
}