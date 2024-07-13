package model;

import java.util.ArrayList;

public class Run {

    private String title;
    private ArrayList<Integer> issues;

    public Run(String title, ArrayList<Run> list){
        this.title=title;
        issues=new ArrayList<Integer>();
    }

    public void addIssue(int issue){
        if (!issues.contains(issue)){
            issues.add(issue);
        }
    }

    public String toString(){
        String repr=title + ":\n";
        for (int issue: issues){
            repr+=Integer.toString(issue) + "\n";
        }
        return repr;
    }

    public String getTitle(){
        return title;
    }

}
