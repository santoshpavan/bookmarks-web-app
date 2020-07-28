package com.webapp;

import com.webapp.constants.KidFriendlyStatus;
import com.webapp.constants.UserType;
import com.webapp.controllers.BookmarkController;
import com.webapp.entities.Bookmark;
import com.webapp.entities.User;
import com.webapp.partner.Shareable;

import java.util.List;

public class View {

    public static void browse(User user, List<List<Bookmark>> bookmarks) {
        System.out.println("\n" + user.getEmail() + " is browsing.");

        for (List<Bookmark> bookmarkList: bookmarks) {
            for (Bookmark bookmark: bookmarkList) {
                if (getBookmarkDecision(bookmark)) {
                    BookmarkController.getInstance().saveUserBookmark(user, bookmark);
                    System.out.println("New item Bookmarked --" + bookmark);
                }

                // Marking as kid-friendly
                if (user.getUserType().equals(UserType.EDITOR) || user.getUserType().equals(UserType.CHIEF_EDITOR)) {
                    if (bookmark.isKidFriendlyEligible() && bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.UNKNOWN)) {
                        KidFriendlyStatus kidFriendlyStatus = getKidFriendlyStatusDecision(bookmark);

                        if (!kidFriendlyStatus.equals(KidFriendlyStatus.UNKNOWN)) {
                            BookmarkController.getInstance().setKidFriendlyStatus(user, kidFriendlyStatus, bookmark);
                        }
                    }
                }

                // Sharing
                if (bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.APPROVED) && bookmark instanceof Shareable) {
                    if (getShareDecision()) {
                        BookmarkController.getInstance().share(user, bookmark);
                    }
                }
            }
        }
    }

    private static boolean getShareDecision() {
        return Math.random() < 0.5 ? true : false;
    }

    private static boolean getBookmarkDecision(Bookmark bookmark) {
        return Math.random() < 0.5 ? true : false;
    }

    private static KidFriendlyStatus getKidFriendlyStatusDecision(Bookmark bookmark) {
        return Math.random() < 0.4 ?
                KidFriendlyStatus.APPROVED : (Math.random() >= 0.4 && Math.random() < 0.8) ?
                    KidFriendlyStatus.REJECTED : KidFriendlyStatus.UNKNOWN;
    }
}
