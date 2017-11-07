package tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.popularmovies.BuildConfig;
import interfaces.AsyncResponseInterface;
import model.Movie;

import static util.Const.DownloadMovies.API_KEY;
import static util.Const.DownloadMovies.BASE_URL;
import static util.Const.DownloadMovies.KEY;
import static util.Const.DownloadMovies.MOVIE_POSTER_BASE;
import static util.Const.DownloadMovies.MOVIE_POSTER_SIZE;
import static util.Const.DownloadMovies.SORT_BY;
import static util.Const.Principal.LOG;

/**
 * Created by Fernando on 21/03/2017.
 */

public class DownloadMoviesTask extends AsyncTask<String, Void, List<Movie>> {

    public AsyncResponseInterface delegate;

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String moviesJsonStr = null;

    private static String orderVoteAverage = "vote_average.desc";
    private static String orderByPopularity = "popularity.desc";

    private String urlSortBy = "";

    public DownloadMoviesTask(AsyncResponseInterface delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        try {
            String sortBy = params[0];

            if (null != sortBy) {
                if (sortBy.equals(orderByPopularity)) {
                    urlSortBy = "/movie/popular?";
                } else if (sortBy.equals(orderVoteAverage)) {
                    urlSortBy = "/movie/top_rated?";
                }
            }

            URL url = new URL("http://api.themoviedb.org/3" + urlSortBy + "&api_key=" + BuildConfig.API_KEY);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            moviesJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG, "Error ", e);
            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG, "Error closing stream", e);
                }
            }
        }

        try {
            return extractData(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> results) {
        if (results != null) {
            delegate.onTaskCompleted(results);
        }
    }

    private String getYear(String date) {
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        final Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Integer.toString(cal.get(Calendar.YEAR));
    }

    private List<Movie> extractData(String moviesJsonStr) throws JSONException {

        final String ARRAY_OF_MOVIES = "results";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(ARRAY_OF_MOVIES);
        int moviesLength = moviesArray.length();
        List<Movie> movies = new ArrayList<Movie>();

        for (int i = 0; i < moviesLength; ++i) {

            JSONObject movie = moviesArray.getJSONObject(i);
            String title = movie.getString(ORIGINAL_TITLE);
            String poster = MOVIE_POSTER_BASE + MOVIE_POSTER_SIZE + movie.getString(POSTER_PATH);
            String overview = movie.getString(OVERVIEW);
            String voteAverage = movie.getString(VOTE_AVERAGE);
            String releaseDate = getYear(movie.getString(RELEASE_DATE));

            movies.add(new Movie(title, poster, overview, voteAverage, releaseDate));
        }

        return movies;

    }
}