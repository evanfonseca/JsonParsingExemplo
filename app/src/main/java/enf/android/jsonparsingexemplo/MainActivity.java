package enf.android.jsonparsingexemplo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = (TextView) findViewById(R.id.tvJsonItem);

        Button btnHIT= (Button) findViewById(R.id.btnHIT);


        btnHIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               new JSONTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesDemoList.txt");


            }
        });

    }


    public class  JSONTask extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... params) {

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

                for (int i=0;i<tamanho;i++){

                    JSONObject finalObject = parentArray.getJSONObject(i);

                    String movieName = finalObject.getString("movie");
                    int    movieYear = finalObject.getInt("year");

                    resultado = resultado + movieName + " - "+movieYear +"\n";


                }


                //tvData.setText(r);
                return resultado;
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvData.setText(result);
        }
    }


}
