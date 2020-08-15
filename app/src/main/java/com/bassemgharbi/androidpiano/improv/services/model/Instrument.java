package com.bassemgharbi.androidpiano.improv.services.model;

public class Instrument {
    private String name;
    private String imagePath;
    private String group;
    private int midiHexCode;

    public Instrument(String name, String imagePath, String group,int midiHexCode) {
        this.name = name;
        this.imagePath = imagePath;
        this.group = group;
        this.midiHexCode = midiHexCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getMidiHexCode() {
        return midiHexCode;
    }

    public void setMidiHexCode(int midiHexCode) {
        this.midiHexCode = midiHexCode;
    }
}
