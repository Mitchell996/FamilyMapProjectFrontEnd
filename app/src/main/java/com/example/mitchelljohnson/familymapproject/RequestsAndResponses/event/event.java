package com.example.mitchelljohnson.familymapproject.RequestsAndResponses.event;

import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;


public class event implements Comparable, Serializable {

            public String eventID;
            public String descendant;
            public String personID;
            public String latitude;
            public String longitude;
            public String country;
            public String city;
            public String eventType;
            public String year;
            //public boolean isUsed = false;

            public event(String ID, String desc, String pID, String lat, String lon, String count, String cit, String type, String y)
            {
                eventID = ID;
                descendant = desc;
                personID = pID;
                latitude = lat;
                longitude = lon;
                country = count;
                city = cit;
                eventType = type;
                year = y;
            }

            public LatLng getCoords(){
                double lat = Double.valueOf(latitude);
                double lon = Double.valueOf(longitude);
                LatLng toReturn = new LatLng(lat, lon);
                return toReturn;
            }
            public String getYear(){
                return year;
            }

    @Override
    public int compareTo( Object o) {

                if(!o.getClass().equals(this.getClass()))
                {
                    return 1;
                }
                event other = (event)o;
                double thisYear = Double.valueOf(year);
                double theirYear = Double.valueOf(other.getYear());
                if(thisYear < theirYear) {
                    return -1;
                }
                if(thisYear == theirYear){
                    return eventType.toLowerCase().compareTo(other.eventType.toLowerCase());

                }
                return 1;
    }
    public boolean equals(event e){
                if(eventID.equals(e.eventID)){
                    return true;
                }
                return false;
    }
}
