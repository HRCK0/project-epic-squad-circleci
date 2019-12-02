package ca.uottawa.mali165.epicclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@TargetApi(26)

public class BookAppointmentActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    CalendarView calendar;
    int myDate;
    int myYear;
    int myMonth;
    Employee clinic;
    Patient patient;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    Button button10;
    Button button11;
    Button button12;
    Button button13;
    Button button14;
    Button button15;
    Button button16;
    Button button17;
    Button button18;
    Button button19;
    Button button20;
    Button button21;
    Button button22;
    Button button23;
    Button button24;
    Button[] timeSlots;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String companyName;
    String patientId;
    String datePickedString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        calendar = findViewById(R.id.calendarView);
        //clinic = getIntent().getExtras().getParcelable("employee");
        //patient = getIntent().getExtras().getParcelable("patient");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        companyName = getIntent().getExtras().getString("companyName");
        patientId= getIntent().getStringExtra("CurrentUser_UID");
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        button12 = findViewById(R.id.button12);
        button13 = findViewById(R.id.button13);
        button14 = findViewById(R.id.button14);
        button15 = findViewById(R.id.button15);
        button16 = findViewById(R.id.button16);
        button17 = findViewById(R.id.button17);
        button18 = findViewById(R.id.button18);
        button19 = findViewById(R.id.button19);
        button20 = findViewById(R.id.button20);
        button21 = findViewById(R.id.button21);
        button22 = findViewById(R.id.button22);
        button23 = findViewById(R.id.button23);
        button24 = findViewById(R.id.button24);
        timeSlots = new Button[]{button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11, button12, button13, button14, button15, button16, button17, button18, button19, button20, button21, button22, button23, button24};

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int date) {

                myDate = date;
                myYear = year;
                myMonth = month + 1;
                datePickedString = date + "/" + myMonth + "/" + year;
                Log.d(TAG, datePickedString);
                Date datePicked;

                try {
                    datePicked = new SimpleDateFormat("dd/MM/yyyy").parse(datePickedString);
                    Date dateNow = new Date();
                    Log.d(TAG, datePicked.toString());
                    Log.d(TAG, dateNow.toString());
                    boolean b = datePicked.before(dateNow);
                    if (b == true)
                        Log.d(TAG, "hi");


                    if (datePicked.before(dateNow)) {
                        for (int i = 0; i < timeSlots.length; i++) {
                            timeSlots[i].setClickable(false); //date picked has already passed, cannot book appointments in the past
                            timeSlots[i].setBackgroundTintList(ColorStateList.valueOf((Color.GRAY)));
                        }
                    } else
                        determineUnclickable(datePickedString);
                } catch (ParseException e) {
                    Log.d(TAG, "parse Error");
                }
                // Now you can uses year, month date to sent
                //must get the time slots from the clinic based on free time slots --> first get availability for the employee that was passed

            }
        });
    }

    public void determineUnclickable(String date) {
        final Activity t = this;
        final String datePicked = date;

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


                                    for (int i = 0; i < timeSlots.length; i++) {
                                        timeSlots[i].setClickable(false);
                                        timeSlots[i].setBackgroundTintList(ColorStateList.valueOf((Color.GRAY)));
                                    }
                                } else {
                                    Map availabilityMap = (Map) clinicData.get("availability");
                                    Calendar c = Calendar.getInstance();

                                    try {
                                        Date datePickedDate = new SimpleDateFormat("dd/MM/yyyy").parse(datePicked);
                                        c.setTime(datePickedDate);
                                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK); //get day of week
                                        Log.d(TAG, String.valueOf(dayOfWeek));
                                        c.setTime(datePickedDate);
                                        LocalTime timeFrom = null;
                                        LocalTime timeTo = null;

                                        if (dayOfWeek == 1) {
                                            String sundayFrom = availabilityMap.get("SundayFrom").toString();
                                            String sundayTo = availabilityMap.get("SundayTo").toString();
                                            if (sundayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(sundayFrom);
                                                timeTo = LocalTime.parse(sundayTo);
                                            }

                                        } else if (dayOfWeek == 2) {
                                            String mondayFrom = availabilityMap.get("MondayFrom").toString();
                                            String mondayTo = availabilityMap.get("MondayTo").toString();
                                            if (mondayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(mondayFrom);
                                                timeTo = LocalTime.parse(mondayTo);
                                            }


                                        } else if (dayOfWeek == 3) {

                                            String tuesdayFrom = availabilityMap.get("TuesdayFrom").toString().trim();
                                            String tuesdayTo = availabilityMap.get("TuesdayTo").toString();
                                            if (tuesdayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(tuesdayFrom);
                                                timeTo = LocalTime.parse(tuesdayTo);
                                            }

                                        } else if (dayOfWeek == 4) {
                                            String wednesdayFrom = availabilityMap.get("WednesdayFrom").toString().trim();
                                            String wednesdayTo = availabilityMap.get("WednesdayTo").toString().trim();
                                            if (wednesdayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(wednesdayFrom);
                                                timeTo = LocalTime.parse(wednesdayTo);

                                            }

                                        } else if (dayOfWeek == 5) {
                                            String thursdayFrom = availabilityMap.get("ThursdayFrom").toString().trim();
                                            String thursdayTo = availabilityMap.get("ThursdayTo").toString().trim();
                                            if (thursdayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(thursdayFrom);
                                                timeTo = LocalTime.parse(thursdayTo);
                                            }


                                        } else if (dayOfWeek == 6) {
                                            String fridayFrom = availabilityMap.get("FridayFrom").toString();
                                            String fridayTo = availabilityMap.get("FridayTo").toString();
                                            if (fridayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(fridayFrom);
                                                timeTo = LocalTime.parse(fridayTo);
                                            }

                                        } else {
                                            String saturdayFrom = availabilityMap.get("SaturdayFrom").toString();
                                            String saturdayTo = availabilityMap.get("SaturdayTo").toString();
                                            if (saturdayFrom.equals("") == false) {
                                                timeFrom = LocalTime.parse(saturdayFrom);
                                                timeTo = LocalTime.parse(saturdayTo);
                                            }

                                        }
                                        if (timeFrom == null) {
                                            //clinic not open that day
                                            for (int i = 0; i < timeSlots.length; i++) {
                                                timeSlots[i].setClickable(false); //date picked has already passed, cannot book appointments in the past
                                                timeSlots[i].setBackgroundTintList(ColorStateList.valueOf((Color.GRAY)));
                                            }


                                        }
                                        if (timeFrom != null) {

                                            if (timeFrom.getMinute() > 0) {
                                                timeFrom = timeFrom.plusMinutes(60 - timeFrom.getMinute()); //rounds to nearest hour

                                            }

                                            if (timeTo.getMinute() > 0) {
                                                timeTo = timeTo.minusMinutes(timeFrom.getMinute());
                                            }


                                            int startHourClinic = timeFrom.getHour();
                                            int endHourClinic = timeTo.getHour();
                                            Log.d(TAG, String.valueOf(startHourClinic));
                                            Log.d(TAG, String.valueOf(endHourClinic));

                                            for (int i = 0; i < 24; i++) {
                                                int endTime = i + 1;
                                                if (i < startHourClinic) {

                                                    /*String a = i + ":00-" + endTime + ":00";
                                                    int idOfButton = getResources().getIdentifier(a, "id", getApplicationContext().getPackageName());
                                                    Button toDelete = findViewById(idOfButton);
                                                    toDelete.setClickable(false);
                                                    toDelete.setBackgroundColor(Color.parseColor("#5F6967"));*/
                                                    timeSlots[i].setClickable(false);
                                                    timeSlots[i].setBackgroundTintList(ColorStateList.valueOf((Color.GRAY)));


                                                } else if (i >= endHourClinic) {

                                                   /* String a = i + ":00-" + endTime + ":00";
                                                    Log.d(TAG,a);
                                                    int idOfButton = getResources().getIdentifier(a, "id", getApplicationContext().getPackageName());
                                                    Button toDelete = findViewById(idOfButton);
                                                    toDelete.setClickable(false);
                                                    toDelete.setBackgroundColor(Color.parseColor("#5F6967"));*/
                                                    timeSlots[i].setClickable(false);
                                                    timeSlots[i].setBackgroundTintList(ColorStateList.valueOf((Color.GRAY)));


                                                } else {
                                                    timeSlots[i].setClickable(true);
                                                    timeSlots[i].setBackgroundTintList(ColorStateList.valueOf(Color.MAGENTA));
                                                }
                                            }
                                        }
                                    } catch (ParseException e) {
                                        //add later
                                    }
                                }


                                if (!clinicData.containsKey("Appointments")) {
                                    return;
                                } else {
                                    Calendar c = Calendar.getInstance();
                                    try {
                                        Date datePickedDate = new SimpleDateFormat("dd/MM/yyyy").parse(datePicked);
                                        c.setTime(datePickedDate);
                                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK); //get day of week
                                        int month = c.get(Calendar.MONTH); //get the month
                                        int year = c.get(Calendar.YEAR);
                                        String startTime;
                                        String endTime;
                                        String aptDate;


                                        ArrayList<Map> bookedAppointments = (ArrayList<Map>) clinicData.get("Appointments");
                                        Map bookedAppointmentsMap = new HashMap();

                                        for (int i = 0; i < bookedAppointments.size(); i++) {

                                            bookedAppointmentsMap = bookedAppointments.get(i);
                                            aptDate = bookedAppointmentsMap.get("date").toString();

                                            if (aptDate.equals(datePicked)) {

                                                startTime = bookedAppointmentsMap.get("startTime").toString();

                                                String b = Character.toString(startTime.charAt(0)) + Character.toString(startTime.charAt(1));

                                                int j = Integer.parseInt(b);
                                                timeSlots[i].setClickable(false);
                                                //timeSlots[i].setBackgroundColor(Color.parseColor("#5F6967"));
                                                timeSlots[j].setBackgroundTintList(ColorStateList.valueOf((Color.GRAY)));
                                            }


                                        }
                                    } catch (ParseException e) {
                                        //add later
                                    }


                                }


                            }
                        }
                    }
                });
    }


    public void showToast(String textToShow) {
        Toast.makeText(BookAppointmentActivity.this, textToShow, Toast.LENGTH_SHORT).show();
    }

    public void updateDatabaseClinic(String timeAdded) {
        Log.d(TAG, "NO APPOINTMENTS YET!!!!!!!!!!!!!");
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




    public void updateDatabasePatient(String timeAdded) {

        final String time =timeAdded;
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





    public void buttonClicked(View view) {
        String text="";

        for(int i=0;i<timeSlots.length;i++)
        {
            if (view.getId() == timeSlots[i].getId())
            {
                text =timeSlots[i].getText().toString();
            }
        }

        String s = "Appointment booked from " + text + " with " + companyName;
        showToast( s);
        updateDatabaseClinic(text);
        updateDatabasePatient(text);




        //need to add to database -->!!!!
       /* if (view.getId() == R.id.button1) {
            text = button1.getText().toString();
        } else if (view.getId() == R.id.button2) {
            text = button2.getText().toString();
        } else if (view.getId() == R.id.button3) {
            //button3 action
        }

        else if (view.getId() == R.id.button4) {
            //button3 action
        }

        else if (view.getId() == R.id.button5) {
            //button3 action
        }
        else if (view.getId() == R.id.button6) {
            //button3 action
        }
        else if (view.getId() == R.id.button7) {
            //button3 action
        }
        else if (view.getId() == R.id.button8) {
            //button3 action
        }
        else if (view.getId() == R.id.button9) {{


        }
            //button3 action
        }
        else if (view.getId() == R.id.button10) {
            //button3 action
        }
        else if (view.getId() == R.id.button11) {
            //button3 action
        }
        else if (view.getId() == R.id.button12) {
            //button3 action
        }
        else if (view.getId() == R.id.button13) {
            //button3 action
        }*/


    }






}
