package bugs;

import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.TimeSeriesPlot;


import java.util.*;

public class statistics {
    public statistics() {
    }

    public List<Project> getProject() {
        List<Project> projectList = MySQLOperation.getProjectList(MySQLOperation.getConnection());
        return projectList;
    }

    public List<Issue> getIssue() {
        List<Project> projectList = getProject();
        List<Issue> issuesList = new ArrayList<>();
        for (int i = 0; i < projectList.size(); i++) {
            List<Issue> issues = projectList.get(i).getIssues();
            for (int j = 0; j < issues.size(); j++) {
                issuesList.add(issues.get(j));
            }
        }
        return issuesList;
    }

    public List<String> getTagsList() {
        List<Issue> issue = getIssue();
        List<String> checkList = new ArrayList<>();
        for (int i = 0; i < issue.size(); i++) {
            checkList.add(issue.get(i).getTags());
        }
        return checkList;
    }

    public Map<String, Integer> getTagMap() {
        List<String> list = getTagsList();
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

    public List<String> getTimeList() {
        List<Issue> issue = getIssue();
        List<String> checkList = new ArrayList<>();
        for (int i = 0; i < issue.size(); i++) {
            String month = issue.get(i).getTime().substring(5, 7);
            String str = null;
            switch (month) {
                case "01":
                    str = "Jan";
                    break;
                case "02":
                    str = "Feb";
                    break;
                case "03":
                    str = "Mar";
                    break;
                case "04":
                    str = "Apr";
                    break;
                case "05":
                    str = "May";
                    break;
                case "06":
                    str = "Jun";
                    break;
                case "07":
                    str = "Jul";
                    break;
                case "08":
                    str = "Aug";
                    break;
                case "09":
                    str = "Sep";
                    break;
                case "10":
                    str = "Oct";
                    break;
                case "11":
                    str = "Nov";
                    break;
                case "12":
                    str = "Dec";
                    break;
            }
            checkList.add(str);
        }
        return checkList;
    }


    public String time() {
        String str = "";

        List<String> list = getTimeList();
        List<String> list1 = new ArrayList<>();
        list1.add("Jan");
        list1.add("Feb");
        list1.add("Mar");
        list1.add("Apr");
        list1.add("May");
        list1.add("Jun");
        list1.add("Jul");
        list1.add("Aug");
        list1.add("Sep");
        list1.add("Oct");
        list1.add("Nov");
        list1.add("Dec");
        List<String> list2 = new ArrayList<>();
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list1.size(); j++) {
                if (list.get(i).equals(list1.get(j))) {
                    list2.set(j, String.valueOf(Integer.valueOf(list2.get(j)) + 1));
                }
            }
        }


