package com.example.mitchelljohnson.familymapproject.RequestsAndResponses.register;

import com.google.gson.Gson;

import java.io.Serializable;

public class registerRequest implements Serializable {

        private String firstName;
        private String lastName;
        private String userName;
        private String password;
        private String gender;
        private String email;

        public registerRequest(String first, String last, String user, String pass, String g, String mail)
        {
            firstName =first;
            lastName = last;
            userName = user;
            password = pass;
            gender = g;
            email = mail;
        }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }
    public String convertToJson(){
        Gson a = new Gson();
        String toReturn = a.toJson(this);
        return toReturn;
    }
}
