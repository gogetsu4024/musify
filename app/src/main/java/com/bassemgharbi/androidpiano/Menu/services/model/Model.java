package com.bassemgharbi.androidpiano.Menu.services.model;

import com.bassemgharbi.androidpiano.improv.services.model.Instrument;

public class Model {

    private int image;
    private String title;
    private String desc;
    private Instrument instrument;
    public Model(int image, String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
    }
    public Model(int image, String title, String desc, Instrument instrument) {
        this.image = image;
        this.title = title;
        this.desc = desc;
        instrument = instrument;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
