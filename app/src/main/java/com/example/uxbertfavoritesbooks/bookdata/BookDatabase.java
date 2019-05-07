package com.example.uxbertfavoritesbooks.bookdata;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Book.class}, version = 1)
public abstract class BookDatabase extends RoomDatabase {

    private static final String DB_NAME = "bookDatabase.db";

    public static BookDatabase getInstance(Context context) {
        return RoomDatabaseInstance.create(context, DB_NAME);
    }

    private static class RoomDatabaseInstance {
        private static BookDatabase instance;
        private static BookDatabase create(Context context, String dbName) {
            if (instance == null)
                instance = Room.databaseBuilder(context, BookDatabase.class, dbName).build();
            return instance;
        }
    }

    public abstract BookDao getBookDao();
}
