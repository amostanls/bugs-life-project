import bugs.Project;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.sql.Connection;
import java.util.List;

import static bugs.MySQLOperation.getConnection;
import static bugs.MySQLOperation.getProjectList;

public class testing {
    public static void main(String[] args) throws Exception {
        Connection myConn = getConnection();
        System.out.println(FuzzySearch.tokenSetPartialRatio("wahtever","thsi is whatever"));
        List<Project> list= getProjectList(myConn);
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i).getId()+" "+list.get(i).getName()+" "+list.get(i).getIssues().size());
        }
    }
}
