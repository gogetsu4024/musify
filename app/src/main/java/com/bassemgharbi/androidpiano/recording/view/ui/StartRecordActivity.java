package com.bassemgharbi.androidpiano.recording.view.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bassemgharbi.androidpiano.R;
import com.bassemgharbi.androidpiano.improv.view.ui.InstrumentsChooserFragment;
import com.chengtao.pianoview.entity.Piano;
import com.chengtao.pianoview.listener.OnPianoListener;
import com.chengtao.pianoview.view.PianoView;

import it.beppi.knoblibrary.Knob;

public class StartRecordActivity extends AppCompatActivity implements
        OnPianoListener, SeekBar.OnSeekBarChangeListener,
        View.OnClickListener
{
    private boolean statePressed=false;
    private PianoView pianoView;
    private SeekBar seekBar;
    private Button leftArrow;
    private Button rightArrow;
    private Button btnMusic;
    private Button btnPlay;
    private int scrollProgress = 0;
    private final static float SEEKBAR_OFFSET_SIZE = -12;
    //
    TextView timerTextView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
            if (seconds%2 ==1){
                btnPlay.setBackgroundResource(R.drawable.record2grey);
            }
            if (seconds%2 ==0){
                btnPlay.setBackgroundResource(R.drawable.record);

            }

            timerHandler.postDelayed(this, 500);
        }
    };


    private boolean record = false;
    private int stateChange=0;
    Knob knob;
    private Uri fileUri;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_record);
        //view
        pianoView = findViewById(R.id.pv);
        seekBar = findViewById(R.id.sb);
        seekBar.setThumbOffset((int) convertDpToPixel(SEEKBAR_OFFSET_SIZE));
        leftArrow = findViewById(R.id.iv_left_arrow);
        rightArrow = findViewById(R.id.iv_right_arrow);
        btnMusic = findViewById(R.id.iv_music);
        btnPlay = findViewById(R.id.button1);

        //listener
        pianoView.setPianoListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        rightArrow.setOnClickListener(this);
        leftArrow.setOnClickListener(this);
        btnMusic.setOnClickListener(this);
        //init

        int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        knob = (Knob) findViewById(R.id.midiKnob);
        knob.setState(10);
        knob.setOnStateChanged(state -> {
            Intent intent = new Intent("volume midi changed");
            intent.putExtra("midiVelocity",6*state);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        });
        timerTextView = (TextView) findViewById(R.id.timer);
    }


    @Override public void onPianoInitFinish() {

    }

    @Override
    public void onPianoClick(Piano.PianoKeyType pianoKeyType, Piano.PianoVoice pianoVoice, int i, int i1) {
        //  Log.d(pianoVoice.name(),i+"");


    }


    @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        pianoView.scroll(i);
    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override protected void onResume() {

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override public void onClick(View view) {
        if (scrollProgress == 0) {
            try {
                scrollProgress = (pianoView.getLayoutWidth() * 100) / pianoView.getPianoWidth();
            } catch (Exception e) {

            }
        }
        int progress;
        switch (view.getId()) {
            case R.id.iv_left_arrow:
                if (scrollProgress == 0) {
                    progress = 0;
                } else {
                    progress = seekBar.getProgress() - scrollProgress;
                    if (progress < 0) {
                        progress = 0;
                    }
                }
                seekBar.setProgress(progress);
                break;
            case R.id.iv_right_arrow:
                if (scrollProgress == 0) {
                    progress = 100;
                } else {
                    progress = seekBar.getProgress() + scrollProgress;
                    if (progress > 100) {
                        progress = 100;
                    }
                }
                seekBar.setProgress(progress);
                break;
            case R.id.iv_music:
                break;
        }
    }


    private float convertDpToPixel(float dp) {
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        pianoView.pause();
        if (pianoView != null) {
            setResult(2);
            finish();
            //tinsech free il midi player si nn yjik il sout il mbaghbich
            //pianoView.
        }
    }



    public void InstrumentDialog(View view) {
        FragmentManager fm = getSupportFragmentManager();
        InstrumentsChooserFragment editNameDialogFragment = InstrumentsChooserFragment.newInstance("Some Title");
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    public void record(View view) {
        record=!record;
        if (record&&stateChange==0){
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
            stateChange=1;
        }
        if (!record&&stateChange==1){
            //stateChange=0;
            timerHandler.removeCallbacks(timerRunnable);
            setResult(2);
            finish();
        }

        Intent intent = this.getIntent();
        String path = intent.getStringExtra("Path");
        int channel = intent.getIntExtra("channel",0);

        Intent intent2 = new Intent("Record Start");
        intent2.putExtra("Path",path);
        intent2.putExtra("Record",record);
        intent2.putExtra("channel",channel);


        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent2);

    }
    @Override
    public void onBackPressed() {
        if (!record)
        {
            pianoView.pause();
            Intent intent=new Intent();
            setResult(2,intent);
            finish();
        }
        else
            Toast.makeText(getApplicationContext(),"You are currently recording !",Toast.LENGTH_SHORT).show();


    }
    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button)findViewById(R.id.button);

    }

}
