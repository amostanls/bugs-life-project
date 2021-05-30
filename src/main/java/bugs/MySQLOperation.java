package bugs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
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
tag VARCHAR(100),
descriptionText VARCHAR(19000),
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
tag VARCHAR(100),
descriptionText VARCHAR(19000),
createdBy VARCHAR(20),
assignee VARCHAR(20),
issue_timestamp TIMESTAMP,
url VARCHAR(2083),
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

    //Return absolute path of exported file as string
    public static String exportJavaObjectsAsJson(Connection myConn, List objects, String fileName) {
        fileName += ".json";
        ObjectMapper om = Json.getDefaultOM();
        try {
            om.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), objects);
        } catch (IOException ex) {
            Logger.getLogger(Json.class.getName()).log(Level.SEVERE, null, ex);
        }
        Path pathOfResultFile = Paths.get(fileName);
        return pathOfResultFile.toAbsolutePath().toString();
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

    public static void importJsonFileToDataBase(Connection myConn, String filePath) throws IOException, SQLException {
        File jsonFile = new File(filePath);
        JsonNode node = Json.parseFile(jsonFile);

        String INSERT_PROJECT = "INSERT INTO projects (project_id, name, project_timestamp) VALUE (?,?,?)";
        String INSERT_ISSUE = "INSERT INTO issues (project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp, url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; //10
        String INSERT_COMMENT = "INSERT INTO comments (project_id, issue_id, comment_id, text, comment_timestamp, user) VALUES (?, ?, ?, ?, ?, ?)";  //6
        String INSERT_REACT = "INSERT INTO react (project_id, issue_id, comment_id, reaction, count) VALUES (?, ?, ?, ?, ?)"; //2
        String INSERT_USER = "INSERT INTO users (userid, username, password, admin, url, email) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement updateProject = myConn.prepareStatement(INSERT_PROJECT, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement updateIssue = myConn.prepareStatement(INSERT_ISSUE, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement updateComment = myConn.prepareStatement(INSERT_COMMENT, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement updateReact = myConn.prepareStatement(INSERT_REACT, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement updateUser = myConn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);

        //add projects
        for (int i = 0; i < node.get("projects").size(); i++) {
            updateProject.setInt(1, node.get("projects").get(i).get("project_id").asInt());
            updateProject.setString(2, node.get("projects").get(i).get("name").asText());

            Timestamp newTimestamp = convertStringTimestampForImport(node.get("projects").get(i).get("project_timestamp").asText());
            updateProject.setTimestamp(3, newTimestamp);
            updateProject.addBatch();

            //add issues
            for (int j = 0; j < node.get("projects").get(i).get("issues").size(); j++) {
                updateIssue.setInt(1, node.get("projects").get(i).get("project_id").asInt());
                updateIssue.setInt(2, node.get("projects").get(i).get("issues").get(j).get("issue_id").asInt());
                updateIssue.setString(3, node.get("projects").get(i).get("issues").get(j).get("title").asText());
                updateIssue.setInt(4, node.get("projects").get(i).get("issues").get(j).get("priority").asInt());
                updateIssue.setString(5, node.get("projects").get(i).get("issues").get(j).get("status").asText());
                updateIssue.setString(6, node.get("projects").get(i).get("issues").get(j).withArray("tag").get(0).asText());
                updateIssue.setString(7, node.get("projects").get(i).get("issues").get(j).get("descriptionText").asText());
                updateIssue.setString(8, node.get("projects").get(i).get("issues").get(j).get("createdBy").asText());
                updateIssue.setString(9, node.get("projects").get(i).get("issues").get(j).get("assignee").asText());

                //convert string timestamp to timestamp
                Timestamp newTs = convertStringTimestampForImport(node.get("projects").get(i).get("issues").get(j).get("timestamp").asText());
                updateIssue.setTimestamp(10, newTs);

                updateIssue.setString(11, node.get("projects").get(i).get("issues").get(j).get("url").asText());
                updateIssue.addBatch();

                //add comments
                for (int k = 0; k < node.get("projects").get(i).get("issues").get(j).get("comments").size(); k++) {
                    updateComment.setInt(1, node.get("projects").get(i).get("project_id").asInt());
                    updateComment.setInt(2, node.get("projects").get(i).get("issues").get(j).get("issue_id").asInt());
                    updateComment.setInt(3, node.get("projects").get(i).get("issues").get(j).get("comments").get(k).get("comment_id").asInt());
                    updateComment.setString(4, node.get("projects").get(i).get("issues").get(j).get("comments").get(k).get("text").asText());

                    newTs = convertStringTimestampForImport(node.get("projects").get(i).get("issues").get(j).get("timestamp").asText());
                    updateComment.setTimestamp(5, newTs);
                    updateComment.setString(6, node.get("projects").get(i).get("issues").get(j).get("comments").get(k).get("user").asText());
                    updateComment.addBatch();

                    //add react
                    for (int l = 0; l < node.get("projects").get(i).get("issues").get(j).get("comments").get(k).get("react").size(); l++) {
                        updateReact.setInt(1, node.get("projects").get(i).get("project_id").asInt());
                        updateReact.setInt(2, node.get("projects").get(i).get("issues").get(j).get("issue_id").asInt());
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
            updateUser.setBoolean(4, node.get("users").get(i).get("admin").asBoolean());
            updateUser.setString(5, node.get("users").get(i).get("url").asText());
            updateUser.setString(6,node.get("users").get(i).get("email").asText());
            updateUser.addBatch();

            if (i == node.get("users").size() - 1) {
                updateUser.executeBatch();
            }
        }

        //Add history table
        String INSERT_PROJECT_HISTORY = "INSERT INTO projects_history (project_id, version_id, name, originalTime) VALUE (?,?,?,?)";
        String INSERT_ISSUE_HISTORY = "INSERT INTO issues_history (project_id, issue_id, version_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp, url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; //10
        String INSERT_COMMENT_HISTORY = "INSERT INTO comments (project_id, issue_id, comment_id, version_id, text, comment_timestamp, user) VALUES (?, ?, ?, ?, ?, ?, ?)";  //6

        PreparedStatement updateProjectHistory = myConn.prepareStatement(INSERT_PROJECT_HISTORY, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement updateIssueHistory = myConn.prepareStatement(INSERT_ISSUE_HISTORY, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement updateCommentHistory = myConn.prepareStatement(INSERT_COMMENT_HISTORY, Statement.RETURN_GENERATED_KEYS);

        for (int i = 0; i < node.get("histories").get("project_histories").size(); i++) {
            updateProjectHistory.setInt(1, node.get("projects_histories").get(i).get("project_id").asInt());
            updateProjectHistory.setInt(2, node.get("projects_histories").get(i).get("version_id").asInt());
            updateProjectHistory.setString(3, node.get("projects_histories").get(i).get("name").asText());

            Timestamp newTimestamp = convertStringTimestampForImport(node.get("projects_histories").get(i).get("originalTime").asText());
            updateProjectHistory.setTimestamp(3, newTimestamp);
            updateProjectHistory.addBatch();
        }
        updateProjectHistory.executeBatch();

        for (int i = 0; i < node.get("histories").get("issue_histories").size(); i++) {
            updateIssueHistory.setInt(1, node.get("issues_history").get(i).get("project_id").asInt());
            updateIssueHistory.setInt(2, node.get("issues_history").get(i).get("issue_id").asInt());
            updateIssueHistory.setInt(3, node.get("issues_history").get(i).get("version_id").asInt());
            updateIssueHistory.setString(4, node.get("issues_history").get(i).get("title").asText());
            updateIssueHistory.setInt(5, node.get("issues_history").get(i).get("priority").asInt());
            updateIssueHistory.setString(6, node.get("issues_history").get(i).get("status").asText());
            updateIssueHistory.setString(7, node.get("issues_history").get(i).get("tag").asText());
            updateIssueHistory.setString(8, node.get("issues_history").get(i).get("descriptionText").asText());
            updateIssueHistory.setString(9, node.get("issues_history").get(i).get("createdBy").asText());
            updateIssueHistory.setString(10, node.get("issues_history").get(i).get("assignee").asText());
            Timestamp newTS = convertStringTimestampForImport(node.get("issues_history").get(i).get("issue_timestamp").asText());
            updateIssueHistory.setTimestamp(11, newTS);
            updateIssueHistory.setString(12, node.get("issues_history").get(i).get("url").asText());
            updateIssueHistory.addBatch();
        }
        updateIssueHistory.executeBatch();

        for (int i = 0; i < node.get("histories").get("comment_histories").size(); i++) {
            updateCommentHistory.setInt(1, node.get("issues_history").get(i).get("project_id").asInt());
            updateCommentHistory.setInt(2, node.get("issues_history").get(i).get("issue_id").asInt());
            updateCommentHistory.setInt(3, node.get("issues_history").get(i).get("comment_id").asInt());
            updateCommentHistory.setInt(3, node.get("issues_history").get(i).get("version_id").asInt());
            updateCommentHistory.setString(4, node.get("issues_history").get(i).get("text").asText());
            Timestamp comment_timestamp = convertStringTimestampForImport(node.get("issues_history").get(i).get("comment_timestamp").asText());
            updateCommentHistory.setTimestamp(5, comment_timestamp);
            updateCommentHistory.setString(6, node.get("issues_history").get(i).get("user").asText());
            updateCommentHistory.addBatch();
        }
        updateCommentHistory.executeBatch();
    }

    public static void updateDatabaseFromUrl(Connection myConn, String url) throws SQLException, MalformedURLException, IOException, ParseException {
        URL jsonUrl = new URL(url);
        JsonNode node = Json.parseUrl(jsonUrl);

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

    private static Timestamp convertStringTimestamp(String strTime) {
        String time = strTime + "000";
        SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        Date date = new Date(Long.parseLong(time));
        long longtime = date.getTime();
        Timestamp newTs = new Timestamp(longtime);
        return newTs;
    }

    private static Timestamp convertStringTimestampForImport(String strTime) {
        SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        Date date = new Date(Long.parseLong(strTime));
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

    public static ArrayList<Project> getProjectList(Connection myConn) {
        Statement stmt = null;
        ResultSet myRs = null;
        ArrayList<Project> projectList = new ArrayList<>();

        try {
            String SQL_GET_PROJECT_LIST = "SELECT * FROM projects ORDER BY project_id";
            stmt = myConn.createStatement();
            myRs = stmt.executeQuery(SQL_GET_PROJECT_LIST);
            //get parameter for creating project object
            while (myRs.next()) {
                int id = myRs.getInt("project_id");
                String name = myRs.getString("name");
                ArrayList<Issue> issues = getIssueListByPriority(myConn, id);
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
            String SQL_GET_LAST_ISSUE_ID = "SELECT MAX(userid) FROM users";
            pstmt = myConn.prepareStatement(SQL_GET_LAST_ISSUE_ID);
            myRs = pstmt.executeQuery();

            //get parameter for creating issue object
            if (myRs.next()) {
                return myRs.getInt("MAX(userid)");
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
            String SQL_GET_LAST_COMMENT_ID = "SELECT Max(comment_id) FROM comments WHERE project_id = ? AND issue_id = ?";
            pstmt = myConn.prepareStatement(SQL_GET_LAST_COMMENT_ID);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            myRs = pstmt.executeQuery();

            //get parameter for creating issue object
            if (myRs.next()) {
                return myRs.getInt("MAX(comment_id)");
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

    public static ArrayList<User> getUserList(Connection myConn) {
        Statement stmt = null;
        ResultSet myRs = null;
        ArrayList<User> UserList = new ArrayList<>();

        try {
            String SQL_GET_USER = "SELECT * FROM users";
            stmt = myConn.createStatement();
            myRs = stmt.executeQuery(SQL_GET_USER);

            while (myRs.next()) {
                int userid = myRs.getInt("userid");
                String username = myRs.getString("username");
                String password = myRs.getString("password");
                boolean admin = myRs.getBoolean("admin");
                String url = myRs.getString("url");
                String email = myRs.getString("email");
                UserList.add(new User(userid, username, password, admin, url, email));
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

        return UserList;
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

    public static void createIssueJavaFX(Connection myConn, int project_id, String username, String tag1, int priority, String title, String assignee, String descriptionText,String url) {
        //Scanner sc = new Scanner(System.in);
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        int issue_id = getLastIssueID(myConn, project_id) + 1;

        String status = "Open";
        String createdBy = username;
        Timestamp issue_timestamp;
        issue_timestamp = new java.sql.Timestamp(new Date().getTime());
        List<Comment> commentList = new ArrayList<>();
        Issue newIssue = null;

        try {
            String SQL_CREATE_ISSUE = "INSERT INTO issues(project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp,url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
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
            pstmt.setString(11,url);

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

    public static void updateIssue(Connection myConn, int project_id, int issue_id, String title, int priority, String status, String tag, String descriptionText,String assignee,String url) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;

        try {
            String SQL_UPDATE_ISSUES_HISTORY = "INSERT INTO issues_history(project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp,url, version_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ? ,? ,?, ?, ?)" ;
            String SQL_GET_REQUIRED_ISSUE = "SELECT * FROM issues WHERE project_id = ? AND issue_id = ?";
            String SQL_UPDATE_ISSUES = "UPDATE issues SET title = ?, priority = ?, status = ?, tag = ?, descriptionText = ?, assignee=?, issue_timestamp = ? ,url=? WHERE project_id = ? AND issue_id = ?";
            String SQL_GET_LAST_VERSION_ID = "SELECT * FROM issues_history WHERE project_id = ? AND issue_id = ? ORDER BY version_id DESC LIMIT 1";

            //get last version id
            int version_id = 1;
            pstmt = myConn.prepareStatement(SQL_GET_LAST_VERSION_ID);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            myRs = pstmt.executeQuery();

            if (myRs.next()) {
                version_id = myRs.getInt("version_id") + 1;
            }

            pstmt = myConn.prepareStatement(SQL_GET_REQUIRED_ISSUE);
            pstmt.setInt(1,project_id);
            pstmt.setInt(2,issue_id);
            myRs = pstmt.executeQuery();
            Issue newIssue = null;
            if (myRs.next()) {
                int temp_issue_id = myRs.getInt("issue_id");
                String temp_title = myRs.getString("title");
                int temp_priority = myRs.getInt("priority");
                String temp_status = myRs.getString("status");
                String[] temp_tag = {myRs.getString("tag")};
                String temp_descriptionText = myRs.getString("descriptionText");
                String createdBy = myRs.getString("createdBy");
                String asignee = myRs.getString("assignee");
                Timestamp issue_timestamp = myRs.getTimestamp("issue_timestamp");
                ArrayList<Comment> comments = getCommentList(myConn, project_id, issue_id);
                String temp_url = myRs.getString("url");
                newIssue = new Issue(project_id, temp_issue_id, temp_title, temp_priority, temp_status, temp_tag, temp_descriptionText, createdBy, asignee, issue_timestamp, comments, temp_url);
            }

            //update table issues history
            pstmt = myConn.prepareStatement(SQL_UPDATE_ISSUES_HISTORY);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, issue_id);
            pstmt.setString(3, newIssue.getTitle());
            pstmt.setInt(4, newIssue.getPriority());
            pstmt.setString(5, newIssue.getStatus());
            pstmt.setString(6, newIssue.getTags());
            pstmt.setString(7, newIssue.getDescriptionText());
            pstmt.setString(8, newIssue.getCreatedBy());
            pstmt.setString(9, newIssue.getAssignee());
            pstmt.setTimestamp(10, newIssue.getTimestamp());
            pstmt.setString(11, newIssue.getUrl());
            pstmt.setInt(12, version_id);
            pstmt.execute();

            //update table issues
            pstmt = myConn.prepareStatement(SQL_UPDATE_ISSUES);
            pstmt.setString(1, title);
            pstmt.setInt(2, priority);
            pstmt.setString(3, status);
            pstmt.setString(4, tag);
            pstmt.setString(5, descriptionText);
            pstmt.setString(6,assignee);

            Timestamp currentTimestamp = new Timestamp(new Date().getTime());

            pstmt.setTimestamp(7, currentTimestamp);
            pstmt.setString(8,url);

            pstmt.setInt(9, project_id);
            pstmt.setInt(10, issue_id);
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
                pstmt.setTimestamp(6, requiredComment.getComment_timestamp());
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

    public static ArrayList<Project_History> getProjectHistoryList(Connection myConn, int project_id) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        ArrayList<Project_History> projectList = new ArrayList<>();

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
                String url = myRs.getString("url");
                Issue_History newIssueHistory = new Issue_History(project_id, issue_id, version_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, url);
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

    public static History getHistoryList(Connection myConn) {
        Statement stmt = null;
        ResultSet myRs = null;
        History history = new History();
        ArrayList<Project_History> projectHistories = new ArrayList<>();
        ArrayList<Issue_History> issueHistories = new ArrayList<>();
        ArrayList<Comment_History> commentHistories = new ArrayList<>();

        String SQL_GET_PROJECT_HISTORY = "SELECT * FROM projects_history";
        String SQL_GET_COMMENT_HISTORY = "SELECT * FROM comments_history";
        String SQL_GET_ISSUE_HISTORY = "SELECT * FROM issues_history";


        try {
            stmt = myConn.createStatement();
            //add project history list to history
            myRs = stmt.executeQuery(SQL_GET_PROJECT_HISTORY);
            while (myRs.next()) {
                while (myRs.next()) {
                    int id = myRs.getInt("project_id");
                    int version_id = myRs.getInt("version_id");
                    String name = myRs.getString("name");
                    Timestamp originalTime = myRs.getTimestamp("originalTime");
                    projectHistories.add(new Project_History(id, version_id, name, originalTime));
                }
            }
            history.setProject_histories(projectHistories);

            //add issue history to history
            myRs = stmt.executeQuery(SQL_GET_ISSUE_HISTORY);
            while (myRs.next()) {
                int project_id = myRs.getInt("project_id");
                int issue_id = myRs.getInt("issue_id");
                int version_id = myRs.getInt("version_id");
                String title = myRs.getString("title");
                int priority = myRs.getInt("priority");
                String status = myRs.getString("status");
                String[] tag = {myRs.getString("tag")};
                String descriptionText = myRs.getString("descriptionText");
                String createdBy = myRs.getString("createdBy");
                String assignee = myRs.getString("assignee");
                Timestamp issue_timestamp = myRs.getTimestamp("issue_timestamp");
                String url = myRs.getString("url");
                issueHistories.add(new Issue_History(project_id, issue_id, version_id, title, priority, status, tag, descriptionText, createdBy, assignee, issue_timestamp,url));
            }
            history.setIssue_histories(issueHistories);

            //add comment history to history
            myRs = stmt.executeQuery(SQL_GET_COMMENT_HISTORY);
            while (myRs.next()) {
                int project_id = myRs.getInt("project_id");
                int issue_id = myRs.getInt("issue_id");
                int comment_id = myRs.getInt("comment_id");
                int version_id = myRs.getInt("version_id");
                String text = myRs.getString("text");
                Timestamp timestamp = myRs.getTimestamp("comment_timestamp");
                String user = myRs.getString("user");
                commentHistories.add(new Comment_History(project_id, issue_id, comment_id, version_id, text, timestamp, user));
            }
            history.setComment_histories(commentHistories);

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

        return history;
    }

    public static Database getDatabase(Connection myConn) {
        List<Project> projects = getProjectList(myConn);
        List<User> users = getUserList(myConn);
        History history = getHistoryList(myConn);
        Database db = new Database(projects, users, history);

        return db;
    }

    public static void resetDatabase(Connection myConn, String database_name) {
        PreparedStatement pstmt = null;
        ResultSet myRs = null;
        String query = "SHOW DATABASES;\n" +
                "USE " + database_name + ";\n" +
                "\n" +
                "DROP TABLE projects_history;\n" +
                "DROP TABLE issues_history;\n" +
                "DROP TABLE comments_history;\n" +
                "DROP TABLE projects;\n" +
                "DROP TABLE issues;\n" +
                "DROP TABLE comments;\n" +
                "DROP TABLE react;\n" +
                "DROP TABLE users;\n" +
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
                "tag VARCHAR(100),\n" +
                "descriptionText VARCHAR(19000),\n" +
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
                "tag VARCHAR(100),\n" +
                "descriptionText VARCHAR(19000),\n" +
                "createdBy VARCHAR(20),\n" +
                "assignee VARCHAR(20),\n" +
                "issue_timestamp TIMESTAMP,\n" +
                "url VARCHAR(2083),\n" +
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

//    public static ArrayList<Project> getProjectList(Connection myConn) {
//        PreparedStatement issuePstmt = null;
//        PreparedStatement commentPstmt = null;
//        PreparedStatement reactPstmt = null;
//        Statement stmt = null;
//        ResultSet project_myRs = null;
//        ResultSet issue_myRs = null;
//        ResultSet comment_myRs = null;
//        ResultSet react_myRs = null;
//        ArrayList<Project> projects = new ArrayList<>();
//
//        String SQL_GET_PROJECT_LIST = "SELECT * FROM projects ORDER BY project_id";
//        String SQL_GET_ISSUE_LIST = "SELECT * FROM issues WHERE project_id = ? ORDER BY priority DESC";
//        String SQL_GET_COMMENT_LIST = "SELECT * FROM comments WHERE project_id = ? AND issue_id = ?";
//        String SQL_GET_REACT_LIST = "SELECT * FROM react WHERE project_id = ? AND issue_id = ? AND comment_id = ?";
//
//        try {
//
//            stmt = myConn.createStatement();
//            project_myRs = stmt.executeQuery(SQL_GET_PROJECT_LIST);
//
//            while (project_myRs.next()) {
//                int project_id = project_myRs.getInt("project_id");
//                String name = project_myRs.getString("name");
//
//                //get issue
//                ArrayList<Issue> issues = new ArrayList<>();
//                issuePstmt = myConn.prepareStatement(SQL_GET_ISSUE_LIST);
//                issuePstmt.setInt(1, project_id);
//                issue_myRs = issuePstmt.executeQuery();
//                //get parameter for creating issue object
//                while (issue_myRs.next()) {
//                    int issue_id = issue_myRs.getInt("issue_id");
//                    String title = issue_myRs.getString("title");
//                    int priority = issue_myRs.getInt("priority");
//                    String status = issue_myRs.getString("status");
//                    String[] tag = {issue_myRs.getString("tag")};
//                    String descriptionText = issue_myRs.getString("descriptionText");
//                    String createdBy = issue_myRs.getString("createdBy");
//                    String asignee = issue_myRs.getString("assignee");
//                    Timestamp issue_timestamp = issue_myRs.getTimestamp("issue_timestamp");
//
//                    //get comments
//                    ArrayList<Comment> comments = new ArrayList<>();
//                    commentPstmt = myConn.prepareStatement(SQL_GET_COMMENT_LIST);
//                    commentPstmt.setInt(1, project_id);
//                    commentPstmt.setInt(2, issue_id);
//                    comment_myRs = commentPstmt.executeQuery();
//
//                    //get parameter for creating react object
//                    while (comment_myRs.next()) {
//                        int comment_id = comment_myRs.getInt("comment_id");
//                        String text = comment_myRs.getString("text");
//
//                        //get reacts
//                        ArrayList<React> reacts = new ArrayList<>();
//                        reactPstmt = myConn.prepareStatement(SQL_GET_REACT_LIST);
//                        reactPstmt.setInt(1, project_id);
//                        reactPstmt.setInt(2, issue_id);
//                        reactPstmt.setInt(3, comment_id);
//                        react_myRs = reactPstmt.executeQuery();
//                        //get parameter for creating issue object
//                        while (react_myRs.next()) {
//                            String reaction = react_myRs.getString("reaction");
//                            int count = react_myRs.getInt("count");
//                            React newReact = new React(reaction, count);
//                            reacts.add(newReact);
//                        }
//
//                        Timestamp timestamp = comment_myRs.getTimestamp("comment_timestamp");
//                        String user = comment_myRs.getString("user");
//                        Comment newComment = new Comment(comment_id, text, reacts, timestamp, user);
//                        comments.add(newComment);
//                    }
//
//                    String url = issue_myRs.getString("url");
//                    Issue newIssue = new Issue(project_id, issue_id, title, priority, status, tag, descriptionText, createdBy, asignee, issue_timestamp, comments, url);
//                    issues.add(newIssue);
//                }
//
//                Timestamp ts = project_myRs.getTimestamp("project_timestamp");
//                projects.add(new Project(project_id, name, issues, ts));
//            }
//
//        } catch (Exception ex) {
//            Logger.getLogger(MySQLOperation.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            if (project_myRs != null) {
//                try {
//                    project_myRs.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (stmt != null) {
//                try {
//                    stmt.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return projects;
//    }

    public static void main(String[] args) {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();

        Connection myConn = null;
        try {
            myConn = getConnection();
//            User u = new User(1,"jhoe","asd",true,null,null);
//            System.out.println(getLastUserID(myConn));
            System.out.println(getLastCommentID(myConn,1,1));
//            resetDatabase(myConn, "5peJ8pFLLQ");
//            ArrayList<Project> projects = getProjectList(myConn);
//            System.out.println(projects.get(0).getIssues().get(0).getTitle());
//            List<Project> projectList = getProjectList(myConn);
//            System.out.println(projectList.get(0).getName());
//            initializedDatabase();
//            Database db = getDatabase(myConn);
//            exportJavaObjectAsJson(myConn, db, "db");
//            importJsonFileToDataBase(myConn, "/Users/tanweilok/IdeaProjects/bugs-life-project/db.json");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (myConn != null) {
                try {
                    myConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        stopwatch.stop();
        long timeTaken = stopwatch.getTime();
        System.out.println(timeTaken);
//
//        showIssueDashboard(1,"priority","jhoe");
    }
}