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

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static List<User> users = new ArrayList<>();
    private static List<List<Bookmark>> bookmarks = new ArrayList<>();
    private static List<UserBookmark> userBookmarks = new ArrayList<>();

    public static void loadData() {
        loadUsers();
        loadWebLinks();
        loadMovies();
        loadBooks();
    }

    private static void loadUsers() {
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

    private static void loadWebLinks() {
        List<String> data = new ArrayList<>();
        IOUtil.read(data, "Weblink");
        List<Bookmark> bookmarkList = new ArrayList<>();
        for (String row: data) {
            String[] values = row.split("\t");
            bookmarkList.add(BookmarkManager.getInstance().createWebLink(Long.parseLong(values[0]), values[1], values[2], values[3]));
        }
        bookmarks.add(bookmarkList);
    }

    private static void loadMovies() {
        List<String> data = new ArrayList<>();
        IOUtil.read(data, "Movie");
        List<Bookmark> bookmarkList = new ArrayList<>();
        for (String row: data) {
            String[] values = row.split("\t");
            String[] cast = values[3].split(","); //the cast
            String[] directors = values[4].split(","); //the directors
            bookmarkList.add(BookmarkManager.getInstance().createMovie(Long.parseLong(values[0]), values[1], Integer.parseInt(values[2]), cast, directors, MovieGenreType.valueOf(values[5]), Double.parseDouble(values[6])));
        }
        bookmarks.add(bookmarkList);
    }

    private static void loadBooks() {
        List<String> data = new ArrayList<>();
        IOUtil.read(data, "Book");
        List<Bookmark> bookmarkList = new ArrayList<>();
        for (String row: data) {
            String[] values = row.split("\t");
            String[] authors = values[4].split(",");
            bookmarkList.add(BookmarkManager.getInstance().createBook(Long.parseLong(values[0]), values[1], Integer.parseInt(values[2]), values[3], authors, BookGenreType.valueOf(values[5]), Double.parseDouble(values[6])));
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
