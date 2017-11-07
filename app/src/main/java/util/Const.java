package util;

import br.com.popularmovies.BuildConfig;

/**
 * Created by Fernando on 21/03/2017.
 */

public class Const {
    public interface Principal {
        public final String LOG = "movie_app_log";
        public final String STORED_MOVIES = "stored_movies";
    }

    public interface DownloadMovies {
        public final String API_KEY = BuildConfig.API_KEY;
        public final String MOVIE_POSTER_BASE = "http://image.tmdb.org/t/p/";
        public final String MOVIE_POSTER_SIZE = "w185";
        public final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        public final String SORT_BY = "sort_by";
        public final String KEY = "api_key";
    }
}
