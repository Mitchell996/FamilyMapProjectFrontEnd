package com.example.mitchelljohnson.familymapproject.RequestsAndResponses.Login;

import java.io.Serializable;
import com.google.gson.Gson;

public class loginRequest implements Serializable {

    private String password;
    private String userName;

    public loginRequest(String user, String pass){
        password = new String(pass);
        userName = new String(user);
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public String convertToJson(){
        Gson a = new Gson();
        String toReturn = a.toJson(this);
        return toReturn;
    }
   /* public String toString(){
        String toReturn = new String"{" +
                "userName" + getUserName() +
                "password"


    }*/
}
