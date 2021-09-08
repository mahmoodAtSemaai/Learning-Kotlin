package com.webkul.mobikul.odoo.helper;

/**
 * Created by shubham.agarwal on 20/6/17.
 */

public class SearchHelper {
    @SuppressWarnings("unused")
    private static final String TAG = "SearchHelper";

    /**
     * @param word         The word on which search is performed
     * @param searchQuery  The term which is checked whether it is present in the word or not
     * @param indexOfMatch position of match found
     * @return word with the search query highlighted or bold
     */
    public static String getHighlightedString(String word, String searchQuery, int indexOfMatch) {
        return word.substring(0, indexOfMatch) + "<B>" + word.substring(indexOfMatch, indexOfMatch + searchQuery.length()) + "</B>"
                + word.substring(indexOfMatch + searchQuery.length(), word.length());

    }
}
