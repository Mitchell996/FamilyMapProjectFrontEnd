package com.example.mitchelljohnson.familymapproject.SearchActivity;

public class searchObject {

    public Object tag;
    public String searchedLine;
    public String belongsTo;
    public String objectType;

    public searchObject(String searched, Object thing){
        searchedLine = searched;
        tag = thing;
        belongsTo = new String("");
        objectType = "person";
    }

    public searchObject(String searched, String belongs, Object thing){
        searchedLine = searched;
        belongsTo = belongs;
        tag = thing;
        objectType = "event";
    }




}
