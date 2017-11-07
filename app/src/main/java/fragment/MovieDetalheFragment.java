package fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.popularmovies.R;
import model.Movie;

/**
 * Created by Fernando on 21/03/2017.
 */

public class MovieDetalheFragment extends Fragment {
    private Movie movie;

    public MovieDetalheFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("movies_details")) {
            movie = (Movie) intent.getParcelableExtra("movies_details");
            showInfo(rootView);
        }

        return rootView;
    }

    private void showInfo(View v) {
        TextView title = (TextView) v.findViewById(R.id.movie_title_view);
        ImageView poster = (ImageView) v.findViewById(R.id.poster_image_view);
        TextView releaseDate = (TextView) v.findViewById(R.id.release_date);
        TextView ratings = (TextView) v.findViewById(R.id.ratings_view);
        TextView overview = (TextView) v.findViewById(R.id.synopsis_view);

        title.setText(movie.getTitle());
        Picasso.with(getActivity()).load(movie.getPoster()).into(poster);
        releaseDate.setText(movie.getReleaseDate());
        ratings.setText(movie.getVoteAverage() + "/10");
        overview.setText(movie.getOverview());
    }
}