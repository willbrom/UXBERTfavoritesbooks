package com.example.uxbertfavoritesbooks.bookdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BookDao {

    @Query("SELECT * FROM Book")
    List<Book> getAllBooks();

    @Insert
    void insert(Book... books);

    @Delete
    void delete(Book book);

    @Query("UPDATE Book SET notificationActive = :notificationActive WHERE id = :id")
    int updateNotificationActive(long id, boolean notificationActive);
}
