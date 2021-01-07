package com.webapp.controllers;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webapp.constants.KidFriendlyStatus;
import com.webapp.entities.Bookmark;
import com.webapp.entities.User;
import com.webapp.managers.BookmarkManager;
import com.webapp.managers.UserManager;

// the URLs
@WebServlet(urlPatterns= {"/bookmark", "/bookmark/save", "/bookmark/mybooks"})
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
		RequestDispatcher dispatcher = null;
		// System.out.println("Servlet path: " + request.getServletPath());
		
		//checking if session is till valid
		if (request.getSession().getAttribute("userId") != null) {
			// getAttribute -> Object and getParameter -> string
			long userId = (long) request.getSession().getAttribute("userId");
			
			//if the request is coming from this path
			if (request.getServletPath().contains("save")) {
				// it's save
				dispatcher = request.getRequestDispatcher("/mybooks.jsp");
				String bid = request.getParameter("bid");
				
				User user = UserManager.getInstance().getUser(userId);
				Bookmark bookmark  = BookmarkManager.getInstance().getBook(Long.parseLong(bid));
				BookmarkManager.getInstance().saveUserBookMark(user, bookmark);
				
				Collection<Bookmark> list = BookmarkManager.getInstance().getBooks(true, userId);
				request.setAttribute("books", list);
			}
			else if (request.getServletPath().contains("mybooks")) {
				// my books
				dispatcher = request.getRequestDispatcher("/mybooks.jsp");
				Collection<Bookmark> list = BookmarkManager.getInstance().getBooks(true, userId);
				request.setAttribute("books", list);
				
			}
			else {
				dispatcher = request.getRequestDispatcher("/browse.jsp");
				Collection<Bookmark> list = BookmarkManager.getInstance().getBooks(false, userId);
				request.setAttribute("books", list);
			}
		}
		else {
			//session has expired; redirect to login page
			dispatcher = request.getRequestDispatcher("/login.jsp");
		}
				
		//forwarding to view
		dispatcher.forward(request, response);
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
    
    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
