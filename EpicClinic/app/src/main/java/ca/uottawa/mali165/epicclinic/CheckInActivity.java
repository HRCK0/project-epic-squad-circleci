package ca.uottawa.mali165.epicclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@TargetApi (26)
public class CheckInActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String companyName;
    String patientId;
    Button bookAppointment;
    TextView infoText;
    TextView waitTime;
    final String TAG = "CheckInActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        companyName = getIntent().getExtras().getString("companyName");
        patientId= getIntent().getStringExtra("CurrentUser_UID");
        bookAppointment = findViewById(R.id.bookAppointment);
        bookAppointment.setVisibility(View.GONE); //hides the book appointment until nessecary
        infoText = findViewById(R.id.infoText);
        waitTime = findViewById(R.id.waitTimeText);
        waitTime.setVisibility(View.GONE);
        Date dateNow = new Date(); //current date
        String date = dateNow.toString();
        db = FirebaseFirestore.getInstance();
        checkIfClinicOpen(date);



    }

    public void checkIfClinicOpen(String date)
    {

        final Activity t = this;
        final String datePicked = date; //today
        Log.d(TAG,datePicked);

        // currently looks for the name of the company. will switch to email once i figure out how to find that object first
        // testing with hardcoded email
        db.collection("users").whereEqualTo("Name of Company", companyName).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot QuerySnapshot) {
                        if (QuerySnapshot.isEmpty()) {
                            Log.d("QUERY SNAPSHOT", "no documents found");
                        } else {
                            for (DocumentSnapshot document : QuerySnapshot) {
                                Log.d("SEARCHING FOR DOCUMENT", document.getId() + " => " + document.getData());
                                final Map clinicData = document.getData();


                                if (!clinicData.containsKey("availability")) {

                                   infoText.setText("Clinic Not Open. Please Considering Booking an Appointment");
                                   bookAppointment.setVisibility(View.VISIBLE);



                                } else {
                                    Map availabilityMap = (Map) clinicData.get("availability");

                                    Log.d(TAG,"Contians Key");


                                        SimpleDateFormat weekdayFormat = new SimpleDateFormat("E");
                                        String dayOfWeek = weekdayFormat.format(new Date());
                                        Log.d(TAG,dayOfWeek);
                                        LocalTime timeFrom = null;
                                        LocalTime timeTo = null;
                                        Log.d(TAG,"Contians got to try");

                                        if (dayOfWeek.equals("Sun")) {
                                            String sundayFrom = availabilityMap.get("SundayFrom").toString();
                                            String sundayTo = availabilityMap.get("SundayTo").toString();
                                            if (sundayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(sundayFrom);
                                                timeTo = LocalTime.parse(sundayTo);
                                            }
                                            else {

                                                infoText.setText("Clinic Not Open. Please Considering Booking an Appointment");
                                                bookAppointment.setVisibility(View.VISIBLE);

                                            }

                                        } else if (dayOfWeek.equals("Mon")) {
                                            String mondayFrom = availabilityMap.get("MondayFrom").toString();
                                            String mondayTo = availabilityMap.get("MondayTo").toString();
                                            if (mondayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(mondayFrom);
                                                timeTo = LocalTime.parse(mondayTo);
                                            }
                                            else {

                                                infoText.setText("Clinic Not Open. Please Considering Booking an Appointment");
                                                bookAppointment.setVisibility(View.VISIBLE);

                                            }


                                        } else if (dayOfWeek.equals("Tue")) {

                                            String tuesdayFrom = availabilityMap.get("TuesdayFrom").toString().trim();
                                            String tuesdayTo = availabilityMap.get("TuesdayTo").toString();
                                            if (tuesdayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(tuesdayFrom);
                                                timeTo = LocalTime.parse(tuesdayTo);
                                            }
                                            else {

                                                infoText.setText("Clinic Not Open. Please Considering Booking an Appointment");
                                                bookAppointment.setVisibility(View.VISIBLE);

                                            }

                                        } else if (dayOfWeek.equals("Mon")) {
                                            String wednesdayFrom = availabilityMap.get("WednesdayFrom").toString().trim();
                                            String wednesdayTo = availabilityMap.get("WednesdayTo").toString().trim();
                                            if (wednesdayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(wednesdayFrom);
                                                timeTo = LocalTime.parse(wednesdayTo);

                                            }
                                            else {

                                                infoText.setText("Clinic Not Open. Please Considering Booking an Appointment");
                                                bookAppointment.setVisibility(View.VISIBLE);

                                            }

                                        } else if (dayOfWeek.equals("Thurs")) {
                                            String thursdayFrom = availabilityMap.get("ThursdayFrom").toString().trim();
                                            String thursdayTo = availabilityMap.get("ThursdayTo").toString().trim();
                                            if (thursdayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(thursdayFrom);
                                                timeTo = LocalTime.parse(thursdayTo);
                                            }
                                            else {

                                                infoText.setText("Clinic Not Open. Please Considering Booking an Appointment");
                                                bookAppointment.setVisibility(View.VISIBLE);

                                            }


                                        } else if (dayOfWeek.equals("Fri")) {
                                            String fridayFrom = availabilityMap.get("FridayFrom").toString();
                                            String fridayTo = availabilityMap.get("FridayTo").toString();
                                            if (fridayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(fridayFrom);
                                                timeTo = LocalTime.parse(fridayTo);
                                            }
                                            else {

                                                infoText.setText("Clinic Not Open. Please Considering Booking an Appointment");
                                                bookAppointment.setVisibility(View.VISIBLE);

                                            }

                                        } else {
                                            String saturdayFrom = availabilityMap.get("SaturdayFrom").toString();
                                            String saturdayTo = availabilityMap.get("SaturdayTo").toString();
                                            if (saturdayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(saturdayFrom);
                                                timeTo = LocalTime.parse(saturdayTo);
                                            }
                                            else {

                                                infoText.setText("Clinic Not Open. Please Considering Booking an Appointment");
                                                bookAppointment.setVisibility(View.VISIBLE);

                                            }

                                        }

                                        if (timeFrom != null) {
                                            Log.d(TAG,"got a date Key");
                                            if (timeFrom.getMinute() > 0) {
                                                timeFrom = timeFrom.plusMinutes(60 - timeFrom.getMinute()); //rounds to nearest hour

                                            }

                                            if (timeTo.getMinute() > 0) {
                                                timeTo = timeTo.minusMinutes(timeFrom.getMinute());
                                            }

                                            LocalTime timeNow = LocalTime.now();


                                            int startHourClinic = timeFrom.getHour();
                                            int endHourClinic = timeTo.getHour();
                                            if(timeNow.compareTo(timeTo)==1)
                                            {
                                                //means clinic is closed.
                                                infoText.setText("Clinic Has Closed. Please Considering Booking an Appointment");
                                                bookAppointment.setVisibility(View.VISIBLE);

                                            }
                                            if( timeNow.compareTo(timeFrom)==1)
                                            {
                                                timeNow = timeNow.plusMinutes(60 - timeNow.getMinute()); //rounds to nearest hour
                                                startHourClinic=timeNow.getHour();
                                            }
                                            Log.d(TAG, String.valueOf(startHourClinic));
                                            Log.d(TAG, String.valueOf(endHourClinic));
                                            boolean isOpen=true;
                                            for(int i=startHourClinic; i<endHourClinic;i++) {
                                                isOpen=true;
                                                String aptStartTime;
                                                String aptEndTime;
                                                int endTime = i+1;
                                                if(i<9) {
                                                    aptStartTime ="0" + i + ":00"; //start time of first appointment
                                                    aptEndTime = "0" +  endTime + ":00"; //endtime of first appointment

                                                }
                                                if(i==9) {
                                                    aptStartTime ="0" + i + ":00"; //start time of first appointment
                                                    aptEndTime =  endTime + ":00"; //endtime of first appointment
                                                }
                                                else {
                                                    aptStartTime = + i + ":00"; //start time of first appointment
                                                    aptEndTime =  +  endTime + ":00"; //endtime of first appointment
                                                }


                                                //check if Appointment is Booked


                                                if (!clinicData.containsKey("Appointments")) {
                                                    isOpen=false;
                                                } else {


                                                        String startTime;
                                                        String aptDate;



                                                        ArrayList<Map> bookedAppointments = (ArrayList<Map>) clinicData.get("Appointments");
                                                        Map bookedAppointmentsMap = new HashMap();

                                                        for (int j = 0; j < bookedAppointments.size(); j++) {

                                                            bookedAppointmentsMap = bookedAppointments.get(j);
                                                            aptDate = bookedAppointmentsMap.get("date").toString();
                                                            Log.d(TAG, "Booked Appointment Date: " + aptDate);

                                                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                                                String datePickedDate = dateFormat.format(new Date());
                                                                Log.d(TAG, "Today Date" + datePickedDate);
                                                            Log.d(TAG, "APT START TIME" + aptStartTime);

                                                                if (aptDate.equals(datePickedDate)) {

                                                                    startTime = bookedAppointmentsMap.get("startTime").toString();
                                                                    if (startTime.equals(aptStartTime)) {
                                                                        isOpen = false;
                                                                        break;
                                                                    }


                                                                }



                                                        }





                                                    }

                                                if(isOpen==true) {
                                                    infoText.setText("Appointment At " + companyName + " Booked From " + aptStartTime + " to " + aptEndTime);
                                                    waitTime.setVisibility(View.VISIBLE);
                                                    LocalTime time = LocalTime.now();
                                                    Log.d(TAG,"APT START TIME" + aptStartTime);

                                                    LocalTime timeOfApt = LocalTime.parse(aptStartTime);
                                                    LocalTime endOfApt = timeOfApt.plusMinutes(60);
                                                    Log.d(TAG, timeOfApt.toString());
                                                    Duration duration = Duration.between(time, timeOfApt);
                                                    waitTime.setText("Wait Time: " + duration.toMinutes() + " Minutes ");
                                                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                                        String dateOfAppointment = dateFormat.format(new Date());
                                                        Log.d(TAG, dateOfAppointment);
                                                        String timeOfAptString =  timeOfApt.toString();
                                                        String endOfAptString = endOfApt.toString();
                                                        String durationOfApt = timeOfAptString + "-" + endOfAptString;

                                                        Log.d(TAG, dateOfAppointment);
                                                        Log.d(TAG,"DURATION " + durationOfApt);
                                                        updateDatabaseClinic(durationOfApt,dateOfAppointment);
                                                        updateDatabasePatient(durationOfApt,dateOfAppointment);
                                                        break;

                                                    }




                                                }

                                            if(isOpen==false)
                                            {
                                                infoText.setText("No Appointments Available Today. Please Book an Appointment for Another Day");
                                                bookAppointment.setVisibility(View.VISIBLE);

                                            }




                                        }
                                    }

                                }




                            }
                        }

                });


    }


    public void updateDatabasePatient(String timeAdded, String datePicked) {

        final String time =timeAdded;
        final String  datePickedString=datePicked;
        Log.d(TAG, patientId);
        Log.d(TAG, "NO APPOINTMENTS YET!!!!!!!!!!!!!");
        db.collection("users")
                .document(patientId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "NO sadjiaodjaidso YET!!!!!!!!!!!!!");
                        Map patientInfo = documentSnapshot.getData();
                        String[] duration = time.split("-");

                        if (patientInfo.containsKey("Appointments")) {

                            //UPDATE PATIENT INFO
                            ArrayList<Map> currentAptInfo= (ArrayList)patientInfo.get("Appointments");


                            Map<String,String> appointmentInfo = new HashMap<String,String>();

                            appointmentInfo.put("StartTime",duration[0]);
                            appointmentInfo.put("EndTime",duration[1]);
                            appointmentInfo.put("Date",datePickedString);
                            appointmentInfo.put("Company",companyName);
                            currentAptInfo.add(appointmentInfo);
                            patientInfo.remove("Appointments");
                            patientInfo.put("Appointments",currentAptInfo);
                            db.collection("users").document(patientId).set(patientInfo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Appointments Updated Succesfully");
                                        }
                                    });


                        }
                        else {
                            Map<String, String> appointmentInfo = new HashMap<String, String>();
                            ArrayList<Map> infoApts = new ArrayList<>();
                            appointmentInfo.put("StartTime", duration[0]);
                            appointmentInfo.put("EndTime", duration[1]);
                            appointmentInfo.put("Date", datePickedString);
                            appointmentInfo.put("Company", companyName);
                            infoApts.add(appointmentInfo);
                            patientInfo.put("Appointments", infoApts);
                            db.collection("users").document(patientId).set(patientInfo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Appointments Updated Succesfully");
                                        }
                                    });



                        }}});
    }


    public void updateDatabaseClinic(String timeAdded, String datePicked) {
        Log.d(TAG, "NO APPOINTMENTS YET!!!!!!!!!!!!!");
        final String  datePickedString=datePicked;
        final String time =timeAdded;
        db.collection("users").whereEqualTo("Name of Company", companyName).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot QuerySnapshot) {
                        if (QuerySnapshot.isEmpty()) {
                            Log.d("QUERY SNAPSHOT", "no documents found");
                        } else {
                            for (DocumentSnapshot document : QuerySnapshot) {
                                Log.d("SEARCHING FOR DOCUMENT", document.getId() + " => " + document.getData());
                                String id = document.getId();
                                final Map clinicData = document.getData();


                                if (!clinicData.containsKey("Appointments")) {

                                    Log.d(TAG, "NO APPOINTMENTS YET!!!!!!!!!!!!!");
                                    Map<String,String> appointmentInfo = new HashMap<String,String>();
                                    String[] duration = time.split("-");
                                    ArrayList<Map> infoApts = new ArrayList<>();

                                    appointmentInfo.put("startTime",duration[0]);
                                    appointmentInfo.put("endTime",duration[1]);
                                    appointmentInfo.put("date",datePickedString);
                                    infoApts.add(appointmentInfo);
                                    clinicData.put("Appointments",infoApts);
                                    db.collection("users").document(id).set(clinicData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "Appointments Updated Succesfully");
                                                }
                                            });



                                    Log.d(TAG, " APPOINTMENTS ADDED!!!!!!!!!!!!!");

                                }


                                else
                                {
                                    //contains the key
                                    ArrayList<Map> currentAptInfo= (ArrayList)clinicData.get("Appointments");
                                    Log.d(TAG, " APPOINTMENTS AREW HERE !!!!!!!!!!!!!");
                                    Map<String,String> appointmentInfo = new HashMap<String,String>();
                                    String[] duration = time.split("-");
                                    appointmentInfo.put("startTime",duration[0]);
                                    appointmentInfo.put("endTime",duration[1]);
                                    appointmentInfo.put("date",datePickedString);
                                    currentAptInfo.add(appointmentInfo);
                                    clinicData.remove("Appointments");
                                    clinicData.put("Appointments",currentAptInfo);
                                    db.collection("users").document(id).set(clinicData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "Appointments Updated Succesfully");
                                                }
                                            });

                                }
                            }
                        }
                    }
                });
    }

    public void onClickBookAppointment (View bookAppointment)
    {
        companyName = getIntent().getExtras().getString("companyName");
        Intent openServicesWindow = new Intent(getApplicationContext(), BookAppointmentActivity.class);
        openServicesWindow.putExtra("companyName", companyName);
        openServicesWindow.putExtra("CurrentUser_UID", getIntent().getStringExtra("CurrentUser_UID"));
        startActivity(openServicesWindow);
    }



}
