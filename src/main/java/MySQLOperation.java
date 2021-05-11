
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
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

    public static Connection getConnection() throws Exception {
        final String user = "sql6411496";
        final String pass = "eIXUjnTvTP";
        final String path = "jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6411496?zeroDateTimeBehavior=CONVERT_TO_NULL";
        final String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        Connection myConn = DriverManager.getConnection(path, user, pass);
        return myConn;
    }

    public static <E> E readJsonUrlToPOJO(String url, Class<E> clazz) throws MalformedURLException, JsonProcessingException, IOException {
        URL jsonUrl = new URL(url);
        JsonNode node = Json.parse(jsonUrl);
        return Json.fromJson(node, clazz);
    }

    public static void updateDatabaseFromUrl(Connection myConn, String url) throws SQLException, MalformedURLException, IOException {
        Database db = readJsonUrlToPOJO(url, Database.class);

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
        for (int i = 0; i < db.getProjects().size(); i++) {
            updateProject.setInt(1, db.getProjects().get(i).getId());
            updateProject.setString(2, db.getProjects().get(i).getName());
            updateProject.addBatch();

            //add issues
            for (int j = 0; j < db.getProjects().get(i).getIssues().size(); j++) {
                updateIssue.setInt(1, db.getProjects().get(i).getId());
                updateIssue.setInt(2, db.getProjects().get(i).getIssues().get(j).getId());
                updateIssue.setString(3, db.getProjects().get(i).getIssues().get(j).getTitle());
                updateIssue.setInt(4, db.getProjects().get(i).getIssues().get(j).getPriority());
                updateIssue.setString(5, db.getProjects().get(i).getIssues().get(j).getStatus());
                updateIssue.setString(6, Arrays.toString(db.getProjects().get(i).getIssues().get(j).getTag()));
                updateIssue.setString(7, db.getProjects().get(i).getIssues().get(j).getDescriptionText());
                updateIssue.setString(8, db.getProjects().get(i).getIssues().get(j).getCreatedBy());
                updateIssue.setString(9, db.getProjects().get(i).getIssues().get(j).getAssignee());
                updateIssue.setTimestamp(10, db.getProjects().get(i).getIssues().get(j).getTimestamp());
                updateIssue.addBatch();

                //add comments
                for (int k = 0; k < db.getProjects().get(i).getIssues().get(j).getComments().size(); k++) {
                    updateComment.setInt(1, db.getProjects().get(i).getId());
                    updateComment.setInt(2, db.getProjects().get(i).getIssues().get(j).getId());
                    updateComment.setInt(3, db.getProjects().get(i).getIssues().get(j).getComments().get(k).getComment_id());
                    updateComment.setString(4, db.getProjects().get(i).getIssues().get(j).getComments().get(k).getText());
                    updateComment.setTimestamp(5, db.getProjects().get(i).getIssues().get(j).getComments().get(k).getTimestamp());
                    updateComment.setString(6, db.getProjects().get(i).getIssues().get(j).getComments().get(k).getUser());
                    updateComment.addBatch();

                    //add react
                    for (int l = 0; l < db.getProjects().get(i).getIssues().get(j).getComments().get(k).getReact().size(); l++) {
                        updateReact.setInt(1, db.getProjects().get(i).getId());
                        updateReact.setInt(2, db.getProjects().get(i).getIssues().get(j).getId());
                        updateReact.setInt(3, db.getProjects().get(i).getIssues().get(j).getComments().get(k).getComment_id());
                        updateReact.setString(4, db.getProjects().get(i).getIssues().get(j).getComments().get(k).getReact().get(l).getReaction());
                        updateReact.setInt(5, db.getProjects().get(i).getIssues().get(j).getComments().get(k).getReact().get(l).getCount());
                        updateReact.addBatch();

                        if (i == db.getProjects().size() - 1) {
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
        for (int i = 0; i < db.getUser().size(); i++) {
            updateUser.setInt(1, db.getUser().get(i).getUserid());
            updateUser.setString(2, db.getUser().get(i).getUsername());
            updateUser.setString(3, db.getUser().get(i).getPassword());
            updateUser.addBatch();
            
            if (i == db.getUser().size() - 1) {
                updateUser.executeBatch();
            }
        }
    }
    
    public static void initializedDatabase() {
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
    
    public static void main(String[] args) {
                    initializedDatabase();

    }
}
