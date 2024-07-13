package model;

import java.util.ArrayList;

public class ImageBook extends Book {

    private String title;
    private String publisher;
    private ArrayList<Integer> issues;

    public ImageBook(String title){
        super(title);
        publisher="Image";
    }

    public String getPublisher() {
        return publisher;
    }

}
