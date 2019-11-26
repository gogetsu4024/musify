package com.bassemgharbi.androidpiano.Instruments;

import java.util.ArrayList;

public class Instruments {
    static final ArrayList<Instrument> instruments = new ArrayList<>();
    static {
        instruments.add(new Instrument("Acoustic Grand Piano","grandpiano","piano",0));
        instruments.add(new Instrument("Violin","violin","Violin",40));
        instruments.add(new Instrument("French Horn","frenchhorn","Brass",60));
        instruments.add(new Instrument("Flute","flute","flute",73));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
    }

    public static ArrayList<Instrument> getInstruments() {
        return instruments;
    }
}
