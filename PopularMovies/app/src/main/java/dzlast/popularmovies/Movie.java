package dzlast.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dzlas_000 on 2/20/2016.
 */
public class Movie implements Parcelable {

    public String title;
    public String movie_img;
    public String overview;
    public String ratings;
    public String release_string;

    private String[] movie_info;

    public Movie(String[] resultStrs) {

        movie_info = resultStrs;

        this.title = resultStrs[0];
        this.movie_img = resultStrs[1];
        this.overview = resultStrs[2];
        this.ratings = resultStrs[3];
        this.release_string = resultStrs[4];
    }

    public String getMovie_img() {
        return movie_img;
    }

    public void setMovie_img(String movie_img) {
        this.movie_img = movie_img;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getRelease_string() {
        return release_string;
    }

    public void setRelease_string(String release_string) {
        this.release_string = release_string;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(movie_info);


    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {

            Movie cur_movie = new Movie(in.createStringArray());

            return cur_movie;
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


}
