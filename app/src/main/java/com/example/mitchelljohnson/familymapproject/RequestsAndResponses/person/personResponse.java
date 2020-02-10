package com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person;
//package response;
import com.google.gson.Gson;
import com.example.mitchelljohnson.familymapproject.*;

//import model.*;
public class personResponse {

    public String descendant;
    public String personID;
    public String firstName;
    public String lastName;
    public String gender;
    public String father;
    public String mother;
    public String spouse;
    public String message;

    /**
     * constructor personResponse
     * puts all the data into a response
     *
     * @param desc   descendant
     * @param person personID
     * @param first  firstNAme
     * @param last   lastName
     * @param gen    gender
     * @param father father personID
     * @param mother mother personID
     * @param spouse spouse personID
     */
    public personResponse(String desc, String person, String first, String last, String gen, String father,
                          String mother, String spouse) {
        descendant = desc;
        personID = person;
        firstName = first;
        lastName = last;
        gender = gen;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public personResponse(String m) {
        message = m;
    }


    /**
     * returns all that good kush in string form
     *
     * @return all the vars in a string
     */
    public String convertToJson() {
        Gson a = new Gson();
        return a.toJson(this);
    }
}