package model;

import java.util.ArrayList;

public class DCBook extends Book {

    private String title;
    private String publisher;
    private ArrayList<Integer> issues;

    public DCBook(String title){
        super(title);
        publisher="DC";
    }

    public String getPublisher() {
        return publisher;
    }

}