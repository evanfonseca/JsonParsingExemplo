package enf.android.jsonparsingexemplo;

import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;

import enf.android.jsonparsingexemplo.models.Movie;

public class MainActivity extends AppCompatActivity {

    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tvData = (TextView) findViewById(R.id.tvJsonItem);
        Button btnHIT= (Button) findViewById(R.id.btnHIT);

        btnHIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new JSONTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesDemoList.txt");

                startActivity(new Intent(MainActivity.this,ShowMovieList.class));
            }
        });
    }



}
