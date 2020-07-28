package com.webapp;

import com.webapp.entities.Bookmark;
import com.webapp.entities.User;
import com.webapp.managers.BookmarkManager;
import com.webapp.managers.UserManager;

import java.util.List;

public class Launch {
    private static List<User> users;
    private static List<List<Bookmark>> bookmarks;

    public static void loadData() {
        System.out.println("Loading data...");
        DataStore.loadData();

        users = UserManager.getInstance().getUsers();
        bookmarks = BookmarkManager.getInstance().getBookmarks();
    }

    public static void printUserData() {
        for(User user: users) {
            System.out.println(user);
        }
    }

    public static void printBookmarkData() {
        for(List<Bookmark> bookmarksList: bookmarks) {
            for(Bookmark bookmark: bookmarksList) {
                System.out.println(bookmark);
            }
        }
    }

    private static void start() {
        System.out.println("\nBookmarking...");
        for (User user: users) {
            View.browse(user, bookmarks);
        }
    }

    public static void main(String[] args) {
        loadData();
        start();
    }
}
