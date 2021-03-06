package com.bassemgharbi.androidpiano;

import android.media.MediaPlayer;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import org.billthefarmer.mididriver.MidiDriver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MidiSoundController implements MidiDriver.OnMidiStartListener {



    protected MidiDriver midi;
    protected MediaPlayer player;
    private static final SparseArray<Integer> SOUND_MAP = new SparseArray<>();
    private static final SparseArray<Integer> POSITION_TO_NOTES = new SparseArray<>();
    private static final HashMap<String,Integer> POSITION_TO_NOTES2 = new HashMap<>();
    private int VOLUME = 60;
    private int currentInstrument=0;
    public int getVOLUME() {
        return VOLUME;
    }

    public void setVOLUME(int VOLUME) {
        this.VOLUME = VOLUME;
    }

    private boolean recording;
    private MidiTrack noteTrack,tempoTrack;
    private List<MidiTrack> tracks;
    private MidiFile midiFile;
    private long startedAt;
    private int channel=-1;

    static {
        POSITION_TO_NOTES.put(1, 60);
        POSITION_TO_NOTES.put(2, 62);
        POSITION_TO_NOTES.put(3, 64);
        POSITION_TO_NOTES.put(4, 65);
        POSITION_TO_NOTES.put(5, 67);
        POSITION_TO_NOTES.put(6, 69);
        POSITION_TO_NOTES.put(7, 71);
        POSITION_TO_NOTES.put(8, 72);
        POSITION_TO_NOTES.put(9, 74);
        POSITION_TO_NOTES.put(10, 76);
        POSITION_TO_NOTES.put(11, 77);
        POSITION_TO_NOTES.put(12, 79);
        POSITION_TO_NOTES.put(13, 81);
        POSITION_TO_NOTES.put(14, 83);
        POSITION_TO_NOTES.put(15, 61);
        POSITION_TO_NOTES.put(16, 63);
        POSITION_TO_NOTES.put(17, 66);
        POSITION_TO_NOTES.put(18, 68);
        POSITION_TO_NOTES.put(19, 70);
        POSITION_TO_NOTES.put(20, 73);
        POSITION_TO_NOTES.put(21, 75);
        POSITION_TO_NOTES.put(22, 78);
        POSITION_TO_NOTES.put(23, 80);
        POSITION_TO_NOTES.put(24, 82);


        POSITION_TO_NOTES2.put("DO",1);
        POSITION_TO_NOTES2.put("RE",3);
        POSITION_TO_NOTES2.put("MI",5);
        POSITION_TO_NOTES2.put("FA",6);
        POSITION_TO_NOTES2.put("SO",8);
        POSITION_TO_NOTES2.put("LA",10);
        POSITION_TO_NOTES2.put("SI",12);
    }

    public MidiSoundController() {
        midi = new MidiDriver();
        if (midi != null)
            midi.setOnMidiStartListener(this);
        midi.start();
    }
    public void stopMidi(){
        midi.stop();
    }
    public void startMidi(){
        midi.start();
    }

    public void playNote(int note,int channel) {
        sendMidi(0x90,note,VOLUME,channel);
    }
    public void stopNote(int note,int channel){
        sendMidi(0x80,note,VOLUME,channel);
    }


    public void playNote2(String note,int octave,int half) {

        int actualMidiNote = 11+POSITION_TO_NOTES2.get(note)+ half + (12*octave);
        sendMidi(0x90, actualMidiNote, VOLUME,0);
        //SOUND_MAP.put(actualMidiNote,actualMidiNote);
        if (recording) {
            if (startedAt == 0)
                startedAt = getCurrentTicks() - 4 * MidiFile.DEFAULT_RESOLUTION;
            noteTrack.insertEvent(new NoteOn(getCurrentTicks() - startedAt, channel, actualMidiNote, VOLUME));
        }

    }

    public void stopNote2(String note,int octave,int half ) {
        int actualMidiNote = 11+POSITION_TO_NOTES2.get(note)+ half + 12*octave;

        sendMidi(0x80, actualMidiNote, 0,0);
        //SOUND_MAP.remove(actualMidiNote);
        if (recording)
        {
            noteTrack.insertEvent(new NoteOff(getCurrentTicks() - startedAt, channel, actualMidiNote, 0));
        }
    }

    @Override
    public void onMidiStart() {
        sendMidi();

        // Get the config
        int config[] = midi.config();
    }
    protected void sendMidi()
    {
        byte msg[] = new byte[2];

        msg[0] = (byte) 0xc0;
        msg[1] = (byte) 0;

        midi.write(msg);
    }
    public void changeMidiInstrument(int instrument)
    {
        currentInstrument=instrument;
        byte msg[] = new byte[2];

        msg[0] = (byte) 0xc0;
        msg[1] = (byte) instrument;

        midi.write(msg);
    }
    protected void sendMidi(int m, int n, int v,int channel)
    {
        byte msg[] = new byte[3];

        msg[0] = (byte) ((byte) m|channel);
        msg[1] = (byte) n;
        msg[2] = (byte) v;

        midi.write(msg);
    }
    public boolean isNotePlaying(int note){
        return SOUND_MAP.get(note)!=null;
    }

    public void startRecording(String path,int channel) throws IOException {
        this.channel=channel;
        File input =  input = new File(path);
        midiFile = new MidiFile(input);
        tracks = midiFile.getTracks();
        Tempo tempo=null;
        for (int i=0;i<tracks.size();i++) {
            Iterator<MidiEvent> it = tracks.get(i).getEvents().iterator();
            while(it.hasNext())
            {
                MidiEvent event = it.next();
                if(event instanceof Tempo)
                {
                    tempo = (Tempo)event;

                }
            }
        }
        if (tempo==null){

            tempoTrack = new MidiTrack();

            TimeSignature ts = new TimeSignature();
            ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
            Tempo t = new Tempo();
            t.setBpm(120);
            tempoTrack.insertEvent(ts);
            tempoTrack.insertEvent(t);
            midiFile.addTrack(tempoTrack);
        }
        noteTrack = new MidiTrack();
        noteTrack.insertEvent(new ProgramChange(0, channel, currentInstrument));
        // noteTrack.insertEvent(new NoteOff(4 * MidiFile.DEFAULT_RESOLUTION, 0, 1, 0));
        recording = true;
        startedAt = 0;
    }


    public long stopRecording(String path) throws IOException {
        midiFile.addTrack(noteTrack);
        recording = false;
        File output = new File(path);
        midiFile.writeToFile(output);
        Log.d("channel","channel is "+channel);
        return midiFile.getLengthInTicks();

    }
    public void setBpm(int bpm) {
        Tempo t = new Tempo();
        t.setBpm(bpm);
        if (recording)
            tempoTrack.insertEvent(t);
    }
    private long getCurrentTicks() {
        return SystemClock.uptimeMillis();
    }


    public void makeExemple() {
        recording=true;
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack = new MidiTrack();

        // 2. Add events to the tracks
        // Track 0 is the tempo map
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        Tempo tempo = new Tempo();
        tempo.setBpm(228);

        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(tempo);

// Track 1 will have some notes in it
        final int NOTE_COUNT = 80;

        for(int i = 0; i < NOTE_COUNT; i++)
        {
            int channel = 0;
            int pitch = 1 + i;
            int velocity = 100;
            long tick = i * 480;
            long duration = 120;

            noteTrack.insertNote(channel, pitch, velocity, tick, duration);
        }

// 3. Create a MidiFile with the tracks we created
        List<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrack);
        File output = new File(Environment.getExternalStorageDirectory().getPath()+"/test.mid");
        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);
        try
        {
            midi.writeToFile(output);
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }
}
