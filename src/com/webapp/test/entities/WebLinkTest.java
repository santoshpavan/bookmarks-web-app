package com.webapp.test.entities;

import com.webapp.entities.WebLink;
import com.webapp.managers.BookmarkManager;

import static org.junit.jupiter.api.Assertions.*;

class WebLinkTest {

    @org.junit.jupiter.api.Test
    void isKidFriendlyEligible() {
        WebLink webLink;
        boolean isKidFriendlyEligible;
        // test 1 -- adult in host
        webLink = BookmarkManager.getInstance().createWebLink(2000, "Taming Tiger Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html", "http://www.adultjavaworld.com");
        isKidFriendlyEligible = webLink.isKidFriendlyEligible();
        assertFalse(isKidFriendlyEligible, "For adult in host - isKidFriendlyEligible() must return false");
        // test 2 -- adult in url but not in host
        webLink = BookmarkManager.getInstance().createWebLink(2000, "Taming Tiger Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-adult-tiger--part-2.html", "http://www.javaworld.com");
        isKidFriendlyEligible = webLink.isKidFriendlyEligible();
        assertFalse(isKidFriendlyEligible, "For adult in url - isKidFriendlyEligible() must return false");
        // test 3 -- adult in title only
        webLink = BookmarkManager.getInstance().createWebLink(2000, "Taming Adult Tiger Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html", "http://www.javaworld.com");
        isKidFriendlyEligible = webLink.isKidFriendlyEligible();
        assertFalse(isKidFriendlyEligible, "For adult in host - isKidFriendlyEligible() must return false");
    }
}