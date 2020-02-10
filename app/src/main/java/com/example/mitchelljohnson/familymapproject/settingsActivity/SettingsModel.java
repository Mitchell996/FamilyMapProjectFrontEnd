package com.example.mitchelljohnson.familymapproject.settingsActivity;

import android.graphics.Color;

public class SettingsModel {

    boolean spouseLines = true;
    boolean familyLines= true;
    boolean lifeLines=true;
    int spouseColor = Color.RED;
    //String spouseString = "Red";
    int lifeColor = Color.GREEN;
    //String lifeString = "Green";
    int familyColor = Color.BLUE;
    //String familyString = "Blue";
    String mapType = "normal";
    static SettingsModel model_instance;

    public static SettingsModel getInstance(){
        if(model_instance==null)
        {
            model_instance = new SettingsModel();
        }
        return model_instance;
    }

    public void setSpouseColor(int spouseColor) {
        this.spouseColor = spouseColor;
    }

    public int getSpouseColor() {
        return spouseColor;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public void setFamilyColor(int familyColor) {
        this.familyColor = familyColor;
    }

    public void setFamilyLines(boolean familyLines) {
        this.familyLines = familyLines;
    }

    public boolean isFamilyLines() {
        return familyLines;
    }

    public boolean isLifeLines() {
        return lifeLines;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public int getFamilyColor() {
        return familyColor;
    }

    public int getLifeColor() {
        return lifeColor;
    }

    public String getMapType() {
        return mapType;
    }

    public void setLifeColor(int lifeColor) {
        this.lifeColor = lifeColor;
    }

    public void setLifeLines(boolean lifeLines) {
        this.lifeLines = lifeLines;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }
}
