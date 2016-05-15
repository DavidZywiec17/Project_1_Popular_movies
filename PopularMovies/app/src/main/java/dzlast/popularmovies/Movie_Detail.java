package dzlast.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Movie_Detail extends AppCompatActivity {

   private Movie current_movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_movie__detail);


       Intent intent = this.getIntent();

        if (intent != null) {

           current_movie = (Movie)getIntent().getParcelableExtra("key");

            TextView movie_title = (TextView) findViewById(R.id.movie_title_textview);
            movie_title.setText(current_movie.getTitle());

            ImageView movie_image = (ImageView) findViewById(R.id.movie_img);
            Picasso.with(this).load("http://image.tmdb.org/t/p/w500" + current_movie.movie_img).into(movie_image);

            TextView movie_description = (TextView) findViewById(R.id.movie_description);
            movie_description.setText(current_movie.getOverview());

            TextView movie_rating = (TextView) findViewById(R.id.movie_rating);
            movie_rating.setText("Rating: " + current_movie.getRatings());

            TextView movie_release_date = (TextView) findViewById(R.id.movie_release_date);
            movie_release_date.setText("Release Date: "+ current_movie.getRelease_string());

        }


       //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
