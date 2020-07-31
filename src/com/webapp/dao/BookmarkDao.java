package com.webapp.dao;

import com.webapp.DataStore;
import com.webapp.entities.Bookmark;
import com.webapp.entities.UserBookmark;
import com.webapp.entities.WebLink;

import java.util.ArrayList;
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

    // in real application SQL or hibernate queries are used
    public List<WebLink> getAllWebLinks() {
        List<WebLink> result = new ArrayList<>();
        List<List<Bookmark>> bookmarks = DataStore.getBookmarks();
        List<Bookmark> allWebLinks = bookmarks.get(0);

        for (Bookmark bookmark: allWebLinks) {
            // downcasting bookmark to weblink is required
            result.add((WebLink) bookmark);
        }
        return result;
    }

    // get all the weblinks with status NOT_ATTEMPTED
    public List<WebLink> getWebLinks(WebLink.DownloadStatus downloadStatus) {
        List<WebLink> result = new ArrayList<>();

        List<WebLink> allWebLinks = getAllWebLinks();

        for (WebLink webLink: allWebLinks) {
            if (webLink.getDownloadStatus().equals(downloadStatus)) {
                result.add(webLink);
            }
        }

        return result;
    }
}
