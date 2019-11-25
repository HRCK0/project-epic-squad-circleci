package ca.uottawa.mali165.epicclinic;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.List;

public class BookingServicesListViewAdapter extends ArrayAdapter {

    private final Activity context;

    private final List<Service> serviceList;

    public BookingServicesListViewAdapter(Activity context, List<Service> serviceList) {
        super(context, R.layout.service_template, serviceList);
        this.context = context;
        this.serviceList = serviceList;

    }
}
