package com.bassemgharbi.androidpiano.Drums.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bassemgharbi.androidpiano.Drums.ViewModel.drumsViewModel;
import com.bassemgharbi.androidpiano.R;
import com.bassemgharbi.androidpiano.improv.view.ui.InstrumentsChooserFragment;
import com.bassemgharbi.androidpiano.recording.viewModel.InstrumentRecordViewModel;

import java.io.IOException;

import it.beppi.knoblibrary.Knob;

import static java.lang.String.format;

public class DrumsActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    private drumsViewModel drumsViewModel;
    boolean first=false;

    private boolean statePressed=false;
    public static final int PICKFILE_RESULT_CODE = 1;
    private Button btnPlay;
    private int current = 0;
    private boolean   running = true;
    private	int duration = 0;
    private MediaPlayer mPlayer;
    private SeekBar mSeekBarPlayer;
    private TextView mMediaTime;
    private boolean record = false;

    Knob knob;
    private Uri fileUri;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drums);
        drumsViewModel = ViewModelProviders.of(this).get(drumsViewModel.class);
        drumsViewModel.init();
        knob = (Knob) findViewById(R.id.midiKnob);
        knob.setState(10);
        knob.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                drumsViewModel.getInstrumentChooserRepository().changeVolume(state);
            }
        });

        int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
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
                    btnPlay.setBackgroundResource(R.drawable.pausebuttongrey);
                else
                    btnPlay.setBackgroundResource(R.drawable.playbuttongrey);
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
        mPlayer.setOnCompletionListener(onCompletionListener);

        loadFragment(new drumsFragment());

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        drumsViewModel.getInstrumentChooserRepository().clear();
        finish();
    }
    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }

    public void changeDrums(View view) {
        Toast.makeText(getBaseContext(), "My Account",Toast.LENGTH_SHORT).show();
        if (!first){
            loadFragment(new drumsPadFragment());
        }
        else{
            loadFragment(new drumsFragment());
        }
        first = !first;

    }
    private float convertDpToPixel(float dp) {
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
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
        InstrumentsChooserFragment editNameDialogFragment = InstrumentsChooserFragment.newInstance("Some Title");
        editNameDialogFragment.show(fm, "fragment_edit_name");
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

                }

                break;
        }
        try {
            resetPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void resetPlayer() throws IOException {

        mMediaTime.setText(format("%02d:%02d / %02d:%02d", 0, 0, 0, 0));
        mSeekBarPlayer.removeCallbacks(onEverySecond);
        mSeekBarPlayer.setProgress(0);
        mSeekBarPlayer.postDelayed(onEverySecond,1000);
        current=0;
        btnPlay.setBackgroundResource(R.drawable.playbuttongrey);
        mPlayer.reset();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(getApplicationContext(),fileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.prepare();
        statePressed=false;
    }
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


}
