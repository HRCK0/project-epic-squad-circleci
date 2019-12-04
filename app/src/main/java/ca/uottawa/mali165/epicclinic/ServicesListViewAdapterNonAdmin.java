package ca.uottawa.mali165.epicclinic;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;

public class ServicesListViewAdapterNonAdmin extends ArrayAdapter {

    private final Activity context;

    private final List<Service> serviceList;

    public ServicesListViewAdapterNonAdmin(Activity context, List<Service> serviceList){

        super(context,R.layout.services_template2 , serviceList); //secon param is referencing the xml of eah row in the list view
        this.context = context;
        this.serviceList=serviceList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.services_template2, null,true);

        //this code gets references to objects in the service_template1.xml file
        TextView serviceNameTextField = (TextView) rowView.findViewById(R.id.serviceName2);
        TextView priceTextField = (TextView) rowView.findViewById(R.id.price2);
        TextView categoryTextField = (TextView) rowView.findViewById(R.id.category2);

        //this code sets the values of the objects to values from the arrays
        serviceNameTextField.setText(serviceList.get(position).getName());
        priceTextField.setText(serviceList.get(position).getPrice());
        categoryTextField.setText(serviceList.get(position).getCategory());

        return rowView;

    };
}
