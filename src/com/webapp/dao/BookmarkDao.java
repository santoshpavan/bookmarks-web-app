package com.webapp.dao;

import com.webapp.DataStore;
import com.webapp.entities.Bookmark;

public class BookmarkDao {
    public Bookmark[][] getBookMarks() {
        return DataStore.getBookmarks();
    }
}
