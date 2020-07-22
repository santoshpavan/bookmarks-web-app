package com.webapp.test.entities;

import com.webapp.constants.MovieGenreType;
import com.webapp.entities.Movie;
import com.webapp.managers.BookmarkManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void isKidFriendlyEligible() {
        boolean isKidFriendlyEligible;
        Movie movie;

        // test 1 -- thriller
        movie = BookmarkManager.getInstance().createMovie(3000, "Citizen Kane",1941, new String[] {"Orson Welles", "Joseph Cotten"}, new String[] {"Orson Welles"}, MovieGenreType.THRILLERS,8.5);
        isKidFriendlyEligible = movie.isKidFriendlyEligible();
        assertFalse(isKidFriendlyEligible, "Thriller Movie Type must return isKidFriendlyEligible() false");

        // test 2 -- foreign thriller
        movie = BookmarkManager.getInstance().createMovie(3000, "Citizen Kane",1941, new String[] {"Orson Welles", "Joseph Cotten"}, new String[] {"Orson Welles"}, MovieGenreType.FOREGIN_THRILLERS,8.5);
        isKidFriendlyEligible = movie.isKidFriendlyEligible();
        assertFalse(isKidFriendlyEligible, "Foreign Thriller Movie Type must return isKidFriendlyEligible() false");

        // test 3 -- horror
        movie = BookmarkManager.getInstance().createMovie(3000, "Citizen Kane",1941, new String[] {"Orson Welles", "Joseph Cotten"}, new String[] {"Orson Welles"}, MovieGenreType.HORROR,8.5);
        isKidFriendlyEligible = movie.isKidFriendlyEligible();
        assertFalse(isKidFriendlyEligible, "Horror Movie Type must return isKidFriendlyEligible() false");
    }
}