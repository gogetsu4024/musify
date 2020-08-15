package com.bassemgharbi.androidpiano.Instruments;

import android.content.Context;

import com.bassemgharbi.androidpiano.improv.services.model.Instrument;

import java.util.ArrayList;

public class Instruments {
    static final ArrayList<Instrument> instruments = new ArrayList<>();
    private static Instruments single_instance = null;
    private Context context;

    static {

        instruments.add(new Instrument("Acoustic Grand Piano","Piano","Piano",0));
        instruments.add(new Instrument("Violin","violin","Violin",40));
        instruments.add(new Instrument("French Horn","frenchhorn","Brass",60));
        instruments.add(new Instrument("Flute","flute","flute",73));
        instruments.add(new Instrument("Bright Acoustic Piano","Piano","Piano",1));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
    }



    public static Instruments getInstance()
    {
        if (single_instance == null)
            single_instance = new Instruments();

        return single_instance;
    }

    public static ArrayList<Instrument> getInstruments() {
        return instruments;
    }


}
