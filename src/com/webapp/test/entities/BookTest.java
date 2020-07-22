package com.webapp.test.entities;

import com.webapp.constants.BookGenreType;
import com.webapp.entities.Book;
import com.webapp.managers.BookmarkManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void isKidFriendlyEligible() {
        boolean isKidFriendlyEligible;
        Book book;

        // test 1
        book = BookmarkManager.getInstance().createBook(4000, "Walden",1854, "Wilder Publications", new String[]{"Henry David Thoreau"}, BookGenreType.PHILOSOPHY, 4.3);
        isKidFriendlyEligible = book.isKidFriendlyEligible();
        assertFalse(isKidFriendlyEligible, "A Philosophy book must return isKidFriendlyEligible() is False");

        // test 2
        book = BookmarkManager.getInstance().createBook(4000, "Walden",1854, "Wilder Publications", new String[]{"Henry David Thoreau"}, BookGenreType.SELF_HELP, 4.3);
        isKidFriendlyEligible = book.isKidFriendlyEligible();
        assertFalse(isKidFriendlyEligible, "A Self-Help book must return isKidFriendlyEligible() is False");
    }
}