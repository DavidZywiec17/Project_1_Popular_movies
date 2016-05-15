package dzlast.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayList<Movie> arraylisttest = new ArrayList<Movie>();

    private MovieAdapter Adapter;

    private String sort_by = "popular";

    static final String save_sort = "sort";

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // Save the user's current game state
        outState.putString(save_sort, sort_by);
;
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            sort_by = savedInstanceState.getString(save_sort);
        } else {
            // Probably initialize members with default values for a new instance
        }

        UpdateMovieList(sort_by);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Adapter = new MovieAdapter(getActivity(),arraylisttest);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridlist = (GridView) rootView.findViewById(R.id.fragment_gridview);
        gridlist.setAdapter(Adapter);

        gridlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie current_movie = (Movie) parent.getAdapter().getItem(position);

                Context context = getActivity();

                Intent mIntent = new Intent(context,Movie_Detail.class);

                Bundle mBundle = new Bundle();

                mBundle.putParcelable("key", current_movie);

                mIntent.putExtras(mBundle);


                startActivity(mIntent);
            }
        });

        return rootView;
    }

    private void UpdateMovieList(String sort_type){

        FetchMovieList movieList = new FetchMovieList();

        if(sort_type == "popular") {
            sort_by = "popularity.desc";
        }
        else
        {
            sort_by = "vote_average.desc";
        }

        movieList.execute(sort_by);



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.popular_sort:
            {
                if (item.isChecked()) {
                    UpdateMovieList("popular");
                    sort_by = "popular";
                }
                else{
                    item.setChecked(true);
                    UpdateMovieList("popular");
                    sort_by = "popular";
                }
                return true;
            }

            case R.id.rating_sort:
                if (item.isChecked()) {
                   UpdateMovieList("top");
                    sort_by = "top";

                }
                else{
                    item.setChecked(true);
                    UpdateMovieList("top");
                    sort_by = "top";
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    class FetchMovieList extends AsyncTask<String, Void, String[][]> {

        private final String Log_Tag = FetchMovieList.class.getSimpleName();

        /* The date/time conversion code is going to be moved outside the asynctask later,
         * so for convenience we're breaking it out into its own method now.
         */
        private String[][] getDataFromJson(String JsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String OWM_IMAGE = "poster_path";
            final String OWM_TITLE = "original_title";
            final String OWM_OVERVIEW = "overview";
            final String OWM_VOTEAVG = "vote_average";
            final String OWM_RELEASE_DATE = "release_date";


            JSONObject Json = new JSONObject(JsonStr);
            JSONArray ResultArray = Json.getJSONArray(OWM_LIST);

            //First in list is going by popularity currently.

            String[][] resultStrs = new String[ResultArray.length()][5];
            for(int i = 0; i < ResultArray.length(); i++) {

                //Image and Title

                // Get the JSON object representing the day
                JSONObject movie = ResultArray.getJSONObject(i);

                // Title is a child of "Result"
                String title = movie.getString(OWM_TITLE);
                String image = movie.getString(OWM_IMAGE);
                String overview = movie.getString(OWM_OVERVIEW);
                Double rating = movie.getDouble(OWM_VOTEAVG);
                String release_string = movie.getString(OWM_RELEASE_DATE);

                /*SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
                try {
                    Date release_date = format.parse(release_string);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/


                resultStrs[i][0] = title;
                resultStrs[i][1] = image;
                resultStrs[i][2] = overview;
                resultStrs[i][3] = rating.toString();
                resultStrs[i][4] = release_string;
            }

            /*for (String s : resultStrs) {
                Log.v(Log_Tag, "Forecast entry: " + s);
            }*/
            return resultStrs;

        }

      protected String[][] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.

            BufferedReader reader = null;
            final String api_key = "";


            if(params.length == 0){
                Log.v("DZ_BUG","DOES NOT GET HERE");
                return null;

            }
            String sort = params[0];

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("sort_by", sort)
                    .appendQueryParameter("api_key", api_key);
            String myUrl = builder.build().toString();

          Log.v("DZ_BUG", myUrl);


            // Will contain the raw JSON response as a string.
            String JsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(myUrl);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    JsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    JsonStr = null;
                }
                JsonStr = buffer.toString();

                String[][] movie_data;
                try {
                    movie_data = getDataFromJson(JsonStr);
                    return movie_data;

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                JsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[][] strings) {

            //Put data into array of class movie
            ArrayList<Movie>  movies = new ArrayList<Movie>();;


            for (int i = 0; i < strings.length; i++) {

                String arraystr[] = new String[strings[i].length];
                for (int a = 0; a< strings[i].length; a++) {

                    arraystr[a] = strings[i][a];
                }
                Movie current_movie = new Movie(arraystr);

                Log.v(Log_Tag, "Movie Title: " + current_movie.movie_img);

                movies.add(current_movie);

            }

            Adapter.clear();
            Adapter.addAll(movies);
        }

    }


}
