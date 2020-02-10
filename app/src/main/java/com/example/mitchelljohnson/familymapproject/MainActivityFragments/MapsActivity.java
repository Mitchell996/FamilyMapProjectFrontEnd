package com.example.mitchelljohnson.familymapproject.MainActivityFragments;

import android.os.Bundle;
import com.example.mitchelljohnson.familymapproject.PersonActivity.personActivity;
import com.example.mitchelljohnson.familymapproject.R;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.event.event;
import com.example.mitchelljohnson.familymapproject.dataObjects.model;
import com.example.mitchelljohnson.familymapproject.dataObjects.person;
import com.example.mitchelljohnson.familymapproject.settingsActivity.SettingsModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.example.mitchelljohnson.familymapproject.settingsActivity.SettingsActivity;
import com.example.mitchelljohnson.familymapproject.FilterActivity;
import com.example.mitchelljohnson.familymapproject.SearchActivity.SearchActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MapsActivity extends Fragment implements OnMapReadyCallback  {

    private GoogleMap mMap;
    model m;
    SettingsModel settings;
    ArrayList<Polyline> polylines= new ArrayList<>();
    RelativeLayout personBox;
    TextView personInfo;
    TextView eventInfo;
    ImageView genderType;
    person personSelected;
    event eventSelected = null;
    private boolean restrictMothersSide = false;
    private boolean restrictFathersSide = false;
    private boolean restrictDad = false;
    private boolean restrictMom = false;
    private final String PERSON_EXTRA ="SELECTED_PERSON";
    private final String EVENT_SELECTED = "SELECTED_EVENT";

    public static MapsActivity newInstance() {
        MapsActivity fragment = new MapsActivity();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_family_map, container, false);
        try {
            Object o = getArguments().getSerializable(EVENT_SELECTED);
            Object p = getArguments().getSerializable(PERSON_EXTRA);
            if (o != null) {
                eventSelected = (event) o;
                personSelected = (person) p;
            }
            else {
                personSelected = null;
            }
        }
        catch(Exception e){}
        try{
            super.onCreate(savedInstanceState);
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager()
                            .findFragmentById(R.id.map);
            m = model.getInstance();
            settings = SettingsModel.getInstance();


            personBox = view.findViewById(R.id.personBox);
            personInfo = view.findViewById(R.id.mapPersonName);
            eventInfo = view.findViewById(R.id.mapPersonEvent);
            genderType = view.findViewById(R.id.mapGenderIcon);
            genderType.bringToFront();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    restrictFathersSide = !(m.isFatherSide());
                    restrictMothersSide = !(m.isMotherSide());
                    restrictMom = restrictMothersSide;
                    restrictDad = restrictFathersSide;
                    person toUse = m.getUser();
                    convertToMarkers(toUse);
                    m.clearAcceptedEvents();
                    decideMapType(settings.getMapType());
                }
            });

            genderType.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (personSelected != null) {
                        Intent intent = new Intent(getActivity(),  personActivity.class);
                        intent.putExtra(PERSON_EXTRA, personSelected);
                        getActivity().startActivity(intent);
                    }
                }

            });
            return view;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        convertToMarkers(m.getUser());
    }

    private void convertToMarkers(person user) {

        ArrayList<event> events = user.getLifeEvents();

        if (user.spouse != null && !user.spouse.gender.equals("m")) {
            if(!m.isGenderBanned(user.spouse)) {
                if(!restrictMothersSide) {
                    convertToMarkers(user.spouse);
                }
                else{
                    restrictMothersSide = false;
                }
            }
        }
        if (user.father != null) {
            if(!restrictFathersSide) {
                convertToMarkers(user.father);
            }
            else {
                if(!restrictMothersSide) {
                    restrictFathersSide = false;
                    restrictMothersSide = true;
                    convertToMarkers(user.father.spouse);
                }
            }
        }
        for (event even : events) {
            if (!this.filterCheckEvent(even, user)) {
                Marker marker = setMarkerColor(even, user);
                m.addToAcceptedEvents(even);
                marker.setTag(even.personID);

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                       LatLng coords = marker.getPosition();
                       String personID = (String)marker.getTag();
                       person toColor = m.findbyID(personID);
                       event selected = m.findEventInPerson(toColor, coords);
                       allLines(toColor, coords);
                       selectPerson(toColor, selected);
                        return false;

                    }
                });
            }
        }
        if(eventSelected != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(eventSelected.getCoords()));
            selectPerson(personSelected, eventSelected);
            allLines(personSelected, eventSelected.getCoords());
            eventSelected = null;
        }
    }
    public void selectPerson(person toColor, event selected){
        personSelected = toColor;
        personInfo.setText(toColor.firstName+ " " + toColor.lastName);
        eventInfo.setText(selected.eventType+": " + selected.city +", " + selected.country+"("+selected.getYear()+")");
        switch(toColor.gender){
            case "m":
                genderType.setImageResource(R.drawable.ic_temp_man);
                genderType.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.holo_blue_bright),
                        PorterDuff.Mode.MULTIPLY);
                break;
            case "f":
                genderType.setImageResource(R.drawable.ic_temp_woman);
                genderType.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light),
                        PorterDuff.Mode.MULTIPLY);
                break;
        }
    }
    public void allLines(person toColor, LatLng coords){
        ArrayList<event> events = locateUsed(toColor);
        if(!polylines.isEmpty())
        {
            for(Polyline line: polylines){
                line.remove();
            }
            polylines.clear();
        }
        if(settings.isLifeLines()) {
            ArrayList<LatLng> eventcoords = new ArrayList<>();
            for (event even : events) {
                eventcoords.add(even.getCoords());
            }
            PolylineOptions opts = new PolylineOptions().addAll(eventcoords).color(settings.getLifeColor()).width(5);
            polylines.add(mMap.addPolyline(opts));
        }
        if(settings.isFamilyLines()){
            generateFamilyLines(toColor, 10, coords);
        }
        if(settings.isSpouseLines()){
            if(toColor.spouse!= null){
                ArrayList<LatLng> spouseEvents = new ArrayList<>();
                event firstEvent = locateFirstUsed(toColor.spouse);
                //event userFirstEvent = locateFirstUsed(toColor);
                if(firstEvent != null) {
                    spouseEvents.add(firstEvent.getCoords());
                    spouseEvents.add(coords);
                    PolylineOptions opts = new PolylineOptions().addAll(spouseEvents).color(settings.getSpouseColor()).width(5);
                    polylines.add(mMap.addPolyline(opts));
                }
            }
        }
    }
    private event locateFirstUsed(person user){

        ArrayList<event> lifeEvents = user.getLifeEvents();
        for(event even : lifeEvents){
         if(!filterCheckEvent(even, user)){
            return even;
         }
        }
        return null;
    }
    private ArrayList<event> locateUsed(person user){
        ArrayList<event> lifeEvents = user.getLifeEvents();
        ArrayList<event> usedEvents = new ArrayList<>();
        for(event even : lifeEvents){
            if(!filterCheckEvent(even, user)){
                usedEvents.add(even);
            }
        }
        return usedEvents;


    }

    private boolean filterCheckEvent(event e, person p){

        if(m.inBannedEventTypes(e)||m.isGenderBanned(p)){
            return true;
        }
        return false;
    }

    private Marker setMarkerColor(event even, person user){
        Marker marker;
        if(even.eventType.equals("birth")) {
             marker = mMap.addMarker(new MarkerOptions().position(even.getCoords()).title(even.eventType + ":" + user.firstName + " " + user.lastName)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
        else if(even.eventType.equals("marriage")){
             marker = mMap.addMarker(new MarkerOptions().position(even.getCoords()).title(even.eventType + ":" + user.firstName + " " + user.lastName)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
        else if(even.eventType.equals("death")){
             marker = mMap.addMarker(new MarkerOptions().position(even.getCoords()).title(even.eventType + ":" + user.firstName + " " + user.lastName)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        else{
             marker = mMap.addMarker(new MarkerOptions().position(even.getCoords()).title(even.eventType + ":" + user.firstName + " " + user.lastName));
        }
        return marker;
    }

    public void decideMapType(String mapType){
        if(mapType.equals("normal")){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        else if(mapType.equals("Hybrid")){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        else if(mapType.equals("Satellite")){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else if(mapType.equals("Terrain")){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
    }
    public void generateFamilyLines(person user, int width, LatLng coords){
        event firstEvent = locateFirstUsed(user);
        if(user.father!= null && !m.isFilterMale()){
            if(!restrictDad) {
                ArrayList<LatLng> dadEvents = new ArrayList<>();
                person dad = user.father;
                event dadEvent = locateFirstUsed(dad);
                if (dadEvent != null) {
                    dadEvents.add(coords);
                    dadEvents.add(dadEvent.getCoords());
                    PolylineOptions opts = new PolylineOptions().addAll(dadEvents).color(settings.getFamilyColor()).width(width);
                    polylines.add(mMap.addPolyline(opts));
                    int walrus = width;
                    if (width > 2) {
                        walrus = width / 2;
                    }
                    generateFamilyLines(dad, walrus, dadEvent.getCoords());
                }
            }
            else{
                restrictDad = false;
            }
        }
        if(user.mother != null && !m.isFilterFemale()) {
            if (!restrictMom) {
                ArrayList<LatLng> momEvents = new ArrayList<>();
                person mom = user.mother;
                event momEvent = locateFirstUsed(mom);
                if (momEvent != null) {
                    momEvents.add(coords);
                    momEvents.add(momEvent.getCoords());
                    PolylineOptions opts = new PolylineOptions().addAll(momEvents).color(settings.getFamilyColor()).width(width);
                    polylines.add(mMap.addPolyline(opts));
                    if (width > 2) {
                        width = width / 2;
                    }
                    generateFamilyLines(mom, width, momEvent.getCoords());
                }
            }
        }
        else{
            restrictMom = false;
        }

    }
}
