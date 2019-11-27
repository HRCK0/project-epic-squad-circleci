package ca.uottawa.mali165.epicclinic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BookingServicesListViewAdapter extends ArrayAdapter {

    private final Activity context;

    private final List<String> companies;
    private final List<String> addresses;
    private final List<String> ratings;

    public BookingServicesListViewAdapter(Activity context, List<String> companies, List<String> addresses, List<String> ratings) {
        super(context, R.layout.service_template_booking, companies);
        this.context = context;
        this.companies = companies;
        this.addresses = addresses;
        this.ratings = ratings;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.service_template_booking, null, true);

        TextView companyNameTextField = (TextView) rowView.findViewById(R.id.companyName);
        TextView addressTextField = (TextView) rowView.findViewById(R.id.address);
        TextView ratingTextField = (TextView) rowView.findViewById(R.id.rating);

        companyNameTextField.setText(companies.get(position));
        addressTextField.setText(addresses.get(position));
        ratingTextField.setText(ratings.get(position));

        return rowView;

    }
}
