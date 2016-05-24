package enf.android.jsonparsingexemplo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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

        url="http://jsonparsing.parseapp.com/jsonData/moviesData.txt";

        lvMovies = (ListView) findViewById(R.id.lvMovies);





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
            //tvData.setText(result);

            //Colocar aqui toda a especificação para trabalhar com a lista de filmes

        }
    }









}
