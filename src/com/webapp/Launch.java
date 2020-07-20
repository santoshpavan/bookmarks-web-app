package com.webapp;

import com.webapp.entities.Bookmark;
import com.webapp.entities.User;
import com.webapp.managers.BookmarkManager;
import com.webapp.managers.UserManager;

public class Launch {
    private static User[] users;
    private static Bookmark[][] bookmarks;

    public static void loadData() {
        System.out.println("Loading data...");
        DataStore.loadData();

        users = UserManager.getInstance().getUsers();
        bookmarks = BookmarkManager.getInstance().getBookmarks();

        System.out.println("Printing data...");
        printUserData();
        printBookmarkData();
    }

    public static void printUserData() {
        for(User user: users) {
            System.out.println(user);
        }
    }

    public static void printBookmarkData() {
        for(Bookmark[] bookmarksList: bookmarks) {
            for(Bookmark bookmark: bookmarksList) {
                System.out.println(bookmark);
            }
        }
    }

    private static void startBookmarking() {
        System.out.println("\nBookmarking...");
        for (User user: users) {
            View.bookmark(user, bookmarks);
        }
    }

    public static void main(String[] args) {
        loadData();
        startBookmarking();
    }
}
