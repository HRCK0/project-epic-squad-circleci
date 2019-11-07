package ca.uottawa.mali165.epicclinic;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class NewServiceDialog extends DialogFragment {

    public static final String TAG = "NewServiceDialog";

    EditText serviceNameEditText;
    EditText priceEditText;
    Spinner categorySpinner;

    FirebaseFirestore db;

    public interface NewServiceDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    NewServiceDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String serviceName = serviceNameEditText.getText().toString();
                        String price = priceEditText.getText().toString();

                        if (serviceName.isEmpty()){
                            serviceNameEditText.setError("Please enter a service name");
                            serviceNameEditText.requestFocus();
                        } else if (price.isEmpty()) {
                            priceEditText.setError("Please enter a price");
                            priceEditText.requestFocus();
                        } else {
                            // successful
                            db = FirebaseFirestore.getInstance();

                            HashMap service = new HashMap();

                            service.put("name", serviceName);
                            service.put("price", price);

                            db.collection("services")
                                    .document(serviceName).set(service)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Service Successfully Created");
                                            serviceNameEditText.setText("");
                                            priceEditText.setText("");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Service Not Created - Document Error", e);
                                        }
                                    });
                            listener.onDialogPositiveClick(NewServiceDialog.this);
                        }
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NewServiceDialog.this.getDialog().cancel();
                    }
                });

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.new_service_dialog, null));

        Dialog dialog = builder.create();

        serviceNameEditText = dialog.findViewById(R.id.newServiceNameEditText);
        priceEditText = dialog.findViewById(R.id.newServicePriceEditText);
        categorySpinner = dialog.findViewById(R.id.categorySpinner);

        return dialog;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (NewServiceDialogListener) context;
        } catch (ClassCastException e) {
            // activity doesn't implement the interface
            throw new ClassCastException(getActivity().toString() + " must implement NewServiceDialogListener");
        }
    }

    public HashMap getNewServiceData() {

        HashMap newService = new HashMap();
        newService.put("name", serviceNameEditText.getText().toString());
        newService.put("price", priceEditText.getText().toString());

        return newService;

    }
}
