package com.webapp.dao;

import com.webapp.DataStore;
import com.webapp.entities.Bookmark;
import com.webapp.entities.UserBookmark;

import java.util.List;

// directly deals with data. Usually has SQL
public class BookmarkDao {
    public List<List<Bookmark>> getBookMarks() {
        return DataStore.getBookmarks();
    }

    public void saveUserBookmark(UserBookmark userBookmark) {
        // adding to the DB
        DataStore.add(userBookmark);
    }
}
