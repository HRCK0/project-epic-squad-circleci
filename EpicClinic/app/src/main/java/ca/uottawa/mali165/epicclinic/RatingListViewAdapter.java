package ca.uottawa.mali165.epicclinic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class RatingListViewAdapter extends ArrayAdapter {

    private final Activity context;

    private final List<Rating> ratingList;

    public RatingListViewAdapter(Activity context, List<Rating> ratingList){

        super(context,R.layout.rating_template , ratingList); //secon param is referencing the xml of eah row in the list view
        this.context = context;
        this.ratingList=ratingList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.rating_template, null,true);

        //this code gets references to objects in the service_template1.xml file
        TextView raterNameTextField = (TextView) rowView.findViewById(R.id.raterName);
        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBar2);
        TextView commentTextField = (TextView) rowView.findViewById(R.id.comment);

        //this code sets the values of the objects to values from the arrays
        raterNameTextField.setText(ratingList.get(position).getRaterName());
        ratingBar.setRating(ratingList.get(position).getRating());
        commentTextField.setText(ratingList.get(position).getComment());

        return rowView;

    };
}
