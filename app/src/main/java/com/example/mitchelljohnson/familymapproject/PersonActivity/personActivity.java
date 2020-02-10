package com.example.mitchelljohnson.familymapproject.PersonActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.example.mitchelljohnson.familymapproject.R;
import com.example.mitchelljohnson.familymapproject.dataObjects.*;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.event.*;
import com.example.mitchelljohnson.familymapproject.EventActivity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class personActivity extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    person currentPerson;
    private final String PERSON_EXTRA ="SELECTED_PERSON";
    private final String EVENT_SELECTED = "SELECTED_EVENT";
    model m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity);

            currentPerson = (person)getIntent().getExtras().getSerializable(PERSON_EXTRA);


        m = model.getInstance();
        TextView firstName = findViewById(R.id.personFirstName);
        TextView lastName = findViewById(R.id.personLastName);
        TextView gender = findViewById(R.id.personGender);

        firstName.setText(currentPerson.firstName);
        lastName.setText(currentPerson.lastName);
        String genderText = "";
        switch(currentPerson.gender) {
            case "m":
                genderText = "Male";
                break;
            case "f":
                genderText = "Female";
                break;
        }
        gender.setText(genderText);

        expListView = (ExpandableListView) findViewById(R.id.personExpandable);
        getListData();
        listAdapter = new expandableListAdapterPerson(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 0) {   //goes to eventActivity...... which I haven't done yet
                    Intent intent = new Intent(personActivity.this, EventActivity.class);

                    String eventString = parent.getItemAtPosition(childPosition+1).toString();
                    event even = StringCompare(eventString);
                    intent.putExtra(EVENT_SELECTED, even);
                    intent.putExtra(PERSON_EXTRA, currentPerson);
                    startActivity(intent);
                } else {
                    String personString = parent.getItemAtPosition(childPosition + 2).toString().toLowerCase();
                    Intent intent = new Intent(personActivity.this, personActivity.class);
                    if (personString.contains("mother")) {
                        intent.putExtra(PERSON_EXTRA, currentPerson.mother);
                    } else if (personString.contains("father")) {
                        intent.putExtra(PERSON_EXTRA, currentPerson.father);
                    } else if (personString.contains("spouse")) {
                        intent.putExtra(PERSON_EXTRA, currentPerson.spouse);
                    }
                    else if (personString.contains("child")) {
                        intent.putExtra(PERSON_EXTRA, currentPerson.child);
                    }
                    else {  //if the other list is expanded, it select something out of the first group.  this should solve that issue
                        //the tutorial went over how to listen when stuff is collapsed, but f that shiz.
                         personString = parent.getItemAtPosition(childPosition + listAdapter.getChildrenCount(0) + 2).toString().toLowerCase();
                        if (personString.contains("mother")) {
                            intent.putExtra(PERSON_EXTRA, currentPerson.mother);
                        } else if (personString.contains("father")) {
                            intent.putExtra(PERSON_EXTRA, currentPerson.father);
                        } else if (personString.contains("spouse")) {
                            intent.putExtra(PERSON_EXTRA, currentPerson.spouse);
                        }
                        else if (personString.contains("child")) {
                            intent.putExtra(PERSON_EXTRA, currentPerson.child);
                        }
                    }
                    startActivity(intent);
                }
                return false;
            }
        });
    }


    private void getListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        person Spouse = currentPerson.spouse;
                if(m.isGenderBanned(Spouse)||(!m.isMotherSide()&&m.onMothersSide(Spouse))||(!m.isFatherSide()&&m.onFathersSide(Spouse))){
                    Spouse = null;
                }
        person father = currentPerson.father;
        if(m.isGenderBanned(father)||(!m.isMotherSide()&&m.onMothersSide(father))||(!m.isFatherSide()&&m.onFathersSide(father))){
            father = null;
        }
        person mother = currentPerson.mother;
        if(m.isGenderBanned(mother)||(!m.isMotherSide()&&m.onMothersSide(mother))||(!m.isFatherSide()&&m.onFathersSide(mother))){
            mother = null;
        }
        person child = currentPerson.child;
        if(m.isGenderBanned(child)||(!m.isMotherSide()&&m.onMothersSide(child))||(!m.isFatherSide()&&m.onFathersSide(child))){
            child = null;
        }
        listDataHeader.add("Events");
        listDataHeader.add("Family");

        // Adding child data
        List<String> lifeEvents = new ArrayList<String>();
        for (event even : currentPerson.getLifeEvents()) {
            if(!m.inBannedEventTypes(even)) {
                lifeEvents.add(even.eventType + ": " + even.city + ", " + even.country + "(" + even.getYear() + ")");
            }
        }

        List<String> family = new ArrayList<String>();
        if (Spouse != null) {
            family.add("Spouse : " + Spouse.firstName + " " + Spouse.lastName );
        }
        if (father != null) {
            family.add("Father : " + father.firstName + " " + father.lastName);
        }
        if (mother != null) {
            family.add("Mother : " + mother.firstName + " " + mother.lastName);
        }
        if(child != null){
            family.add("child : " + child.firstName +" " + child.lastName);
        }

        if (lifeEvents.size() != 0) {
            listDataChild.put(listDataHeader.get(0), lifeEvents); // Header, Child data
        }
        if (family.size() != 0) {
            listDataChild.put(listDataHeader.get(1), family);
        }
    }

    private event StringCompare(String toCompare){
        for(event even : currentPerson.getLifeEvents()){
                String toCheck = even.eventType + ": " + even.city + ", " + even.country + "(" + even.getYear() + ")";
                if (toCheck.equals(toCompare)) {
                    return even;
            }
        }
        return null;
    }
}
