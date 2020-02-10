package com.example.mitchelljohnson.familymapproject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.mitchelljohnson.familymapproject.MainActivityFragments.LoginFragment;
import com.example.mitchelljohnson.familymapproject.MainActivityFragments.MapsActivity;
import com.example.mitchelljohnson.familymapproject.dataObjects.person;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.event.event;

public class EventActivity  extends AppCompatActivity {

    private final String EVENT_SELECTED ="SELECTED_EVENT";
    private final String PERSON_EXTRA ="SELECTED_PERSON";
    protected void onCreate(Bundle savedInstanceState) {
        event even = (event)getIntent().getExtras().getSerializable(EVENT_SELECTED);
        person toSend = (person)getIntent().getExtras().getSerializable(PERSON_EXTRA);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        FragmentManager fm = this.getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EVENT_SELECTED, even);
        bundle.putSerializable(PERSON_EXTRA, toSend);


        MapsActivity mapsActivity = new MapsActivity();
        mapsActivity.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.eventFrameLayout, mapsActivity);
        fragmentTransaction.commit();
    }
}
