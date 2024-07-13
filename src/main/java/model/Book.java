package model;

import java.util.ArrayList;

public class Book{

    private String title;
    private String publisher;
    private ArrayList<Integer> issues;

    public Book(String title){
        this.title=title;
        issues=new ArrayList<Integer>();
    }

    public void addIssue(int issue) {
        if (!issues.contains(issue)){
            issues.add(issue);
        }
    }

    public void addIssue(int start, int end) {
        int issue=start;
        while (issue<=end){
            issues.add(issue);
            issue++;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String toString(){
        String repr=title + ":\n";
        for (int issue: issues){
            repr+=Integer.toString(issue) + "\n";
        }
        return repr;
    }



}
