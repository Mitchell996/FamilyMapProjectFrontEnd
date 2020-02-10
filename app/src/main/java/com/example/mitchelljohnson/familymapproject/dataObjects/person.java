package com.example.mitchelljohnson.familymapproject.dataObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.event.event;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.personResponse;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.personResponse;


public class person implements Serializable {

    public person descendant;
    public String personID;
    public String firstName;
    public String lastName;
    public String gender;
    public person father;
    public person mother;
    public person spouse;
    public person child;
    public TreeSet<event> lifeEvents;


    public person(personResponse r){
        personID = r.personID;
        firstName = r.firstName;
        lastName = r.lastName;
        gender = r.gender;
        lifeEvents = new TreeSet<event>();
    }
    public person(String pID, String first, String last, String g)
    {
        personID = pID;
        firstName = first;
        lastName = last;
        gender = g;
        lifeEvents = new TreeSet<>();
    }
    public void setFather(person dad)
    {
        father = dad;
    }
    public void setSpouse(person wife)
    {
        spouse = wife;
    }
    public void addEvent(event e){
        lifeEvents.add(e);
    }

    public ArrayList<event> getLifeEvents(){
        return new ArrayList<event>(lifeEvents);
    }
}
