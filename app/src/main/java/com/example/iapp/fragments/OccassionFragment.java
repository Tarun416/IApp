package com.example.iapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.iapp.R;
import com.example.iapp.adapter.EventsAdapter;
import com.example.iapp.models.EventsName;
import com.example.iapp.models.Occassion;
import com.example.iapp.utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rahul on 02/04/17.
 */

public class OccassionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.imagePlaceholder)
    ImageView imagePlaceholder;
    @Bind(R.id.eventsRecView)
    RecyclerView eventsRecView;
    private DatabaseReference mDatabase;
    private Calendar calendar;
    private SharedPreferences preferences;
    private EventsAdapter mAdapter;

    public OccassionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_occassion, container, false);
        ButterKnife.bind(this, view);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getEventsFromFirebase();
        fab.setOnClickListener(this);
        return view;
    }

    private void getEventsFromFirebase() {
        CommonUtils.displayProgressDialog(getActivity(),"Fetching events");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        eventsRecView.setLayoutManager(linearLayoutManager);

        mDatabase.child("users").child(preferences.getString("accountId","")).child("occassions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CommonUtils.dismissProgressDialog();

                if(dataSnapshot.getChildrenCount()>0) {
                    imagePlaceholder.setVisibility(View.GONE);
                    eventsRecView.setVisibility(View.VISIBLE);

                    Log.d("count",dataSnapshot.getChildrenCount()+"");

                    Occassion eventName=new Occassion();
                    ArrayList<Occassion> occassion=new ArrayList<Occassion>();

                    for(DataSnapshot d: dataSnapshot.getChildren())
                    {
                        eventName = d.getValue(Occassion.class);
                        eventName.occassionName = d.getKey();
                        occassion.add(eventName);
                    }
                    mAdapter = new EventsAdapter(getActivity(), occassion);
                    eventsRecView.setAdapter(mAdapter);
                }
                else {
                    imagePlaceholder.setVisibility(View.VISIBLE);
                    eventsRecView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CommonUtils.dismissProgressDialog();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                invokeDialog();
                break;
            case R.id.radio_yes:
                if (calendarTimeButton.getVisibility() != View.VISIBLE) {
                    calendarTimeButton.setVisibility(View.VISIBLE);
                    calendarTimeButton.setText("Select Time");
                }
                break;
            case R.id.radio_no:
                calendarTimeButton.setVisibility(View.GONE);
                descriptionContainer.setVisibility(View.VISIBLE);
                break;
            case R.id.cancelButton:
                dialog.cancel();
                break;
            case R.id.saveButton:
                String occassionName;
                String time;
                String description1;
                Boolean isFriendInvited;
                if (otherOccassion.getVisibility() == View.VISIBLE)
                    occassionName = otherOccassion.getText().toString();
                else
                    occassionName = spinner.getSelectedItem().toString();


                if (calendarTimeButton.getVisibility() == View.VISIBLE) {
                    time = calendarTimeButton.getText().toString();
                    String []hour=time.split(":");
                    if(Integer.parseInt(hour[0].trim())<12 )
                        time= calendarTimeButton.getText().toString()+ " "+"AM";
                    else
                        time= calendarTimeButton.getText().toString()+ " "+"PM";
                }
                else
                    time = null;

                if (description.getText().toString().length() > 0)
                    description1 = description.getText().toString();
                else
                    description1 = null;

                if (radioYes.isChecked())
                    isFriendInvited = true;
                else
                    isFriendInvited = false;

                Occassion occassion = new Occassion(calendarButton.getText().toString(), isFriendInvited, time, description1);
                mDatabase.child("users").child(preferences.getString("accountId", "")).child("occassions").child(occassionName).setValue(occassion, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        getEventsFromFirebase();
                    }
                });
                dialog.cancel();
                break;
        }
    }

    private View alertLayout;
    private Spinner spinner;
    private EditText otherOccassion;
    private Button calendarButton;
    private DatePickerDialog dpd;
    private TimePickerDialog tpd;
    private String month;
    private LinearLayout inviteRel;
    private RadioButton radioYes;
    private RadioButton radioNo;
    private Button calendarTimeButton;
    private LinearLayout descriptionContainer;
    private EditText description;
    private Button cancelButton;
    private Button saveButton;
    private AlertDialog dialog;

    private void invokeDialog() {
        CommonUtils.displayProgressDialog(getActivity(), "Fetching Occassions");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.dialog_occassion, null);
        spinner = (Spinner) alertLayout.findViewById(R.id.spinner);
        otherOccassion = (EditText) alertLayout.findViewById(R.id.other);
        calendarButton = (Button) alertLayout.findViewById(R.id.calendarButton);
        inviteRel = (LinearLayout) alertLayout.findViewById(R.id.inviteRel);
        radioYes = (RadioButton) alertLayout.findViewById(R.id.radio_yes);
        calendarTimeButton = (Button) alertLayout.findViewById(R.id.calendarTimeButton);
        description = (EditText) alertLayout.findViewById(R.id.description);
        radioNo = (RadioButton) alertLayout.findViewById(R.id.radio_no);
        descriptionContainer = (LinearLayout) alertLayout.findViewById(R.id.descriptionContainer);
        cancelButton = (Button) alertLayout.findViewById(R.id.cancelButton);
        saveButton = (Button) alertLayout.findViewById(R.id.saveButton);
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        spinner.setOnItemSelectedListener(this);
        calendarTimeButton.setOnClickListener(this);
        radioYes.setOnClickListener(this);
        radioNo.setOnClickListener(this);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                       @Override
                                                       public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                                           if (monthOfYear == 0)
                                                               month = "January";
                                                           else if (monthOfYear == 1)
                                                               month = "February";
                                                           else if (monthOfYear == 2)
                                                               month = "March";
                                                           else if (monthOfYear == 3)
                                                               month = "April";
                                                           else if (monthOfYear == 4)
                                                               month = "May";
                                                           else if (monthOfYear == 5)
                                                               month = "June";
                                                           else if (monthOfYear == 6)
                                                               month = "July";
                                                           else if (monthOfYear == 7)
                                                               month = "August";
                                                           else if (monthOfYear == 8)
                                                               month = "September";
                                                           else if (monthOfYear == 9)
                                                               month = "October";
                                                           else if (monthOfYear == 10)
                                                               month = "November";
                                                           else if (monthOfYear == 11)
                                                               month = "December";

                                                           calendarButton.setText(dayOfMonth + " " + month + " " + year);
                                                           inviteRel.setVisibility(View.VISIBLE);
                                                           if (radioYes.isChecked()) {
                                                               calendarTimeButton.setVisibility(View.VISIBLE);
                                                               calendarTimeButton.setText("Select Time");
                                                           }

                                                       }
                                                   }, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dpd.show(getActivity().getFragmentManager(), "TAG");

            }

        });


        calendarTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();

                tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        calendarTimeButton.setText(hourOfDay + " : " + minute);
                        descriptionContainer.setVisibility(View.VISIBLE);

                    }
                }, 12, 10, 0, true);

                tpd.show(getActivity().getFragmentManager(), "TimePicker");
            }
        });


        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    description.setHint("Description (optional)");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        otherOccassion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0)
                    calendarButton.setVisibility(View.GONE);
                else
                    calendarButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        final List<String> categories = new ArrayList<>();
        categories.add("Select Event");

        mDatabase.child("occassions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CommonUtils.dismissProgressDialog();
                Log.d("inside", dataSnapshot.getChildren().toString());

                for (DataSnapshot occassionSnapShot : dataSnapshot.getChildren()) {
                    String value = occassionSnapShot.getValue(String.class);
                    categories.add(value);
                }

                categories.add("Other");
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);
                AlertDialog.Builder alert = new AlertDialog.Builder(alertLayout.getContext());
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(true);
                dialog = alert.create();
                dialog.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CommonUtils.dismissProgressDialog();
                Log.d("ERROR", databaseError.getMessage() + " " + databaseError.getDetails());

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        calendarButton.setText("Select Date");
        if (adapterView.getSelectedItemPosition() == adapterView.getCount() - 1) {
            otherOccassion.setVisibility(View.VISIBLE);
            calendarTimeButton.setVisibility(View.VISIBLE);
        } else {
            otherOccassion.setVisibility(View.GONE);
            calendarTimeButton.setVisibility(View.GONE);
            descriptionContainer.setVisibility(View.GONE);

        }

        if (adapterView.getSelectedItemPosition() == 0 || adapterView.getSelectedItemPosition() == adapterView.getCount() - 1) {
            calendarButton.setVisibility(View.GONE);
            inviteRel.setVisibility(View.GONE);
            calendarTimeButton.setVisibility(View.GONE);
            descriptionContainer.setVisibility(View.GONE);
        } else {
            calendarButton.setVisibility(View.VISIBLE);

            if (!calendarButton.getText().toString().equalsIgnoreCase("select date")) {
                inviteRel.setVisibility(View.VISIBLE);
            } else {
                inviteRel.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}