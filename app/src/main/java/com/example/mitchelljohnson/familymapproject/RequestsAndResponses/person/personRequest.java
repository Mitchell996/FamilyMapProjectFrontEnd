package com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person;

import com.google.gson.Gson;

public class personRequest {

    public String personID;
    public String authToken;

    public personRequest(String personID, String authToken)
    {
        this.personID = personID;
        this.authToken = authToken;
    }
    public String convertToJson() {
        Gson a = new Gson();
        return a.toJson(personID);
    }

}
