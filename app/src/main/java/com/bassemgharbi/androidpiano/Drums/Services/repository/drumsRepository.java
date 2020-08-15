package com.bassemgharbi.androidpiano.Drums.Services.repository;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bassemgharbi.androidpiano.MidiSoundController;
import com.bassemgharbi.androidpiano.improv.services.model.Instrument;

import java.util.ArrayList;
import java.util.List;

public class drumsRepository {
    private MidiSoundController midiSoundController;
    private static drumsRepository single_instance = null;
    private Context context;
    public static drumsRepository getInstance(){
        if (single_instance ==null){
            single_instance = new drumsRepository();
        }
        return single_instance;
    }

    public drumsRepository() {
        this.midiSoundController = new MidiSoundController();
    }


    public void playNote(int note){
        midiSoundController.playNote(note,9);
    }
    public void stopNote(int note){
        midiSoundController.playNote(note,9);
    }
    public void changeVolume(int state){
                midiSoundController.setVOLUME(6*state);

    }
    public void clear(){
        midiSoundController.stopMidi();
        single_instance =null;
    }

}
