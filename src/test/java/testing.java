import bugs.Project;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.List;

import static bugs.MySQLOperation.getProjectList;

public class testing {
    public static void main(String[] args) {
        System.out.println(FuzzySearch.tokenSetPartialRatio("wahtever","thsi is whatever"));

    }
}
