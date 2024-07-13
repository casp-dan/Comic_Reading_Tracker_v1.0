package model;



public class Comic {

    //private String title;
    private int issue;

    public Comic(int issue){
        this.issue=issue;
    }

    public String toString(){
        return Integer.toString(issue);
    }
}
