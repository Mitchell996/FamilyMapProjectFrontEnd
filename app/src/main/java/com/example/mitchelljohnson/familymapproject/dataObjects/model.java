package com.example.mitchelljohnson.familymapproject.dataObjects;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.event.event;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.Login.loginRequest;
import com.example.mitchelljohnson.familymapproject.*;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Set;
import android.graphics.Color;
//model pretty much does holds everything created in the Dataset and will search through person for specific stuff.
//but it's actually extremely simple
public class model {    //I'll handle you later
    //singleton
    public static model model_instance = null;

    //people data
    protected person user;
    protected dataSet data;
    //filter stuff
    protected boolean filterMale = false;
    protected boolean filterFemale = false;
    protected boolean motherSide = true;
    protected boolean fatherSide = true;
    protected ArrayList<String> bannedEventTypes = new ArrayList<>();
    protected ArrayList<String> allEventTypes = new ArrayList<>();
    protected ArrayList<event> acceptedEvents = new ArrayList<>();
        //resync variables
    protected boolean loggedIn = false;
    protected loginRequest login;
    protected String serverHost;
    protected String serverPort;


    public boolean isInAcceptedEvents(event e) {
        return acceptedEvents.contains(e);
    }

    public void addToAcceptedEvents(event e){
        acceptedEvents.add(e);
    }

    public void clearAcceptedEvents(){
        acceptedEvents = new ArrayList<>();
    }

    public void setAllEventsTypes(ArrayList<String> newt){
        allEventTypes = newt;
    }
    public static model getInstance(){
        if(model_instance==null)
        {
            model_instance = new model();
        }
        return model_instance;
    }

    public boolean isFatherSide() {
        return fatherSide;
    }

    public boolean isMotherSide() {
        return motherSide;
    }

    public void setFatherSide(boolean fatherSide) {
        this.fatherSide = fatherSide;
    }

    public void setMotherSide(boolean motherSide) {
        this.motherSide = motherSide;
    }

    public void logout(){
        loggedIn = false;
    }

    public loginRequest getLogin(){
        return login;
    }

    public void setLogin(loginRequest lr){
        login = lr;
    }

    public String getServerHost(){
        return serverHost;
    }

    public void setServerHost(String server){
        serverHost = server;
    }

    public void setServerPort(String server){
        serverPort = server;
    }

    public String getServerPort(){
        return serverPort;
    }

    public void setPerson(person toUse){
        user = toUse;
    }

    public void setData(dataSet dat){
        data = dat;
        user = data.getOGPerson();
        allEventTypes = data.allEventTypes;
        loggedIn = true;
    }

    public ArrayList<String> getAllEventTypes() {
        return allEventTypes;
    }

    public ArrayList<String> getBannedEventTypes() {
        return bannedEventTypes;
    }

    public void setBannedEventTypes(Set<String> banned){
        bannedEventTypes = new ArrayList<>(banned);
    }

    public person getUser(){
        return user;
    }

    public boolean isFilterMale(){
        return filterMale;
    }

    public boolean isFilterFemale() {
        return filterFemale;
    }

    public void toggleMale(){
        if(filterMale == true){
            filterMale=false;
        }
        else {
            filterMale=true;
        }
    }

    public void toggleFemale(){
        if(filterFemale == true){
            filterFemale=false;
        }
        else {
            filterFemale = true;
        }
    }
    public event findEventInPerson(person Selected, LatLng coords){
        ArrayList<event> lifeEvents = Selected.getLifeEvents();
        for(event even : lifeEvents ){
            if(coords.equals(even.getCoords())){
                return even;
            }
        }
        return null;
    }
    public person findbyID(String personID){
        return personHunt(personID, user);
    }
    private person personHunt(String personID, person current){
        if(current.personID.equals(personID))
        {
            return current;
        }
        if(current.spouse != null) {
            if(!current.spouse.gender.equals("m")) {
                person spouseSide = personHunt(personID, current.spouse);
                if (spouseSide != null) {
                    return spouseSide;
                }
            }
        }
        if(current.father != null) {
            person fatherSide = personHunt(personID, current.father);
            if(fatherSide != null){
                return fatherSide;
            }
        }
            return null;
    }
    public boolean isLoggedIn(){
        return loggedIn;
    }

    public void addBannedEvent(String type){
        bannedEventTypes.add(type);
    }

    public void removeBannedEventType(String type){
        bannedEventTypes.remove(type);
    }
    public boolean inBannedEventTypes(event e){
        if(bannedEventTypes.contains(e.eventType))
        {
            return true;
        }
        return false;
    }
    public boolean inBannedEventTypes(String e){
        if(bannedEventTypes.contains(e))
        {
            return true;
        }
        return false;
    }
    public boolean isGenderBanned(person p){
        if(p == null){
            return true;
        }
        if( ( filterMale == true && p.gender.equals("m") )
                || ( filterFemale == true && p.gender.equals("f") ) )
        {
            return true;
        }
        return false;
    }
    public boolean onMothersSide(person toCheck){
        person mother = user.mother;
        return inFamilyTree(toCheck, mother);
    }
    public boolean onFathersSide(person toCheck){
        return inFamilyTree(toCheck, user.father);
    }
    private boolean inFamilyTree(person toCheck, person toUse){
        if(toUse.personID.equals(toCheck.personID)){
            return true;
        }
        if(toUse.mother != null){
            return inFamilyTree(toCheck, toUse.mother);
        }
        if(toUse.father != null){
            return inFamilyTree(toCheck, toUse.father);
        }
        return false;
    }

}
