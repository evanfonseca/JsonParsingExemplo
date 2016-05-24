package enf.android.jsonparsingexemplo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import enf.android.jsonparsingexemplo.models.Movie;

/**
 * Created by enfonseca on 24/05/16.
 */
public class MovieAdapter  extends ArrayAdapter{
    private List<Movie> movieList;
    private int resource;
    private LayoutInflater inflater;

    public MovieAdapter(Context context, int resource, List<Movie> objects) {
        super(context, resource, objects);
        movieList=objects;
        this.resource=resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){

            convertView = inflater.inflate(R.layout.row,null);
        }

        ImageView ivMovIcon;


        return super.getView(position, convertView, parent);




    }
}
