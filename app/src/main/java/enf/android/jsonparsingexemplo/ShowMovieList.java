package enf.android.jsonparsingexemplo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import enf.android.jsonparsingexemplo.models.Movie;

public class ShowMovieList extends AppCompatActivity {

    String url;
    ListView lvMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movie_list);

        // Create default options which will be used for every
//  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .defaultDisplayImageOptions(defaultOptions)
        .build();
        ImageLoader.getInstance().init(config); // Do it on Application start


        url="http://jsonparsing.parseapp.com/jsonData/moviesData.txt";

        lvMovies = (ListView) findViewById(R.id.lvMovies);


        new JSONTask().execute(url);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reflesh) {

            new JSONTask().execute(url);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class  JSONTask extends AsyncTask<String,String,List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader= null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();


                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer=new StringBuffer();
                String line="";

                while ((line=reader.readLine())!=null){
                    buffer.append(line);
                }

                String finalJSON=buffer.toString();
                JSONObject parentObject = new JSONObject(finalJSON);
                JSONArray parentArray = parentObject.getJSONArray("movies");
                int tamanho = parentArray.length();

                String resultado="";

                List<Movie> movieList = new ArrayList<>();

                for (int i=0;i<tamanho;i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Movie movie= new Movie();
                    movie.setMovie(finalObject.getString("movie"));
                    movie.setYear(finalObject.getInt("year"));
                    movie.setRating(finalObject.getInt("rating"));
                    movie.setDirector(finalObject.getString("director"));
                    movie.setDuration(finalObject.getString("duration"));
                    movie.setTagline(finalObject.getString("tagline"));
                    movie.setImage(finalObject.getString("image"));
                    movie.setStory(finalObject.getString("story"));

                    List<Movie.Cast> castList = new ArrayList<>();
                    for(int j = 0;j<finalObject.getJSONArray("cast").length();j++){

                        JSONObject castObject = finalObject.getJSONArray("cast").getJSONObject(j);

                        Movie.Cast cast = new Movie.Cast();

                        cast.setName(castObject.getString("name"));

                        castList.add(cast);
                    }

                    movie.setCastList(castList);

                    movieList.add(movie);

                }
                return movieList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null){
                    connection.disconnect();
                }

                try {
                    if(reader!=null){
                        reader.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            super.onPostExecute(result);

            MovieAdapter movieAdapter =new MovieAdapter(getApplicationContext(),R.layout.row,result);
            lvMovies.setAdapter(movieAdapter);

            /*
            StringBuilder stringBuilder=new StringBuilder();
            for (Movie m: result) {

                stringBuilder.append(m.getMovie()+"\n");

            }
            Toast.makeText(getApplicationContext(),""+stringBuilder,Toast.LENGTH_LONG).show();
            */
        }
    }

    public class MovieAdapter  extends ArrayAdapter {
        private List<Movie> movieList;
        private int resource;
        private LayoutInflater inflater;

        public MovieAdapter(Context context, int resource, List<Movie> objects) {
            super(context, resource, objects);
            movieList=objects;
            this.resource=resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){

                convertView = inflater.inflate(resource,null);
            }

            ImageView ivMovIcon;
            TextView tvMovie;
            TextView tvTagline;
            TextView tvYear;
            TextView tvDuration;
            TextView tvDirector;
            RatingBar rbMovieRating;
            TextView tvCast;
            TextView tvStory;
            final ProgressBar progressBar=(ProgressBar) convertView.findViewById(R.id.progressBar);

            ivMovIcon= (ImageView) convertView.findViewById(R.id.ivIcon);
            tvMovie= (TextView) convertView.findViewById(R.id.tvMovie);
            tvTagline= (TextView) convertView.findViewById(R.id.tvTagline);
            tvYear= (TextView) convertView.findViewById(R.id.tvYear);
            tvDuration= (TextView) convertView.findViewById(R.id.tvDuration);
            tvDirector= (TextView) convertView.findViewById(R.id.tvDirector);
            rbMovieRating= (RatingBar) convertView.findViewById(R.id.rbMovie);
            tvCast= (TextView) convertView.findViewById(R.id.tvCast);
            tvStory= (TextView) convertView.findViewById(R.id.tvStory);


            tvMovie.setText(movieList.get(position).getMovie());

            tvTagline.setText(movieList.get(position).getTagline());
            tvYear.setText("Year: "+movieList.get(position).getYear());
            //tvYear.setText(""+2016);
            tvDuration.setText("Duration: "+movieList.get(position).getDuration());
            tvDirector.setText("Director: "+movieList.get(position).getDirector());

            rbMovieRating.setRating(movieList.get(position).getRating()/2);


            StringBuffer stringBuffer = new StringBuffer();
            for (Movie.Cast cast: movieList.get(position).getCastList())
            {
                stringBuffer.append(cast.getName()+" , ");
            }

            tvCast.setText("Cast: "+stringBuffer.toString());
            tvStory.setText(movieList.get(position).getStory());

            //ImageLoad
            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(movieList.get(position).getImage(), ivMovIcon, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                }
            }); // Default options will be used


            return convertView;

        }
    }


}
