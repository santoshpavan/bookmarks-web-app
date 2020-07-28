package com.webapp.managers;

import com.webapp.constants.BookGenreType;
import com.webapp.constants.KidFriendlyStatus;
import com.webapp.constants.MovieGenreType;
import com.webapp.dao.BookmarkDao;
import com.webapp.entities.*;
import com.webapp.util.HttpConnect;
import com.webapp.util.IOUtil;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

// Singleton pattern
public class BookmarkManager {
    private static BookmarkManager bookmarkManagerInstance = new BookmarkManager();
    // MVC Policy => Manager/Model communicating with DAO
    private static BookmarkDao bookmarkDao = new BookmarkDao();

    // private constructor
    private BookmarkManager() {
    }

    public static BookmarkManager getInstance() {
        return bookmarkManagerInstance;
    }

    public Movie createMovie(long id, String title, int releaseYear, String[] cast,
                             String[] directors, MovieGenreType genre, double imdbRating) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setReleaseYear(releaseYear);
        movie.setCast(cast);
        movie.setDirectors(directors);
        movie.setGenre(genre);
        movie.setImdbRating(imdbRating);

        return movie;
    }

    public Book createBook(long id, String title, int publicationYear, String publisher,
                           String[] authors, BookGenreType genre, double amazonRating) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setPublicationYear(publicationYear);
        book.setPublisher(publisher);
        book.setAuthors(authors);
        book.setGenre(genre);
        book.setAmazonRating(amazonRating);

        return book;
    }

    public WebLink createWebLink(long id, String title, String url, String host) {
        WebLink webLink = new WebLink();
        webLink.setId(id);
        webLink.setTitle(title);
        webLink.setUrl(url);
        webLink.setHost(host);

        return webLink;
    }

    public List<List<Bookmark>> getBookmarks() {
        return bookmarkDao.getBookMarks();
    }

    public void saveUserBookMark(User user, Bookmark bookmark) {
        UserBookmark userBookmark = new UserBookmark();
        userBookmark.setUser(user);
        userBookmark.setBookmark(bookmark);

        if (bookmark instanceof WebLink) {
            try {
                String url = ((WebLink)bookmark).getUrl();
                if (!url.endsWith(".pdf")) {
                    String webpage = HttpConnect.download(((WebLink)bookmark).getUrl());
                    if (webpage != null) {//when the status is not >=200 and <300
                        IOUtil.write(webpage, bookmark.getId());
                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        bookmarkDao.saveUserBookmark(userBookmark);
    }

    public void setKidFriendlyStatus(User user, KidFriendlyStatus kidFriendlyStatus, Bookmark bookmark) {
        bookmark.setKidFriendlyStatus(kidFriendlyStatus);
        bookmark.setKidFriendlyMarkedBy(user);
        System.out.println("Kid friendly status: " + kidFriendlyStatus + ", marked by " + user.getEmail() + " -> " + bookmark);
    }

    public void share(User user, Bookmark bookmark) {
        bookmark.setSharedBy(user);

        System.out.println("Data to be shared: ");
        if (bookmark instanceof Book) {
            System.out.println(((Book) bookmark).getItemData());
        } else if (bookmark instanceof WebLink) {
            System.out.println(((WebLink) bookmark).getItemData());
        }
    }
}