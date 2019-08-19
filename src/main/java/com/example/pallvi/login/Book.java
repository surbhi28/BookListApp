package com.example.pallvi.login;

import java.io.Serializable;

public class Book implements Serializable {
    /**
     * Title of the Book
     */

    private String image;
    private String bimag;
    private String title;
    private String link;


    /**
     * Author of the Book
     */
    private String author;

    private String publish;
    private String price;
    private String curr;
    private String descr;


    /**
     * Constructs a new {@link Book}.
     *
     * @param title  is the title of the book
     * @param author is the author of the book
     * @param bookPrice is the author of the book
     */
    public Book(String image, String inner_image, String title, String Link, String author, String publisher, String bookPrice, String code, String description) {
        this.image=image;
        this.bimag=inner_image;
        this.title = title;
        this.link = Link;
        this.author = author;
        this.publish=publisher;
        this.price = bookPrice;
        this.curr=code;
        this.descr=description;
    }

    public String getImage(){
        return this.image;
    }

    public String getinnerImage(){
        return this.bimag;
    }

    public String getTitle() {
        return this.title;
    }

    public String getLink(){
        return this.link;
    }


    public String getAuthor() {
        return this.author;
    }

    public String getPublish() {
        return this.publish;
    }


    public String getPrice(){
        return price;
    }

    public String getCurr() {
        return this.curr;
    }
    public String getDescr() {

        return this.descr;
    }




}



