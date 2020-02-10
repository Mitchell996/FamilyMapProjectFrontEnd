package com.example.mitchelljohnson.familymapproject.dataObjects;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.event.event;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.event.eventSet;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.personResponse;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.personsResponse;

import java.util.ArrayList;
import com.example.mitchelljohnson.familymapproject.*;
/**
 * the purpose of this class is to turn the personsResponse into a person tree that we can play with more.  It takes in an eventSet and personsResponse,
 * and turns that data into person objects that are connected to the user.
 */
public class dataSet {


    protected ArrayList<event> events;
    protected ArrayList<personResponse> persons;
    protected personResponse use;
    protected person user;
    protected ArrayList<String> allEventTypes = new ArrayList<>();

    public dataSet(eventSet e, personsResponse r, personResponse toUse){
        events = e.data;
        persons = r.data;
        use = toUse;
    }

    public person getOGPerson(){
        person OGperson = new person(use);

        user = OGperson;
        addRelations(user, use, false);

        return user;
    }

    private void addRelations(person toAdd, personResponse dataHolder, boolean isSpouse){
        person father = null;
        person spouse = null;
        String spouseID = dataHolder.spouse;
        String fatherID = dataHolder.father;

        personResponse dadData = null;
        personResponse wifeData = null;
        //add relations to parents and spouse
        for(personResponse r: persons){
            if(!isSpouse) { //if it isn't the spouse
                if (r.personID.equals(spouseID)) {  //add the spouse
                    spouse = new person(r);
                    wifeData = r;
                    spouse.setSpouse(toAdd); //spouse's spouse is person
                    toAdd.setSpouse(spouse);    //person's spouse is spouse
                }
            }
            else{   //if it is the spouse
                if(toAdd.spouse != null) {  //add mother to child and child to mother
                    toAdd.child = toAdd.spouse.child;   //spouse's child is person's child
                    if(toAdd.child != null) {
                        toAdd.child.mother = toAdd;  //spouse's child is person
                    }

                }
            }
            if(r.personID.equals(fatherID)){    //add father
                father = new person(r);
                dadData = r;
                father.child = toAdd;
                toAdd.setFather(father);
            }

        }
       // Add events to the persons life
        for(event e: events)    //add life events
        {
            if(!allEventTypes.contains(e.eventType.toLowerCase())){
                allEventTypes.add(e.eventType.toLowerCase());
            }

            if(e.personID.equals(toAdd.personID)){

                toAdd.addEvent(e);
                if(e.eventType.equals("marriage")){
                    System.out.println("RÈeeeeeeeee");//it reads in every marriage.  IT REACHES HERE BUT DOESN'T ADD TO THE FILE
                }
            }
        }
        if(father != null)
        {
            addRelations(father, dadData, false);
        }

        if(spouse != null && !isSpouse){
            addRelations(spouse, wifeData, true);
        }
    }
}
