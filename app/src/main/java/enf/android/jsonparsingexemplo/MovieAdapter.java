package enf.android.jsonparsingexemplo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
        tvYear.setText(movieList.get(position).getYear());
        tvDuration.setText(movieList.get(position).getDuration());
        tvDirector.setText(movieList.get(position).getDirector());

        rbMovieRating.setRating(movieList.get(position).getRating()/2);

        StringBuffer stringBuffer = new StringBuffer();
        for (Movie.Cast cast: movieList.get(position).getCastList())
            {
                stringBuffer.append(cast.getName()+" , ");
            }
        tvCast.setText(stringBuffer.toString());


        tvStory.setText(movieList.get(position).getStory());


        //ImageLoad


        return convertView;




    }
}
