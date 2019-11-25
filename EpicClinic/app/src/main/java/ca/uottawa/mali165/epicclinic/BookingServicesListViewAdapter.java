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

    private final List<Service> serviceList;

    public BookingServicesListViewAdapter(Activity context, List<Service> serviceList) {
        super(context, R.layout.service_template, serviceList);
        this.context = context;
        this.serviceList = serviceList;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.service_template_booking, null, true);

        TextView serviceNameTextField = (TextView) rowView.findViewById(R.id.serviceNameBooking);
        TextView priceTextField = (TextView) rowView.findViewById(R.id.priceBooking);
        TextView categoryTextField = (TextView) rowView.findViewById(R.id.categoryBooking);

        serviceNameTextField.setText(serviceList.get(position).getName());
        priceTextField.setText(serviceList.get(position).getPrice());
        categoryTextField.setText(serviceList.get(position).getCategory());

        return rowView;

    }
}
