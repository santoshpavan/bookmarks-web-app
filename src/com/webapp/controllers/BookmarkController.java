package com.webapp.controllers;

import com.webapp.constants.KidFriendlyStatus;
import com.webapp.entities.Bookmark;
import com.webapp.entities.User;
import com.webapp.managers.BookmarkManager;

// Singleton
public class BookmarkController extends HttpServlet{
    /*
    Commenting out because Servlet implements it as a singleton
	private static BookmarkController bookmarkControllerInstance = new BookmarkController();
    
    private BookmarkController() {}
	
    public static BookmarkController getInstance() {
        return bookmarkControllerInstance;
    }
	*/
    public void saveUserBookmark(User user, Bookmark bookmark) {
        BookmarkManager.getInstance().saveUserBookMark(user, bookmark);
    }

    public void setKidFriendlyStatus(User user, KidFriendlyStatus kidFriendlyStatus, Bookmark bookmark) {
        BookmarkManager.getInstance().setKidFriendlyStatus(user, kidFriendlyStatus, bookmark);
    }

    public void share(User user, Bookmark bookmark) {
        BookmarkManager.getInstance().share(user, bookmark);
    }
}
