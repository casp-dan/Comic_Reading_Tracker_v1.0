package model;

import java.util.ArrayList;

public class MarvelBook extends Book {

    private String title;
    private boolean xmen;
    private String publisher;
    private ArrayList<Integer> issues;

    public MarvelBook(String title, boolean xmen){
        super(title);
        this.xmen=xmen;
        publisher="Marvel";
    }

    public boolean isXmen(){
        return xmen;
    }

    public String getPublisher() {
        return publisher;
    }
}
