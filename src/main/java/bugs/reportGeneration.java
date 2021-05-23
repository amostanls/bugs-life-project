package bugs;

import java.util.*;

public class reportGeneration {

    public reportGeneration() {
    }
    public List<Project> getProject(){ //get project list
        List<Project> projectList=MySQLOperation.getProjectList(MySQLOperation.connectionToDatabase());
        return projectList;
    }

    /**
     * key : project id
     * value : issues list
     * @return
     */
    public Map<Integer, ArrayList<Issue>> getIssue(){
        List<Project> projectList=getProject();
        Map<Integer, ArrayList<Issue>> map=new HashMap<>();
        for (int i = 0; i < projectList.size(); i++) {
            map.put(projectList.get(i).getId(),projectList.get(0).getIssues());
        }
        return map;
    }
    public void showIssue(Map<Integer, ArrayList<Issue>>map){
        int resovled=0;
        int unresovled=0;
        int inProgress=0;
        int other=0;
        for (Map.Entry<Integer, ArrayList<Issue>> entry : map.entrySet()) {//遍历map
            ArrayList<Issue>list=entry.getValue();
            for (Issue issue:list) {
                if (issue.getStatus().equals("Closed"))resovled++;
                else if (issue.getStatus().equals("Open")||issue.getStatus().equals("Whatever"))unresovled++;
                else if (issue.getStatus().equals("InProgress"))inProgress++;
                else other++;
            }
        }
        System.out.println("The issues resolved in this week is: "+resovled);
        System.out.println("The issues unresolved in this week is: "+unresovled);
        System.out.println("The issues resolved in this week is: "+inProgress);
        System.out.println("Ohter: "+other);
    }

    public List<String> TeamPerformer(){ // get assignee and store in list
        List<String> list = null;
        Map<Integer, ArrayList<Issue>> map=getIssue();
        for (Map.Entry<Integer, ArrayList<Issue>> entry : map.entrySet()) {//遍历map
            ArrayList<Issue> issue=entry.getValue();
            for (Issue issue1:issue) {
                list.add(issue1.getAssignee());
            }
        }
        return list;
    }
    public void showTopTeamPerformer(List<String> list){  //show top assignee
       HashMap<String, Integer> hashMap = new HashMap<String, Integer>(); //key : assignee name, value:issue number
       for (String string : list) {
           if (hashMap.get(string) != null) {
               Integer value = hashMap.get(string);
               hashMap.put(string, value+1);
           } else {
               hashMap.put(string, 1);
           }
       }

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
       System.out.println("The top performer in this week is:"+maxKey+" the issues he(she) participate are:"+maxNo);
   }
   public List<String> frequentTag(){
       List<String> list = null;
       Map<Integer, ArrayList<Issue>> map=getIssue();
       for (Map.Entry<Integer, ArrayList<Issue>> entry : map.entrySet()) {//遍历map
           ArrayList<Issue> issue=entry.getValue();
           for (Issue issue1:issue) {
               list.add(issue1.getTagAsString());
           }
       }
       return list;
   }
    public void showfrequentTag(List<String> list){  //show top assignee
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>(); //key : assignee name, value:issue number
        for (String string : list) {
            if (hashMap.get(string) != null) {
                Integer value = hashMap.get(string);
                hashMap.put(string, value+1);
            } else {
                hashMap.put(string, 1);
            }
        }

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
        System.out.println("The frequent label in this week is:"+maxKey+" which shows: "+maxNo+"times");
    }
    public void showInPDF(){

    }
}
