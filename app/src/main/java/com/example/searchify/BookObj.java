package com.example.searchify;

public class BookObj {
    private String name;
    private String category;
    private String owner;
    private String writer;
    private String availability;
    private String book_id;



    private String imageuri;

    public BookObj(String name, String category, String owner, String writer, String availability, String book_id, String imageuri) {
        this.name = name;
        this.category = category;
        this.owner = owner;
        this.writer = writer;
        this.availability = availability;
        this.book_id = book_id;
        this.imageuri = imageuri;
    }

    public BookObj() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }
    @Override
    public String toString() {
        return "BookObj{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", owner='" + owner + '\'' +
                ", writer='" + writer + '\'' +
                ", availability='" + availability + '\'' +
                ", book_id='" + book_id + '\'' +
                '}';
    }
}
