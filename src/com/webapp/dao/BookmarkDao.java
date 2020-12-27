package com.webapp.dao;

import com.webapp.DataStore;
import com.webapp.entities.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// directly deals with data. Usually has SQL
public class BookmarkDao {
    public List<List<Bookmark>> getBookMarks() {
        return DataStore.getBookmarks();
    }

    public void saveUserBookmark(UserBookmark userBookmark) {
        // adding to the DB
        // connect to the database
        // Connection and Statement are interfaces
        // connection string syntax: <protocol>:<sub-protocol>:<data-source details>
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false", "root", "477905");
             Statement statement = connection.createStatement();) {
            if (userBookmark.getBookmark() instanceof Book) {
                saveUserBook(userBookmark, statement);
            } else if (userBookmark.getBookmark() instanceof Movie) {
                saveUserMovie(userBookmark, statement);
            } else {
                saveUserWebLink(userBookmark, statement);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void saveUserWebLink(UserBookmark userBookmark, Statement statement) throws SQLException {
        String query = "insert into User_WebLink (user_id, weblink_id) values (" +
                userBookmark.getUser().getId() + ", " + userBookmark.getBookmark().getId() + ")";
        // similar to executeUpdate in DataStore but this is for Updates and that for SELECT
        statement.executeUpdate(query);
    }

    private void saveUserMovie(UserBookmark userBookmark, Statement statement) throws SQLException {
        String query = "insert into User_Book (user_id, book_id) values (" +
                userBookmark.getUser().getId() + ", " + userBookmark.getBookmark().getId() + ")";
        // similar to executeUpdate in DataStore but this is for Updates and that for SELECT
        statement.executeUpdate(query);
    }

    private void saveUserBook(UserBookmark userBookmark, Statement statement) throws SQLException {
        String query = "insert into User_Movie (user_id, movie_id) values (" +
                userBookmark.getUser().getId() + ", " + userBookmark.getBookmark().getId() + ")";
        // similar to executeUpdate in DataStore but this is for Updates and that for SELECT
        statement.executeUpdate(query);
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
