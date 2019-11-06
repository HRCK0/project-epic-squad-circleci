package ca.uottawa.mali165.epicclinic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ServicesListViewAdapter extends ArrayAdapter {

    //to reference the Activity that the list view is on
    private final Activity context;

    private final Integer[] serviceIds;

    //to store the animal images
    private final String[] serviceNamesArray;

    //to store the list of countries
    private final String[] pricesArray;

    //to store the list of countries
    private final String[] categoriesArray;

    public ServicesListViewAdapter(Activity context, Integer[] serviceIdsParam, String[] serviceNamesArrayParam, String[] pricesArrayParam, String[] categoriesArrayParam){

        super(context,R.layout.service_template , serviceIdsParam); //secon param is referencing the xml of eah row in the list view
        this.context = context;
        this.categoriesArray=categoriesArrayParam;
        this.pricesArray=pricesArrayParam;
        this.serviceNamesArray=serviceNamesArrayParam;
        this.serviceIds=serviceIdsParam;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.service_template, null,true);

        //this code gets references to objects in the service_template.xml file
        TextView serviceNameTextField = (TextView) rowView.findViewById(R.id.serviceName);
        TextView priceTextField = (TextView) rowView.findViewById(R.id.price);
        TextView categoryTextField = (TextView) rowView.findViewById(R.id.category);

        //this code sets the values of the objects to values from the arrays
        serviceNameTextField.setText(serviceNamesArray[position]);
        priceTextField.setText(pricesArray[position]);
        categoryTextField.setText(categoriesArray[position]);

        return rowView;

    };
}
