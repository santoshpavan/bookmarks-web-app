package com.webapp.dao;

import com.webapp.DataStore;
import com.webapp.constants.BookGenreType;
import com.webapp.entities.*;
import com.webapp.managers.BookmarkManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

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

    public void updateKidFriendlyStatus(Bookmark bookmark) {
        int kidFriendlyStatus = bookmark.getKidFriendlyStatus().ordinal();
        long user_id = bookmark.getKidFriendlyMarkedBy().getId();

        String tableToUpdate = "Book";
        if (bookmark instanceof Movie) {
            tableToUpdate = "Movie";
        } else if (bookmark instanceof WebLink) {
            tableToUpdate = "WebLink";
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false", "root", "477905");
             Statement statement = connection.createStatement();) {

            String query = "update " + tableToUpdate + " set kid_friendly_status = " + kidFriendlyStatus + ", kid_friendly_marked_by = " + user_id + " where id = " + bookmark.getId();
            System.out.println("query (updateKidFriendlyStatus): " + query);
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void sharedByInfo(Bookmark bookmark) {
        long user_id = bookmark.getSharedBy().getId();
        String tableToUpdate = "Book";
        if (bookmark instanceof WebLink) {
            tableToUpdate = "WebLink";
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false", "root", "477905");
             Statement statement = connection.createStatement();) {

            String query = "update " + tableToUpdate + " set shared_by = " + user_id + ", kid_friendly_marked_by = " + user_id + " where id = " + bookmark.getId();
            System.out.println("query (updateKidFriendlyStatus): " + query);
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

	public static Collection<Bookmark> getBooks(boolean isBookmarked, long userId) {
		Collection<Bookmark> result = new ArrayList<>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false", "root", "root");
				Statement stmt = conn.createStatement();) {			
			
			String query = "";
			if (!isBookmarked) {
				query = "Select b.id, title, image_url, publication_year, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, " +
									"amazon_rating from Book b, Author a, Book_Author ba where b.id = ba.book_id and ba.author_id = a.id and " + 
									"b.id NOT IN (select ub.book_id from User u, User_Book ub where u.id = " + userId +
									" and u.id = ub.user_id) group by b.id";				
			} 
			else {
				query = "Select b.id, title, image_url, publication_year, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, " +
						"amazon_rating from Book b, Author a, Book_Author ba where b.id = ba.book_id and ba.author_id = a.id and " + 
						"b.id IN (select ub.book_id from User u, User_Book ub where u.id = " + userId +
						" and u.id = ub.user_id) group by b.id";
			}
			
			ResultSet rs = stmt.executeQuery(query);				
			
	    	while (rs.next()) {
	    		long id = rs.getLong("id");
				String title = rs.getString("title");
				String imageUrl = rs.getString("image_url");
				int publicationYear = rs.getInt("publication_year");
				//String publisher = rs.getString("name");		
				String[] authors = rs.getString("authors").split(",");			
				int genre_id = rs.getInt("book_genre_id");
				BookGenreType genre = BookGenreType.values()[genre_id];
				double amazonRating = rs.getDouble("amazon_rating");
				
				System.out.println("id: " + id + ", title: " + title + ", publication year: " + publicationYear + ", authors: " + String.join(", ", authors) + ", genre: " + genre + ", amazonRating: " + amazonRating);
	    		
	    		Bookmark bookmark = BookmarkManager.getInstance().createBook(id, title, imageUrl, publicationYear, null, authors, genre, amazonRating);
	    		result.add(bookmark); 
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public static Bookmark getBook(long bookId) {
		Book book = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false", "root", "root");
				Statement stmt = conn.createStatement();) {
			String query = "Select b.id, title, image_url, publication_year, p.name, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, amazon_rating, created_date"
					+ " from Book b, Publisher p, Author a, Book_Author ba "
					+ "where b.id = " + bookId + " and b.publisher_id = p.id and b.id = ba.book_id and ba.author_id = a.id group by b.id";
	    	ResultSet rs = stmt.executeQuery(query);
			
	    	while (rs.next()) {
	    		long id = rs.getLong("id");
				String title = rs.getString("title");
				String imageUrl = rs.getString("image_url");
				int publicationYear = rs.getInt("publication_year");
				String publisher = rs.getString("name");		
				String[] authors = rs.getString("authors").split(",");			
				int genre_id = rs.getInt("book_genre_id");
				BookGenreType genre = BookGenreType.values()[genre_id];
				double amazonRating = rs.getDouble("amazon_rating");
				
				System.out.println("id: " + id + ", title: " + title + ", publication year: " + publicationYear + ", publisher: " + publisher + ", authors: " + String.join(", ", authors) + ", genre: " + genre + ", amazonRating: " + amazonRating);
	    		
	    		book = BookmarkManager.getInstance().createBook(id, title, imageUrl, publicationYear, publisher, authors, genre, amazonRating/*, values[7]*/);
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return book;
	}
}
