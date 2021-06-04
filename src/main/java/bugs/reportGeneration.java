package bugs;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class reportGeneration {
    private List<Project> projectlist;
    private List<Issue> issue;
    //  private List<Comment> comment;
    private Timestamp startTime;
    private Timestamp endTime;
    private List<Project> projectList;
    private String duration = "Weekly";


    public reportGeneration(Timestamp time, String date) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        duration = date;
        if (date.equals("Weekly")) {
            c.add(Calendar.DATE, 7);
            Timestamp newTime = new Timestamp(c.getTime().getTime());
            setStartTime(time);
            setEndTime(newTime);
        } else {
            c.add(Calendar.MONTH, 1);
            Timestamp newTime = new Timestamp(c.getTime().getTime());
            setStartTime(time);
            setEndTime(newTime);
        }
    }

    public List<Project> getProjectlist() {  //base on the date to get the projects list
        projectList = MySQLOperation.getProjectList(MySQLOperation.getConnection());
        List<Project> newProjectList = new ArrayList<>();
        for (int i = 0; i < projectList.size(); i++) {
            if (projectList.get(i).getProject_timestamp().compareTo(startTime) >= 0 || projectList.get(i).getProject_timestamp().compareTo(endTime) <= 0) {
                newProjectList.add(projectList.get(i));
            }
        }
        this.projectlist = newProjectList;
        return newProjectList;
    }


    public List<Issue> getIssue() {

        List<Issue> newIssue = new ArrayList<>();
        for (int i = 0; i < projectList.size(); i++) {
            List<Issue> list = projectList.get(i).getIssues();
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getTimestamp().compareTo(startTime) >= 0 || list.get(j).getTimestamp().compareTo(endTime) <= 0) {
                    newIssue.add(list.get(j));
                }
            }
        }
        this.issue = newIssue;
        return newIssue;
    }

    public List<String> getCommentUser() {
        List<Issue> issue = getIssue();
        List<String> checkList = new ArrayList<>();
        for (int i = 0; i < issue.size(); i++) {
            List<Comment> comment = issue.get(i).getComments();
            for (int j = 0; j < comment.size(); j++) {
                checkList.add(comment.get(j).getUser());
            }
        }
        return checkList;
    }

    public Map<String, Integer> getCommentUserMap() {
        List<String> list = getCommentUser();
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>(); //key : user name, value:issue number
        for (String string : list) {
            if (hashMap.get(string) != null) {
                Integer value = hashMap.get(string);
                hashMap.put(string, value + 1);
            } else {
                hashMap.put(string, 1);
            }
        }
        return hashMap;
    }

    public String toString() {
        getProjectlist();
        List<Issue> issue = getIssue();

        int resovled = 0;
        int unresovled = 0;
        int inProgress = 0;
        int other = 0;
        String str = "";
        if (duration.equals("Weekly")) str += "\t\t\t\t\t" + "WEEKLY REPORT" + "\t\t\t\t\t" + "\n"; //title
        else str += "\t\t\t\t\t" + "MONTHLY REPORT" + "\t\t\t\t\t" + "\n"; //title


        str += "Starting from " + startTime.toLocalDateTime().toLocalDate() + " to " + endTime.toLocalDateTime().toLocalDate() + "\n\n";

        String performance = showTopTeamPerformer();
        str += performance + "\n";
        str += "this week has " + projectlist.size() + " projects in Process" + "\n";
        str += "\n----------Project List-----------" + "\n";
        for (int i = 0; i < projectlist.size(); i++) {
            str += "|" + projectlist.get(i).getName() + "| " + " ";
            str += "\n";
        }
        str += "\n----------Issue Statement-----------" + "\n";
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        List<Integer> list3 = new ArrayList<>();
        for (int i = 0; i < issue.size(); i++) { //issue comment react
            if (issue.get(i).getStatus().equalsIgnoreCase("closed")) {
                list1.add(issue.get(i).getIssue_id());
                resovled++;
            } else if (issue.get(i).getStatus().equalsIgnoreCase("whatever")) {
                list2.add(issue.get(i).getIssue_id());
                unresovled++;
            } else if (issue.get(i).getStatus().equalsIgnoreCase("In Progress") || issue.get(i).getStatus().equalsIgnoreCase("Open")) {
                list3.add(issue.get(i).getIssue_id());
                inProgress++;
            } else other++;
        }
        str += "The issues resolved in this week is: " + resovled + "\n" + "issues id: " + list1.toString() + "\n";
        str += "The issues unresolved in this week is: " + unresovled + "\n" + "issues id: " + list2.toString() + "\n";
        str += "The issues in progress in this week is: " + inProgress + "\n" + "issues id: " + list3.toString() + "\n";
        str += "Other: " + other + "\n";
        str += "\n----------User Activity-----------" + "\n";
        str += "---issue---" + "\n";
        Map<String, Integer> map = getUserMap();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            str += entry.getKey() + "----issue created----" + entry.getValue() + "\n";
        }
        str += "---comment---" + "\n";
        Map<String, Integer> map1 = getCommentUserMap();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            str += entry.getKey() + "----comment created----" + entry.getValue() + "\n";
        }
        str += "\n----------Comment and Reaction-----------" + "\n";
        int commentCount = 0;
        int count = 0;
        for (int i = 0; i < issue.size(); i++) {
            List<Comment> comment = issue.get(i).getComments();
            str += "Project_id " + issue.get(i).getProject_id() + " ,Issue_id " + issue.get(i).getIssue_id() + "----" + "number of comment created: " + comment.size();
            for (int j = 0; j < comment.size(); j++) {
                List<React> reactlist = comment.get(j).getReact();
                for (int k = 0; k < reactlist.size(); k++) {
                    count += reactlist.get(k).getCount();
                }
            }
            str += "----" + "total " + count + " reaction in this comment----" + "\n";
            count = 0;

        }
        return str;
    }

    public List<String> getTopPerformance() {
        List<Issue> issue = getIssue();
        List<String> checkList = new ArrayList<>();
        for (int i = 0; i < issue.size(); i++) {
            checkList.add(issue.get(i).getAssignee());
            checkList.add(issue.get(i).getCreatedBy());
        }
        return checkList;
    }

    public List<String> getUser() {
        List<Issue> issue = getIssue();
        List<String> checkList = new ArrayList<>();
        for (int i = 0; i < issue.size(); i++) {
            checkList.add(issue.get(i).getCreatedBy());
        }
        return checkList;
    }

    public Map<String, Integer> getUserMap() {
        List<String> list = getUser();
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>(); //key : user name, value:issue number
        for (String string : list) {
            if (hashMap.get(string) != null) {
                Integer value = hashMap.get(string);
                hashMap.put(string, value + 1);
            } else {
                hashMap.put(string, 1);
            }
        }
        return hashMap;
    }

    public String showTopTeamPerformer() {  //show top assignee
        List<String> list = getTopPerformance();
        String str = "";
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>(); //key : assignee name, value:issue number
        for (String string : list) {
            if (hashMap.get(string) != null) {
                Integer value = hashMap.get(string);
                hashMap.put(string, value + 1);
            } else {
                hashMap.put(string, 1);
            }
        }
        //   show who appearences the moximum times in the list
        String maxKey = "";
        int maxNo = 0;
        Set<String> keySet = hashMap.keySet();
        for (String key : keySet) {
            int valueNo = (Integer) hashMap.get(key);
            if (valueNo > maxNo) {
                maxNo = valueNo;
                maxKey = key;
            }
        }
        return str = "The top performer in this week is:" + maxKey;
    }

    public void showComment(Comment comment) {

    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public static void main(String[] args) {

        String date = "2019-08-08 00:00:00";   // user type date
        Timestamp timestamp = Timestamp.valueOf(date);
        reportGeneration reportGeneration = new reportGeneration(timestamp, "Weekly");  // type Weekly or Monthly
        System.out.println(reportGeneration.toString());

    }

}
