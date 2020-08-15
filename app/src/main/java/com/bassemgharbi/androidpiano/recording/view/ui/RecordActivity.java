package com.bassemgharbi.androidpiano.recording.view.ui;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bassemgharbi.androidpiano.Instruments.Instruments;
import com.bassemgharbi.androidpiano.Menu.view.adapters.Adapter;
import com.bassemgharbi.androidpiano.R;
import com.bassemgharbi.androidpiano.improv.services.model.Instrument;
import com.bassemgharbi.androidpiano.improv.viewModel.InstrumentChooserViewModel;
import com.bassemgharbi.androidpiano.recording.services.model.InstrumentRecord;
import com.bassemgharbi.androidpiano.recording.services.repository.InstrumentRecordRepository;
import com.bassemgharbi.androidpiano.recording.view.adapters.MyAdapter;
import com.bassemgharbi.androidpiano.recording.viewModel.InstrumentRecordViewModel;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
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

public class RecordActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    List<InstrumentRecord> models;
    private RecyclerView recyclerView1;
    private RecyclerView.LayoutManager layoutManager1;
    private RecyclerView.Adapter mAdapter1;
    private InstrumentRecordViewModel instrumentRecordViewModel;

    private Button btnPlay;
    private int current = 0;
    private boolean   running = true;
    private	int duration = 0;
    private SeekBar mSeekBarPlayer;
    private TextView mMediaTime;
    private boolean record = false;
    private boolean statePressed=false;
    private File input;
    private MidiFile midi;
    private Tempo tempoEvent;
    ArrayList<Instrument> instruments;
    private String path;
    private int channel=0;
    private Runnable onEverySecond;
    private boolean prepared=false;
    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            try {
                resetPlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Intent intent = this.getIntent();
        path = intent.getStringExtra("FilePath");
        recyclerView1 =  findViewById(R.id.recycle_view_record);



        instrumentRecordViewModel = ViewModelProviders.of(this)
                .get(InstrumentRecordViewModel.class);
        try {
            instrumentRecordViewModel.init(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        instrumentRecordViewModel.getTracks().observe(this, new Observer<List<InstrumentRecord>>() {
            @Override
            public void onChanged(List<InstrumentRecord> instruments) {
                mAdapter1.notifyDataSetChanged();
            }
        });

        initRecyclerView();





        instrumentRecordViewModel.getFile().observe(this, new Observer<Boolean>(){
            @Override
            public void onChanged(Boolean changed) {
                Log.d("something","something");
                //resetPlayer();
            }


        });


        btnPlay = findViewById(R.id.button1);
        mMediaTime = (TextView)findViewById(R.id.mediaTime);
        mSeekBarPlayer = (SeekBar)findViewById(R.id.progress_bar);

        mMediaTime.setText(format("%02d:%02d / %02d:%02d", 0, 0, 0, 0));
        instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().setOnPreparedListener(this);
        try {
            instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().setOnCompletionListener(onCompletionListener);
        mSeekBarPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().seekTo(progress);
                    updateTime();
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                statePressed=!statePressed;

                if (statePressed)
                    btnPlay.setBackgroundResource(R.drawable.pausebuttongrey);
                else
                    btnPlay.setBackgroundResource(R.drawable.playbuttongrey);
                if (!statePressed)
                {
                    instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().pause();
                }
                else{
                    instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().start();
                    mSeekBarPlayer.postDelayed(onEverySecond, 1000);
                }
            }

        });
        onEverySecond = new Runnable() {
            @Override
            public void run(){
                if(running){
                    if(mSeekBarPlayer != null) {
                        mSeekBarPlayer.setProgress(instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().getCurrentPosition());
                    }

                    if(instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().isPlaying()) {
                        mSeekBarPlayer.postDelayed(onEverySecond, 1000);
                        updateTime();
                    }
                }
            }
        };
    }


    private void updateTime(){
        do {
            current = instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().getCurrentPosition();
            System.out.println("duration - " + duration + " current- "
                    + current);
            int dSeconds = (int) (duration / 1000) % 60 ;
            int dMinutes = (int) ((duration / (1000*60)) % 60);
            int dHours   = (int) ((duration / (1000*60*60)) % 24);

            int cSeconds = (int) (current / 1000) % 60 ;
            int cMinutes = (int) ((current / (1000*60)) % 60);
            int cHours   = (int) ((current / (1000*60*60)) % 24);

            if(dHours == 0){
                mMediaTime.setText(format("%02d:%02d / %02d:%02d", cMinutes, cSeconds, dMinutes, dSeconds));
            }else{
                mMediaTime.setText(format("%02d:%02d:%02d / %02d:%02d:%02d", cHours, cMinutes, cSeconds, dHours, dMinutes, dSeconds));
            }

            try{
                Log.d("Value: ", String.valueOf((int) (current * 100 / duration)));
                if(mSeekBarPlayer.getProgress() >= 100){
                    break;
                }
            }catch (Exception e) {}
        }while (mSeekBarPlayer.getProgress() <= 100);
    }

    @Override
    public void onPrepared(MediaPlayer arg0) {

        duration = instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().getDuration();
        mSeekBarPlayer.setMax(duration);
        mSeekBarPlayer.postDelayed(onEverySecond, 1000);
    }

    public void resetPlayer() throws IOException {

        mMediaTime.setText(format("%02d:%02d / %02d:%02d", 0, 0, 0, 0));
        mSeekBarPlayer.removeCallbacks(onEverySecond);
        mSeekBarPlayer.setProgress(0);
        mSeekBarPlayer.postDelayed(onEverySecond,2000);
        current=0;
        btnPlay.setBackgroundResource(R.drawable.playbuttongrey);
        instrumentRecordViewModel.getInstrumentPlayerRepository().resetPlayer();
        instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().prepare();
        statePressed=false;
    }

    public void startRecord(View view) {

        Intent intent1 = this.getIntent();
        String path = intent1.getStringExtra("FilePath");
        Log.e("error","path is "+path);

        Intent intent=new Intent(RecordActivity.this, StartRecordActivity.class);
        intent.putExtra("Path",path);
        intent.putExtra("Record",true);
        intent.putExtra("channel",channel);


        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2)
        {
            //mAdapter1.notifyDataSetChanged();

            instrumentRecordViewModel.clear();
            try {
                instrumentRecordViewModel.init(path);
                resetPlayer();
                instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().setOnCompletionListener(onCompletionListener);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mAdapter1.notifyDataSetChanged();
            instrumentRecordViewModel.notifyChange();
            channel++;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        instrumentRecordViewModel.clear();
        //instrumentRecordViewModel.getInstrumentPlayerRepository().getmPlayer().reset();
        finish();
    }
    public void initRecyclerView(){
        layoutManager1 = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager1);
        mAdapter1 = new MyAdapter(instrumentRecordViewModel.getTracks().getValue());
        recyclerView1.setAdapter(mAdapter1);

    }
}