        Table table = Table.create("Monthly statistical data").addColumns(StringColumn.create("Month", list1), StringColumn.create("Number of Issue", list2));
        str += table.toString();
        return str;
    }

    public void timeHTML() {
        List<String> list = getTimeList();
        List<String> list1 = new ArrayList<>();
        list1.add("Jan");
        list1.add("Feb");
        list1.add("Mar");
        list1.add("Apr");
        list1.add("May");
        list1.add("Jun");
        list1.add("Jul");
        list1.add("Aug");
        list1.add("Sep");
        list1.add("Oct");
        list1.add("Nov");
        list1.add("Dec");
        List<String> list2 = new ArrayList<>();
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        list2.add("0");
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list1.size(); j++) {
                if (list.get(i).equals(list1.get(j))) {
                    list2.set(j, String.valueOf(Integer.valueOf(list2.get(j)) + 1));
                }
            }
        }

        Table table = Table.create("Monthly statistic").addColumns(StringColumn.create("Month", list1), StringColumn.create("Number of Issue", list2));
        Plot.show(
                TimeSeriesPlot.create(
                        "Monthly statistical data",//折线图名称
                        table, "Month", "Number of Issue"));
    }

    public String issueStatus() {
        String str = null;
        List<Issue> issue = getIssue();
        String status[] = {"resolved", "unresolved", "in progress", "others"};
        int rnum = 0;
        int urnum = 0;
        int inum = 0;
        int onum = 0;
        for (int i = 0; i < issue.size(); i++) {
            if (issue.get(i).getStatus().equalsIgnoreCase("closed")) {
                rnum++;
            } else if (issue.get(i).getStatus().equalsIgnoreCase("whatever")) {
                urnum++;
            } else if (issue.get(i).getStatus().equalsIgnoreCase("In Progress") || issue.get(i).getStatus().equalsIgnoreCase("open")) {
                inum++;
            } else onum++;
        }
        int num[] = {rnum, urnum, inum, onum};
        Table table1 = Table.create("Issue status").addColumns(StringColumn.create("status", status), IntColumn.create("number of issue", num));
        str += table1.toString();

        return str;
    }

    public void issueStatusHTML() {
        List<Issue> issue = getIssue();
        String status[] = {"resolved", "unresolved", "in progress", "others"};
        int rnum = 0;
        int urnum = 0;
        int inum = 0;
        int onum = 0;
        for (int i = 0; i < issue.size(); i++) {
            if (issue.get(i).getStatus().equalsIgnoreCase("closed")) {
                rnum++;
            } else if (issue.get(i).getStatus().equalsIgnoreCase("whatever")) {
                urnum++;
            } else if (issue.get(i).getStatus().equalsIgnoreCase("In Progress") || issue.get(i).getStatus().equalsIgnoreCase("open")) {
                inum++;
            } else onum++;
        }
        int num[] = {rnum, urnum, inum, onum};
        Table table1 = Table.create("Issue status").addColumns(StringColumn.create("status", status), IntColumn.create("number of issue", num));
        Plot.show(
                TimeSeriesPlot.create(
                        "Issue status",//折线图名称
                        table1, "status", "number of issue"));
    }

    public String tag() {
        String str = "";
        Map<String, Integer> map = getTagMap();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            list1.add(entry.getKey());
            list2.add(String.valueOf(entry.getValue()));
        }
        Table table = Table.create("Tag").addColumns(StringColumn.create("Tag name", list1), StringColumn.create("number of issue", list2));
        str += table.toString();
        return str;
    }

    public void tagHTML() {

        Map<String, Integer> map = getTagMap();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            list1.add(entry.getKey());
            list2.add(String.valueOf(entry.getValue()));
        }
        Table table = Table.create("Tag").addColumns(StringColumn.create("Tag name", list1), StringColumn.create("number of issue", list2));
        Plot.show(
                TimeSeriesPlot.create(
                        "Tag",//折线图名称
                        table, "Tag name", "number of issue"));
    }

    public String showStatic() {
        String str = "";
        String tag = tag();
        str += tag;
        str += "\n" + "\n";
        String issueStatus = issueStatus();
        str += issueStatus;
        str += "\n" + "\n";
        String time = time();
        str += time;
        return str;
    }

    public static void main(String[] args) {
        statistics statistic = new statistics();
        System.out.println(statistic.showStatic());
//        statics.tagHTML();
//        statics.issueStatusHTML();
//        statics.timeHTML();
        int count = 0;
        m:
        while (true) {
            if (count == 3) {
                break;
            }
            System.out.println("press 1 generate tag graph, press 2 generate issue graph, press 3 generate monthly issue graph, press 4 quit");
            Scanner in = new Scanner(System.in);
            int num = in.nextInt();
            System.out.println();
            switch (num) {
                case 1:
                    statistic.tagHTML();
                    System.out.println("wait");
                    count++;
                    break;
                case 2:
                    statistic.issueStatusHTML();
                    System.out.println("wait");
                    count++;
                    break;
                case 3:
                    statistic.timeHTML();
                    System.out.println("wait");
                    count++;
                    break;
                case 4:
                    break m;
            }
        }
    }
}
