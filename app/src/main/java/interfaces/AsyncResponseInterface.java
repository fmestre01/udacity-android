package interfaces;

import java.util.List;

import model.Movie;

/**
 * Created by Fernando on 21/03/2017.
 */

public interface AsyncResponseInterface {
    void onTaskCompleted(List<Movie> results);
}
