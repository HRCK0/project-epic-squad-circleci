package ca.uottawa.mali165.epicclinic;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ServiceTemplate extends LinearLayout {

    private static final String TAG = "ServiceTemplate";

    private TextView serviceNameField;
    private TextView priceField;
    private TextView categoryField;


    ServiceTemplate(Context context){
        super(context);
    }

    public void init(){
        Log.d(TAG, "Hello");
        inflate(getContext(), R.layout.service_template, this);
        serviceNameField= (TextView) findViewById(R.id.serviceName);
        priceField= (TextView) findViewById(R.id.price);
        categoryField= (TextView) findViewById(R.id.category);

    }

    public void setServiceName(String serviceName){
         serviceNameField.setText(serviceName);
    }
    public void setPrice(String price){
        priceField.setText(price);
    }
    public void setCategory(String category){
        categoryField.setText(category);
    }
}
