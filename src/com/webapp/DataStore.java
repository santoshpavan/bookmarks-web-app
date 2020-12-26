package com.webapp;

import com.webapp.constants.BookGenreType;
import com.webapp.constants.GenderType;
import com.webapp.constants.MovieGenreType;
import com.webapp.constants.UserType;
import com.webapp.entities.Bookmark;
import com.webapp.entities.User;
import com.webapp.entities.UserBookmark;
import com.webapp.managers.BookmarkManager;
import com.webapp.managers.UserManager;
import com.webapp.util.IOUtil;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static List<User> users = new ArrayList<>();
    private static List<List<Bookmark>> bookmarks = new ArrayList<>();
    private static List<UserBookmark> userBookmarks = new ArrayList<>();

    public static void loadData() {
        /*
        loadUsers();
        loadWebLinks(); //index 0
        loadMovies(); //index 1
        loadBooks(); //index 2
         */

        // loading JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // connect to the database
        // Connection and Statement are interfaces
        // connection string syntax: <protocol>:<sub-protocol>:<data-source details>
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false", "root", "477905");
        Statement statement = connection.createStatement();) {
            loadUsers(statement);
            loadWebLinks(statement);
            loadMovies(statement);
            loadBooks(statement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void loadUsers(Statement statement) {
        List<String> data = new ArrayList<>();
        IOUtil.read(data, "User");
        for (String row: data) {
            String[] values = row.split("\t");
            GenderType gender = GenderType.MALE;
            if (values[5].equals("f")) {
                gender = GenderType.FEMALE;
            } else {
                gender = GenderType.TRANSGENDER;
            }
            users.add(UserManager.getInstance().createUser(Long.parseLong(values[0]), values[1], values[2], values[3], values[4], gender, UserType.valueOf(values[6])));
        }
    }

    private static void loadWebLinks(Statement statement) throws SQLException {
        String query = "Select * from Weblink";
        ResultSet rs = statement.executeQuery(query);

        List<Bookmark> bookmarkList = new ArrayList<>();
        while (rs.next()) {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            String url = rs.getString("url");
            String host = rs.getString("host");

            bookmarkList.add(BookmarkManager.getInstance().createWebLink(id, title, url, host));
        }
        bookmarks.add(bookmarkList);
    }

    private static void loadMovies(Statement statement) throws SQLException {
        String query = "Select m.id, title, release_year, GROUP_CONCAT(DISTINCT a.name SEPARATOR ',') AS cast, GROUP_CONCAT(DISTINCT d.name SEPARATOR ',') AS directors, movie_genre_id, imdb_rating"
                + " from Movie m, Actor a, Movie_Actor ma, Director d, Movie_Director md "
                + "where m.id = ma.movie_id and ma.actor_id = a.id and "
                + "m.id = md.movie_id and md.director_id = d.id group by m.id";
        ResultSet rs = statement.executeQuery(query);

        List<Bookmark> bookmarkList = new ArrayList<>();
        while (rs.next()) {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            int releaseYear = rs.getInt("release_year");
            String[] cast = rs.getString("cast").split(",");
            String[] directors = rs.getString("directors").split(",");
            int genre_id = rs.getInt("movie_genre_id");
            MovieGenreType genre = MovieGenreType.values()[genre_id];
            double imdbRating = rs.getDouble("imdb_rating");

            bookmarkList.add(BookmarkManager.getInstance().createMovie(id, title, Integer.parseInt(""), cast, directors, genre, imdbRating));
        }
        bookmarks.add(bookmarkList);
    }

    private static void loadBooks(Statement statement) throws SQLException {
        String query = "Select b.id, title, publication_year, p.name, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, amazon_rating, created_date"
                + " from Book b, Publisher p, Author a, Book_Author ba "
                + "where b.publisher_id = p.id and b.id = ba.book_id and ba.author_id = a.id group by b.id";
        ResultSet rs = statement.executeQuery(query);

        List<Bookmark> bookmarkList = new ArrayList<>();
        while (rs.next()) {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            int publicationYear = rs.getInt("publication_year");
            String publisher = rs.getString("name");
            String[] authors = rs.getString("authors").split(",");
            int genre_id = rs.getInt("book_genre_id");
            BookGenreType genre = BookGenreType.values()[genre_id];
            double amazonRating = rs.getDouble("amazon_rating");

            Date createdDate = rs.getDate("created_date");
            System.out.println("createdDate: " + createdDate);
            Timestamp timeStamp = rs.getTimestamp(8);
            System.out.println("timeStamp: " + timeStamp);
            System.out.println("localDateTime: " + timeStamp.toLocalDateTime());

            System.out.println("id: " + id + ", title: " + title + ", publication year: " + publicationYear + ", publisher: " + publisher + ", authors: " + String.join(", ", authors) + ", genre: " + genre + ", amazonRating: " + amazonRating);

            bookmarkList.add(BookmarkManager.getInstance().createBook(id, title, publicationYear, publisher, authors, genre, amazonRating));
        }
        bookmarks.add(bookmarkList);
    }

    public static List<User> getUsers() {
        return users;
    }

    public static List<List<Bookmark>> getBookmarks() {
        return bookmarks;
    }

    public static void add(UserBookmark userBookmark) {
        userBookmarks.add(userBookmark);
    }
}
