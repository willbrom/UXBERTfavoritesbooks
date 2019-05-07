package com.example.uxbertfavoritesbooks.bookdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Book {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public final String bookName;
    public final String bookAuthor;
    public final String releaseDate;
    public final String bookDetails;
    public boolean notificationActive;

    public Book(String bookName, String bookAuthor, String releaseDate, String bookDetails, boolean notificationActive) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.releaseDate = releaseDate;
        this.bookDetails = bookDetails;
        this.notificationActive = notificationActive;
    }
}
