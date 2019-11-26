package com.bassemgharbi.androidpiano;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chengtao.pianoview.entity.Piano;
import com.chengtao.pianoview.listener.OnPianoListener;
import com.chengtao.pianoview.view.PianoView;

import java.io.IOException;

import it.beppi.knoblibrary.Knob;

import static java.lang.String.format;

public final class PianoActivity extends AppCompatActivity implements
        OnPianoListener, SeekBar.OnSeekBarChangeListener,
        View.OnClickListener,MediaPlayer.OnPreparedListener
{
    private boolean statePressed=false;
    private PianoView pianoView;
    private SeekBar seekBar;
    private Button leftArrow;
    private Button rightArrow;
    private Button btnMusic;
    private int scrollProgress = 0;
    private final static float SEEKBAR_OFFSET_SIZE = -12;
    //
    public static final int PICKFILE_RESULT_CODE = 1;
    private Button btnPlay;
    private Button btnPouse;
    private int current = 0;
    private boolean   running = true;
    private	int duration = 0;
    private MediaPlayer mPlayer;
    private SeekBar mSeekBarPlayer;
    private TextView mMediaTime;
    private boolean record = false;




    //
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
        setContentView(R.layout.activity_piano);
        //view
        pianoView = findViewById(R.id.pv);
        seekBar = findViewById(R.id.sb);
        seekBar.setThumbOffset((int) convertDpToPixel(SEEKBAR_OFFSET_SIZE));
        leftArrow = findViewById(R.id.iv_left_arrow);
        rightArrow = findViewById(R.id.iv_right_arrow);
        btnMusic = findViewById(R.id.iv_music);

        //listener
        pianoView.setPianoListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        rightArrow.setOnClickListener(this);
        leftArrow.setOnClickListener(this);
        btnMusic.setOnClickListener(this);
        //init

        int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        knob = (Knob) findViewById(R.id.midiKnob);
        knob.setState(10);
        knob.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                Intent intent = new Intent("volume midi changed");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intent.putExtra("midiVelocity",6*state);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        });


        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.imagine);
        btnPlay = (Button) findViewById(R.id.button1);
        mMediaTime = (TextView)findViewById(R.id.mediaTime);
        mSeekBarPlayer = (SeekBar)findViewById(R.id.progress_bar);
        mPlayer.setOnPreparedListener(this);
        mMediaTime.setText(format("%02d:%02d / %02d:%02d", 0, 0, 0, 0));

        mSeekBarPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mPlayer.seekTo(progress);
                    updateTime();
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               statePressed=!statePressed;

               if (statePressed)
                   btnPlay.setBackgroundResource(R.drawable.pause);
               else
                   btnPlay.setBackgroundResource(R.drawable.play);
                // TODO Auto-generated method stub
                if (!statePressed)
                {
                    mPlayer.pause();
                }
                else{

                try {
                    mPlayer.prepare();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mPlayer.start();
                mSeekBarPlayer.postDelayed(onEverySecond, 1000);
            }
            }

        });






    }

    /**
     * 初始化小星星列表
     */

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
        /**
         * 设置为横屏
         */
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

    /**
     * Dp to px
     *
     * @param dp dp值
     * @return px 值
     */
    private float convertDpToPixel(float dp) {
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        if (pianoView != null) {
            //pianoView.releaseAutoPlay();
        }
    }

    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run(){
            if(true == running){
                if(mSeekBarPlayer != null) {
                    mSeekBarPlayer.setProgress(mPlayer.getCurrentPosition());
                }

                if(mPlayer.isPlaying()) {
                    mSeekBarPlayer.postDelayed(onEverySecond, 1000);
                    updateTime();
                }
            }
        }
    };

    private void updateTime(){
        do {
            current = mPlayer.getCurrentPosition();
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
        // TODO Auto-generated method stub
        duration = mPlayer.getDuration();
        mSeekBarPlayer.setMax(duration);
        mSeekBarPlayer.postDelayed(onEverySecond, 1000);
    }


    public void InstrumentDialog(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FireMissilesDialogFragment editNameDialogFragment = FireMissilesDialogFragment.newInstance("Some Title");
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    public void record(View view) {
        record=!record;
        Intent intent = new Intent("Record Start");
        intent.putExtra("Record",record);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

    }

    public void choose(View view) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    Log.d("YOOO",fileUri.toString());
                    mPlayer.reset();
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mPlayer.setDataSource(getApplicationContext(),fileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

}
