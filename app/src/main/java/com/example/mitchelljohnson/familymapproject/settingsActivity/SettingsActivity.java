package com.example.mitchelljohnson.familymapproject.settingsActivity;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Switch;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Toast;

import com.example.mitchelljohnson.familymapproject.MainActivityFragments.MainActivity;
import com.example.mitchelljohnson.familymapproject.R;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.Login.loginRequest;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.personRequest;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.personResponse;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.personsResponse;

import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.register.registerResponse;
import com.example.mitchelljohnson.familymapproject.SearchActivity.SearchActivity;
import com.example.mitchelljohnson.familymapproject.dataObjects.dataSet;
import com.example.mitchelljohnson.familymapproject.dataObjects.model;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.event.eventSet;
import com.example.mitchelljohnson.familymapproject.proxyServer;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;


public class SettingsActivity  extends AppCompatActivity {

    model m;
    Set<String> selected = new TreeSet<>();
    Switch lifeLines;
    Switch spouseLines;
    Switch familyLines;
    Spinner lifeSpinner;
    Spinner spouseSpinner;
    Spinner familySpinner;
    Spinner mapStyle;
    TextView resync;
    TextView logout;
    Context context;
    SettingsModel settings;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        m= model.getInstance();
        settings = SettingsModel.getInstance();
        context = getApplicationContext();
        lifeSpinner = findViewById(R.id.lifeSpinner);
        lifeLines = findViewById(R.id.lifeSwitch);

        spouseSpinner = findViewById(R.id.spouseSpinner);
        spouseLines = findViewById(R.id.spouseSwitch);

        familyLines = findViewById(R.id.familySwitch);
        familySpinner = findViewById(R.id.familySpinner);

        mapStyle = findViewById(R.id.mapDropdown);

        final String[] items = new String[]{"Red", "Blue", "Green"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spouseSpinner.setAdapter(adapter);
        spouseSpinner.setSelection(adapter.getPosition(convertFromColor(settings.getSpouseColor())));
        final String familyItems[] = new String[]{"Blue", "Red", "Green"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, familyItems);
        familySpinner.setAdapter(adapter);
        familySpinner.setSelection(adapter.getPosition(convertFromColor(settings.getFamilyColor())));
        final String lifeItems[] = new String[]{"Green", "Red", "Blue"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lifeItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lifeSpinner.setAdapter(adapter);
        lifeSpinner.setSelection(adapter.getPosition(convertFromColor(settings.getLifeColor())));
         final String mapItems[] = new String[]{ "normal", "Satellite", "Hybrid", "Terrain" };
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mapItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapStyle.setAdapter(adapter);
        mapStyle.setSelection(adapter.getPosition(settings.getMapType()));

        lifeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                settings.setLifeColor(convertToColor(lifeItems[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                settings.setLifeColor(convertToColor(lifeItems[0]));
            }

        });

        familySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                settings.setFamilyColor(convertToColor(familyItems[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                settings.setFamilyColor(convertToColor(familyItems[0]));
            }

        });

        spouseSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                settings.setSpouseColor(convertToColor(items[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                settings.setSpouseColor(convertToColor(items[0]));
            }

        });


        mapStyle.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                settings.setMapType(mapItems[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                settings.setMapType(mapItems[0]);
            }

        });
        spouseLines.setChecked(settings.isSpouseLines());
        spouseLines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    settings.setSpouseLines(!settings.isSpouseLines());
            }
        });
        lifeLines.setChecked(settings.isLifeLines());
        lifeLines.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                settings.setLifeLines(!settings.isLifeLines());
            }
        });
        familyLines.setChecked(settings.isFamilyLines());
        familyLines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setFamilyLines(!settings.isFamilyLines());
            }
        });

        resync = findViewById(R.id.resync);
        resync.setOnClickListener(new View.OnClickListener(){
                @Override
            public void onClick(View v){
                    Toast message = Toast.makeText(context, "resyncing... Please wait", Toast.LENGTH_LONG);
                    message.show();
                    ArrayList<String> clean = new ArrayList<>();
                    m.setAllEventsTypes(clean);
                    new asyncLogin().execute(m.getLogin());
                }
        });
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                m.logout();

                logout();
            }
        });
    }
    private void logout(){
        MainActivity.startTopActivity(SettingsActivity.this, true);
        //startActivity(new Intent(this, MainActivity.class));
    }

    private class asyncLogin extends AsyncTask<loginRequest, Void, personResponse> {

        String serverHost = m.getServerHost();
        String serverPort = m.getServerPort();
        personsResponse persons;
        eventSet events;

        @Override
        protected personResponse doInBackground(loginRequest... loginRequests) {
            registerResponse response = proxyServer.Login(serverHost, serverPort, loginRequests[0]);
            if(response == null){
                return null;
            }
            if((response.message != null)) {
                return new personResponse(response.message);
            }
            personRequest newR = new personRequest(response.personID, response.authToken);
            personResponse toReturn = proxyServer.getPerson(serverHost, serverPort, newR);
            persons = proxyServer.getPersons(serverHost, serverPort, newR);
            events = proxyServer.getEvents(serverHost, serverPort, newR);
            dataSet data = new dataSet(events, persons, toReturn);
            model m = model.getInstance();
            m.setData(data);
            return toReturn;
        }


        @Override
        protected void onPostExecute(personResponse response)
        {
            if((response.message != null)) {
                Toast message = Toast.makeText(context, response.message, Toast.LENGTH_LONG);
                message.show();
            }
            else if(!(response.firstName == null)){

                Toast message = Toast.makeText(context, "Welcome, "+ response.firstName + " "  + response.lastName, Toast.LENGTH_LONG);
                message.show();
                MainActivity.startTopActivity(SettingsActivity.this, true);
            }
        }

    }
    public int convertToColor(String color){
        if(color.equals("Red")){
            return Color.RED;
        }
        else if(color.equals("Blue")){
            return Color.BLUE;
        }
        else if(color.equals("Green")){
            return Color.GREEN;
        }
        return Color.GRAY;
    }
    public String convertFromColor(int color){
        if(color == Color.RED){
            return "Red";
        }
        else if(color == Color.BLUE){
            return "Blue";
        }
        else if(color == Color.GREEN){
            return "Green";
        }
        return null;
    }


}
