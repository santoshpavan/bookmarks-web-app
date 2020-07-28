package com.webapp.entities;

import com.webapp.constants.BookGenreType;
import com.webapp.partner.Shareable;

import java.util.Arrays;

public class Book extends Bookmark implements Shareable {
    private int publicationYear;
    private String publisher;
    private String[] authors;
    private BookGenreType genre;
    private double amazonRating;

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public BookGenreType getGenre() {
        return genre;
    }

    public void setGenre(BookGenreType genre) {
        this.genre = genre;
    }

    public double getAmazonRating() {
        return amazonRating;
    }

    public void setAmazonRating(double amazonRating) {
        this.amazonRating = amazonRating;
    }

    @Override
    public String toString() {
        return "Book{" +
                "publicationYear=" + publicationYear +
                ", publisher='" + publisher + '\'' +
                ", authors=" + Arrays.toString(authors) +
                ", genre='" + genre + '\'' +
                ", amazonRating=" + amazonRating +
                '}';
    }

    @Override
    public boolean isKidFriendlyEligible() {
        if (getGenre().equals(BookGenreType.SELF_HELP) || getGenre().equals(BookGenreType.PHILOSOPHY)){
            return false;
        } else {
            return true;
        }
    }

    @Override
    // sharing data in XML format
    public String getItemData() {
        StringBuilder builder = new StringBuilder();
        builder.append("<item>");
            builder.append("<type>Book</type>");
            builder.append("<title>").append(getTitle()).append("</title>");
            builder.append("<publisher>").append(publisher).append("</publisher>");
            builder.append("<publicationYear>").append(publicationYear).append("</publicationYear>");
            builder.append("<genre>").append(genre).append("</genre>");
            builder.append("<amazonRating>").append(amazonRating).append("</amazonRating>");
        builder.append("</item>");

        return builder.toString();
    }
}
