package bugs;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class reportGeneration {
    private List<Project> projectlist;
    private List<Issue> issue;
    private List<Comment> comment;
    private Timestamp startTime;
    private Timestamp endTime;


    public reportGeneration(Timestamp time,String date) {// make sure the date is sunday
        if(date.equals("Weekly")) {
            endTime = new Timestamp(Calendar.getInstance().get(
                    Calendar.YEAR), Calendar.getInstance().get(
                    Calendar.MONTH), Calendar.getInstance().get(
                    Calendar.DATE) + 7, Calendar.getInstance().get(
                    Calendar.HOUR), Calendar.getInstance().get(
                    Calendar.MINUTE), Calendar.getInstance().get(
                    Calendar.SECOND), 0);// 7 days before as start date
            startTime = time;
        }else if(date.equals("Monthly")){
            endTime = new Timestamp(Calendar.getInstance().get(
                    Calendar.YEAR), Calendar.getInstance().get(
                    Calendar.MONTH)+1, Calendar.getInstance().get(
                    Calendar.DATE) , Calendar.getInstance().get(
                    Calendar.HOUR), Calendar.getInstance().get(
                    Calendar.MINUTE), Calendar.getInstance().get(
                    Calendar.SECOND), 0);// 7 days before as start date
            startTime = time;
        }
    }

    public List<Project> getProjectlist() {  //base on the date to get the projects list
        List<Project> projectList=MySQLOperation.getProjectList(MySQLOperation.getConnection());
        List<Project> newProjectList=new ArrayList<>();
        for (int i = 0; i < projectList.size(); i++) {
            if (projectList.get(i).getProject_timestamp().compareTo(startTime)>=0||projectList.get(i).getProject_timestamp().compareTo(endTime)<=0){
                newProjectList.add(projectList.get(i));
            }
        }
        this.projectlist=newProjectList;
        return newProjectList;
    }


    public List<Issue> getIssue() {
        List<Project> projectList=MySQLOperation.getProjectList(MySQLOperation.getConnection());
        List<Issue> newIssue=new ArrayList<>();
        for (int i = 0; i < projectList.size(); i++) {
            List<Issue> list=projectList.get(i).getIssues();
            for (int j = 0; j < list.size(); j++) {
                if(list.get(j).getTimestamp().compareTo(startTime)>=0||list.get(j).getTimestamp().compareTo(endTime)<=0){
                    newIssue.add(list.get(j));
                }
            }
        }
        this.issue=newIssue;
        return newIssue;
    }


    public String toString(){
        List<Issue>issue=getIssue();
        List<Project>projects=getProjectlist();
        int resovled=0;
        int unresovled=0;
        int inProgress=0;
        int other=0;
        String str="";
        str+="\t\t\t\t\t"+"weekly report"+"\t\t\t\t\t"+"\n"; //title
        String performance=showTopTeamPerformer();
        str+=performance+"\n";
        str+="this week has "+projectlist.size()+" in Process"+"\n";
        for (int i = 0; i < projectlist.size(); i++) {
            str+=projectlist.get(i).getName()+"\n";
        }
        for (int i = 0; i < issue.size(); i++) { //issue comment react
            if (issue.get(i).getStatus().equalsIgnoreCase("closed"))resovled++;
            else if(issue.get(i).getStatus().equalsIgnoreCase("whatever"))unresovled++;
            else if (issue.get(i).getStatus().equalsIgnoreCase("In Progress")||issue.get(i).getStatus().equalsIgnoreCase("open"))inProgress++;
            else other++;
        }
        str+="The issues resolved in this week is: "+resovled+"\n";
        str+="The issues unresolved in this week is: "+unresovled+"\n";
        str+="The issues in progress in this week is: "+inProgress+"\n";
        str+="Other: "+other+"\n";
        str+="the List of issues are below"+"\n";
        for (int i = 0; i < issue.size(); i++) {
            str+=issue.get(i).getTitle()+" "+issue.get(i).getStatus()+"\n";
            str+="Has "+comment.size()+" comments in this issue"+"\n";
            comment=issue.get(i).getComments();
            for (int j = 0; j < comment.size(); j++) {
                str+=comment.get(j).getText()+" which with ";
                List<React> reactlist= comment.get(j).getReact();
                for (int k = 0; k < reactlist.size(); k++) {
                    str+=reactlist.get(k).getCount()+"people reacting with this comment"+"\n";
                }
            }
        }
        return str;
    }
    public List<String> getTopPerformance(){
        List<Issue>issue=getIssue();
        List<String> checkList = new ArrayList<>();
        for (int i = 0; i < issue.size(); i++) {
            checkList.add(issue.get(i).getAssignee());
            checkList.add(issue.get(i).getCreatedBy());
        }return checkList;
    }
    public String showTopTeamPerformer(){  //show top assignee
        List<String> list=getTopPerformance();
        String str="";
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>(); //key : assignee name, value:issue number
        for (String string : list) {
            if (hashMap.get(string) != null) {
                Integer value = hashMap.get(string);
                hashMap.put(string, value+1);
            } else {
                hashMap.put(string, 1);
            }
        }
        //   show who appearences the moximum times in the list
        String maxKey = "";
        int maxNo= 0;
        Set<String> keySet = hashMap.keySet();
        for(String key:keySet){
            int valueNo = (Integer)hashMap.get(key);
            if(valueNo>maxNo){
                maxNo = valueNo;
                maxKey = key;
            }
        }
        return str="The top performer in this week is:"+maxKey;
    }

    public void showComment(Comment comment){

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

        Scanner in=new Scanner(System.in);
        System.out.println("Print the date when you generate report(in format  yyyy-MM-dd HH:mm:ss  ): ");
        String dateText=in.nextLine();
        Date date=new Date(dateText);
        System.out.println("Option 1 selection=Weekly");
        System.out.println("Option 2 selection=Monthly");
        System.out.print("Type Weekly or Monthly: ");
        String type=in.nextLine();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=df.format(date);
        Timestamp timestamp=Timestamp.valueOf(time);
        reportGeneration reportGeneration=new reportGeneration(timestamp,type);
        System.out.println(reportGeneration.toString());


    }

}
