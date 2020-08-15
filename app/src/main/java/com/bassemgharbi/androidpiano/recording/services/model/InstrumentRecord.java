package com.bassemgharbi.androidpiano.recording.services.model;

import com.bassemgharbi.androidpiano.improv.services.model.Instrument;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.meta.Tempo;

public class InstrumentRecord {

    private int image;
    private MidiTrack midiTrack;
    private Tempo tempo;
    private int resolution;
    private String path;

    public InstrumentRecord() {

    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    private Instrument instrument;
    public InstrumentRecord(int image, MidiTrack midiTrack, Tempo tempo) {
        this.image = image;
        this.midiTrack = midiTrack;
        this.tempo = tempo;
    }
    public InstrumentRecord(int image, MidiTrack midiTrack, Tempo tempo, Instrument instrument,int resolution,String path) {
        this.image = image;
        this.midiTrack = midiTrack;
        this.tempo = tempo;
        this.instrument = instrument;
        this.resolution = resolution;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public MidiTrack getMidiTrack() {
        return midiTrack;
    }

    public void setMidiTrack(MidiTrack midiTrack) {
        this.midiTrack = midiTrack;
    }

    public Tempo getTempo() {
        return tempo;
    }

    public void setTempo(Tempo tempo) {
        this.tempo = tempo;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }
}
