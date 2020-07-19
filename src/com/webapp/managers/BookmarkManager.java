package com.webapp.managers;

import com.webapp.entities.Book;
import com.webapp.entities.Movie;
import com.webapp.entities.WebLink;

// Singleton pattern
public class BookmarkManager {
    private static BookmarkManager bookmarkManagerInstance = new BookmarkManager();

    // private constructor
    private BookmarkManager() {
    }

    public static BookmarkManager getInstance() {
        return bookmarkManagerInstance;
    }

    public Movie createMovie(long id,
                             String title,
                             int releaseYear,
                             String[] cast,
                             String[] directors,
                             String genre,
                             double imdbRating) {
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

    public Book createBook(long id,
                           String title,
                           int publicationYear,
                           String publisher,
                           String[] authors,
                           String genre,
                           double amazonRating) {
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

    public WebLink createWebLink(long id,
                                 String title,
                                 String url,
                                 String host) {
        WebLink webLink = new WebLink();
        webLink.setId(id);
        webLink.setTitle(title);
        webLink.setUrl(url);
        webLink.setHost(host);

        return webLink;
    }
}