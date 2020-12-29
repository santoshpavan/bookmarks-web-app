package com.webapp.controllers;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public BookmarkController() {
        // TODO Auto-generated constructor stub
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Collection<Bookmark> list = BookmarkManager.getInstance().getBooks(false, 5);
		request.setAttribute("books", list);
		//forwarding to view
		request.getRequestDispatcher("/browse.jsp").forward(request, response);
	}
	
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
