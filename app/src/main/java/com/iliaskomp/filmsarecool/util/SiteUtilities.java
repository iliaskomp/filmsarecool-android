package com.iliaskomp.filmsarecool.util;

import com.iliaskomp.filmsarecool.config.SiteConfig;
import com.iliaskomp.filmsarecool.models.film.FilmFullInfo;

/**
 * Created by IliasKomp on 18/10/17.
 */

public class SiteUtilities {
    public static String getWikiSearchUrl(FilmFullInfo film) {
        return SiteConfig.WIKI_SEARCH_URL + film.getTitle() + " film";
    }
}
