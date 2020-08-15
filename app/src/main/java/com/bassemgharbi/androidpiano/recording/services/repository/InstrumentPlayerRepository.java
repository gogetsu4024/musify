package com.bassemgharbi.androidpiano.recording.services.repository;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.bassemgharbi.androidpiano.R;
import com.bassemgharbi.androidpiano.improv.services.repository.InstrumentChooserRepository;
import com.bassemgharbi.androidpiano.recording.services.model.InstrumentRecord;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.Controller;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;

public class InstrumentPlayerRepository {

    private static InstrumentPlayerRepository single_instance = null;

    private MediaPlayer mPlayer;
    private String path;
    public static InstrumentPlayerRepository getInstance(String path) throws IOException {
        if (single_instance ==null){
            single_instance = new InstrumentPlayerRepository(path);

        }
        return single_instance;
    }
    private InstrumentPlayerRepository(String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        FileDescriptor fileDescriptor = fileInputStream.getFD();
        mPlayer = new MediaPlayer();
        mPlayer.setDataSource(fileDescriptor);
        this.path=path;
        //
    }
    public void resetPlayer(){

        mPlayer.reset();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(path));
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            mPlayer.setDataSource(fileDescriptor);
           // 2143
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void changeMedia(String path) throws IOException {
        mPlayer.setDataSource(path);
    }
    public MediaPlayer getmPlayer() {
        return mPlayer;
    }

    public void setmPlayer(MediaPlayer mPlayer) {
        this.mPlayer = mPlayer;
    }

    public static void clear()
    {
        single_instance = null;

    }
}
