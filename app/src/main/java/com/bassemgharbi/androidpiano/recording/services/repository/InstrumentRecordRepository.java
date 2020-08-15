package com.bassemgharbi.androidpiano.recording.services.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.bassemgharbi.androidpiano.R;
import com.bassemgharbi.androidpiano.improv.services.model.Instrument;
import com.bassemgharbi.androidpiano.improv.services.repository.InstrumentChooserRepository;
import com.bassemgharbi.androidpiano.recording.services.model.InstrumentRecord;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.ChannelEvent;
import com.leff.midi.event.Controller;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InstrumentRecordRepository {

    static  List<InstrumentRecord> trackList = new ArrayList<>();
    private InstrumentChooserRepository instrumentChooserRepository = InstrumentChooserRepository.getInstance();
    private static InstrumentRecordRepository single_instance = null;
    private MidiFile midi;
    private boolean changed=false;


    private File input;
    private Tempo tempoEvent;
    private String path;
    public static InstrumentRecordRepository getInstance(String path){
        if (single_instance ==null){
            single_instance = new InstrumentRecordRepository(path);

        }
        return single_instance;
    }
    private  InstrumentRecordRepository(String path){
        loadMidiFile(path);
    }

    private void loadMidiFile(String path){
        this.path = path;
        input = new File(this.path);
        try {
            midi = new MidiFile(input);
            List<MidiTrack> tracks = midi.getTracks();
            setTracks(tracks);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void setTracks(List<MidiTrack> tracks ){
        for (int i=0;i<tracks.size();i++) {
            Log.d(tracks.size()+"yo","yooooooo");
            Iterator<MidiEvent> it = tracks.get(i).getEvents().iterator();
            while(it.hasNext())
            {
                MidiEvent event = it.next();
                if(event instanceof Tempo)
                {
                    tempoEvent = (Tempo)event;
                }
                if(event instanceof ProgramChange)
                {
                    // Log.d("yoooooooooooo",((ProgramChange)event).getProgramNumber()+1+"track number :"+i);
                    trackList.add(new InstrumentRecord(R.drawable.the_bridge, tracks.get(i), tempoEvent,instrumentChooserRepository.getInstruments().getValue().get(((ProgramChange)event).getProgramNumber()),midi.getResolution(),path));
                }
            }
        }

    }
    public MutableLiveData<List<InstrumentRecord>> getTracks(String path) {
        final MutableLiveData<List<InstrumentRecord>> data =
                new MutableLiveData<>();
        data.setValue(trackList);
        return data;
    }
    public MutableLiveData<Boolean> getInputChanged() {
        final MutableLiveData<Boolean> data =
                new MutableLiveData<>();
        data.setValue(false);
        return data;
    }

    public Tempo getTempo(){
        return tempoEvent;
    }

    public  void muteTrack(int track,int channel) throws IOException {
        boolean unMute = false;
        Controller eventToDelete =null;
        Iterator<MidiEvent> it =  midi.getTracks().get(track).getEvents().iterator();
        while(it.hasNext()) {
            MidiEvent event = it.next();
            if (event instanceof Controller)
            {
                if (((Controller) event).getControllerType()==0x07){
                    eventToDelete = (Controller)event;
                    unMute = true;

                }
            }

        }
        if (unMute)
        {
            midi.getTracks().get(track).getEvents().remove(eventToDelete);
            writeToFile();
            Log.d("Controller type","unmute");
        }
            if (!unMute) {
                midi.getTracks().get(track).getEvents().add(new Controller(0, channel, 0x07, 0));
                writeToFile();
                Log.d("Controller type","mute");
            }

    }
    public  void unmuteTrack(int track,int channel,int velocity) throws IOException {

        Iterator<MidiEvent> it =  midi.getTracks().get(track).getEvents().iterator();
        while(it.hasNext()) {
            MidiEvent event = it.next();
            if (event instanceof Controller)
            {
                if (((Controller) event).getControllerType()==0x07){
                    midi.getTracks().get(track).getEvents().remove((Controller) event);

                }
            }

        }

    }
    private void writeToFile() throws IOException {
        midi.writeToFile(input);
    }
    public static void clear()
    {
        single_instance = null;
        trackList.clear();
    }
}
